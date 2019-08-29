import com.oocourse.TimableOutput;
import java.util.ArrayList;

public class Elevator extends Thread {
    private final String elevatorId;
    private final int capacity;
    private final int moveOneFloorTime;
    private final int doorOpenCloseTime;
    private final Scheduler scheduler;

    private Floor floor;
    private int currentFloor = 1;
    private int currentDirection = 1;
    private int currentPersonNum = 0;
    private boolean doorClosed = true;

    public Elevator(String elevatorId,
                    int capacity,
                    int moveOneFloorTime,
                    int doorOpenCloseTime,
                    Floor floor,
                    Scheduler scheduler) {
        this.elevatorId = elevatorId;
        this.capacity = capacity;
        this.moveOneFloorTime = moveOneFloorTime;
        this.doorOpenCloseTime = doorOpenCloseTime;
        this.floor = floor;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        if (scheduler.isFinished(elevatorId, floor)) {
            return;
        }
        checkDirection();
        personIn();

        while (true) {
            if (scheduler.isFinished(elevatorId, floor)) {
                // System.out.println(elevatorId + " shuts down");
                break;
            }
            move();
            personOut();
            checkDirection();
            personIn();
        }
    }

    private void move() {
        try {
            Thread.sleep(moveOneFloorTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (currentDirection == 1) {
            if (currentFloor == -1) {
                currentFloor = 1;
            } else {
                currentFloor++;
            }
        } else {
            if (currentFloor == 1) {
                currentFloor = -1;
            } else {
                currentFloor--;
            }
        }

        TimableOutput.println(
                String.format("ARRIVE-%d-%s", currentFloor, elevatorId));
    }

    private void checkDirection() {
        if (currentPersonNum == 0 && !scheduler.isInRightDirection(
                floor, currentFloor, currentDirection)) {
            currentDirection = -currentDirection;
            // System.out.println(String.format(
            // "%s: Direction turned at floor %d, now go %s",
            // elevatorId, currentFloor,
            // (currentDirection == 1) ? "up" : "down"));
        }
    }

    private void personIn() {
        if (!floor.isReachable(currentFloor)) {
            return;
        }

        ArrayList<Request> inRequests =
                scheduler.getInRequests(elevatorId, floor, currentFloor,
                        currentDirection, currentPersonNum, capacity);
        if (!inRequests.isEmpty()) {
            open();
            for (Request request : inRequests) {
                TimableOutput.println(String.format("IN-%d-%d-%s",
                        request.getPersonId(), currentFloor, elevatorId));
                currentPersonNum++;
            }
            inRequests = scheduler.getInRequests(elevatorId, floor, currentFloor
                    , currentDirection, currentPersonNum, capacity);
            for (Request request : inRequests) {
                TimableOutput.println(String.format("IN-%d-%d-%s",
                        request.getPersonId(), currentFloor, elevatorId));
                currentPersonNum++;
            }
        }
        close();
    }

    private void personOut() {
        if (!floor.isReachable(currentFloor)) {
            return;
        }

        ArrayList<Request> outRequests =
                scheduler.getOutRequests(elevatorId, currentFloor);
        if (!outRequests.isEmpty()) {
            open();
            for (Request request : outRequests) {
                TimableOutput.println(String.format("OUT-%d-%d-%s",
                        request.getPersonId(), currentFloor, elevatorId));
                currentPersonNum--;
                scheduler.putSecondRequest(request);
            }
        }
    }

    private void open() {
        if (!doorClosed) {
            return;
        }

        TimableOutput.println(String.format("OPEN-%d-%s",
                currentFloor, elevatorId));
        try {
            Thread.sleep(doorOpenCloseTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        doorClosed = false;
    }

    private void close() {
        if (doorClosed) {
            return;
        }

        try {
            Thread.sleep(doorOpenCloseTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimableOutput.println(String.format("CLOSE-%d-%s",
                currentFloor, elevatorId));

        doorClosed = true;
    }
}