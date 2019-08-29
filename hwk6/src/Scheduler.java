import java.util.ArrayList;
import com.oocourse.elevator2.PersonRequest;

public class Scheduler {
    private ArrayList<PersonRequest> requestList = new ArrayList<>();
    private ArrayList<PersonRequest> incidentList = new ArrayList<>();
    private int currentFloor = 1;       // 初始位置为1层
    private int currentDirection = 0;   // 1表示上行，-1表示下行， 0表示静止
    private boolean finished = false;

    public synchronized void putRequest(PersonRequest request) {
        requestList.add(request);
        if (requestList.size() == 1) {
            notifyAll();
        }
    }

    public synchronized PersonRequest getRequest() {
        if (finished && requestList.isEmpty()) {
            return null;
        }

        while (!finished && requestList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return requestList.remove(0);
    }

    public synchronized PersonRequest getIncidentRequest() {
        if (incidentList.isEmpty()) {
            return null;
        }
        return incidentList.remove(0);
    }

    public synchronized ArrayList<PersonRequest> getToDoList(int type) {
        /* type: -1下客，1上客，0皆有 */
        PersonRequest request;
        int fromFloor;
        int toFloor;
        int direction;
        ArrayList<PersonRequest> toDoList = new ArrayList<>();

        if (type == 0 || type == -1) {
            for (int i = 0; i < incidentList.size(); i++) {
                request = incidentList.get(i);
                toFloor = request.getToFloor();

                if (toFloor == currentFloor) {
                    incidentList.remove(i);
                    toDoList.add(request);
                    i--;
                }
            }
        }

        if (type == 0 || type == 1) {
            for (int i = 0; i < requestList.size(); i++) {
                request = requestList.get(i);
                fromFloor = request.getFromFloor();
                toFloor = request.getToFloor();
                if (toFloor > fromFloor) {
                    direction = 1;
                } else if (toFloor < fromFloor) {
                    direction = -1;
                } else {
                    direction = 0;
                }

                if (fromFloor == currentFloor && (direction == currentDirection
                        || currentDirection == 0)) {
                    incidentList.add(request);
                    requestList.remove(i);
                    toDoList.add(request);
                    i--;
                }
            }
        }

        return toDoList;
    }

    public void setCurrentFloor(int currentFloor) {
        if (currentFloor <= 0) {
            this.currentFloor = currentFloor - 1;
            return;
        }
        this.currentFloor = currentFloor;
    }

    public void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }

    public void setFinished() {
        this.finished = true;
    }
}