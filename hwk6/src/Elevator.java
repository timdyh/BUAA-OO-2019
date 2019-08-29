import java.util.ArrayList;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.TimableOutput;

public class Elevator extends Thread {
    private static final int transportTimePerFloor = 400;
    private static final int doorOpenCloseTime = 200;

    private Scheduler scheduler;
    private PersonRequest request = null;
    private int personId;
    private int fromFloor;
    private int toFloor;
    private int currentFloor = 1;
    private int currentDirection = 0;
    private ArrayList<PersonRequest> toDoList;

    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        while (true) {
            /* 接到新的主请求，先去接乘客 */
            if (request == null) {
                if ((request = scheduler.getRequest()) == null) {
                    break;
                }
                parseRequest();
                checkDirection(fromFloor);
                transport(fromFloor);
                checkDirection(toFloor);
                open();
                personIn(personId);
                toDoList = scheduler.getToDoList(1);    // 捎带
                doList();
                close();
            }

            /* 接到乘客，送往目的楼层 */
            transport(toFloor);
            open();
            personOut(personId);
            scheduler.setCurrentDirection(currentDirection = 0);
            toDoList = scheduler.getToDoList(-1);   // 放下已到达的捎带乘客
            doList();

            /* 已送达，检查电梯内是否还有乘客，若有则优先服务电梯内的乘客，
             * 否则服务刚刚捎带上的乘客（如果有的话），
             * 实际上是将捎带请求转化为了主请求 */
            if ((request = scheduler.getIncidentRequest()) != null) {
                parseRequest();
                checkDirection(toFloor);
            }
            toDoList = scheduler.getToDoList(1);    // 捎带
            doList();
            close();
            if (request == null) {
                if ((request = scheduler.getIncidentRequest()) != null) {
                    parseRequest();
                    checkDirection(toFloor);
                }
            }
        }
    }

    private void parseRequest() {
        personId = request.getPersonId();
        fromFloor = floorPlus1(request.getFromFloor());
        toFloor = floorPlus1(request.getToFloor());
    }

    private void checkDirection(int targetFloor) {
        if (targetFloor > currentFloor) {
            currentDirection = 1;
        } else if (targetFloor < currentFloor) {
            currentDirection = -1;
        } else {
            currentDirection = 0;
        }
        scheduler.setCurrentDirection(currentDirection);
    }

    private void transport(int targetFloor) {
        while (currentFloor != targetFloor) {
            toDoList = scheduler.getToDoList(0);
            if (!toDoList.isEmpty()) {
                open();
                doList();
                close();
            }
            moveOneFloor(currentDirection);
        }
    }

    private void doList() {
        if (!toDoList.isEmpty()) {
            for (PersonRequest request : toDoList) {
                if (floorPlus1(request.getFromFloor()) == currentFloor) {
                    personIn(request.getPersonId());
                }
                if (floorPlus1(request.getToFloor()) == currentFloor) {
                    personOut(request.getPersonId());
                }
            }
        }
    }

    private void moveOneFloor(int direction) {
        try {
            Thread.sleep(transportTimePerFloor);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (direction == 1) {
            TimableOutput.println("ARRIVE-" + floorMinus1(++currentFloor));
        } else if (direction == -1) {
            TimableOutput.println("ARRIVE-" + floorMinus1(--currentFloor));
        }
        scheduler.setCurrentFloor(currentFloor);
    }

    private void open() {
        TimableOutput.println("OPEN-" + floorMinus1(currentFloor));
        try {
            Thread.sleep(doorOpenCloseTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            Thread.sleep(doorOpenCloseTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimableOutput.println("CLOSE-" + floorMinus1(currentFloor));
    }

    private void personIn(int personId) {
        TimableOutput.println(
                "IN-" + personId + "-" + floorMinus1(currentFloor));
    }

    private void personOut(int personId) {
        TimableOutput.println(
                "OUT-" + personId + "-" + floorMinus1(currentFloor));
    }

    private int floorPlus1(int floor) {
        if (floor < 0) {
            return floor + 1;
        }
        return floor;
    }

    private int floorMinus1(int floor) {
        if (floor <= 0) {
            return floor - 1;
        }
        return floor;
    }
}