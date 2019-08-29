import java.util.ArrayList;
import java.util.HashMap;

public class Scheduler {
    private final Floor[] reachableFloor;
    private final int elevatorNum;

    private ArrayList<Request> requestQueue = new ArrayList<>();
    private HashMap<Request, Request> requestHub = new HashMap<>();
    private boolean finished = false;

    public Scheduler(Floor[] reachableFloor, int elevatorNum) {
        this.reachableFloor = reachableFloor;
        this.elevatorNum = elevatorNum;
    }

    public synchronized boolean isFinished(String elevatorId, Floor floor) {
        if (finished && requestQueue.isEmpty() && requestHub.isEmpty()) {
            notifyAll();
            return true;
        }

        while (!finished && isFree(elevatorId, floor)) {
            try {
                // System.out.println(elevatorId + " is waiting");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // System.out.println(elevatorId + " wakes up");
        }

        return false;
    }

    private synchronized boolean isFree(String elevatorId, Floor floor) {
        String status;
        int fromFloor;

        for (Request request : requestQueue) {
            status = request.getStatus();
            fromFloor = request.getFromFloor();
            if ((status.equals("") && floor.isReachable(fromFloor))
                    || status.equals(elevatorId)) {
                return false;
            }
        }

        return true;
    }

    public synchronized void putRequest(Request request) {
        int personId = request.getPersonId();
        int fromFloor = request.getFromFloor();
        int toFloor = request.getToFloor();

        for (int i = 0; i < elevatorNum; i++) {
            if (reachableFloor[i].isReachable(fromFloor)
                    && reachableFloor[i].isReachable(toFloor)) {
                requestQueue.add(request);
                if (!requestQueue.isEmpty()) {
                    notifyAll();
                }
                return;
            }
        }

        int low = Math.min(fromFloor, toFloor);
        int high = Math.max(fromFloor, toFloor);
        int hub;
        if (Math.abs(low - 1) <= Math.abs(high - 15)) {
            hub = 1;
        } else {
            hub = 15;
        }
        Request request1 = new Request(fromFloor, hub, personId);
        Request request2 = new Request(hub, toFloor, personId);
        requestHub.put(request1, request2);
        putRequest(request1);
    }

    public synchronized ArrayList<Request> getOutRequests(String elevatorId,
                                                          int currentFloor) {
        ArrayList<Request> outRequests = new ArrayList<>();
        Request request;
        String status;
        int toFloor;

        for (int i = 0; i < requestQueue.size(); i++) {
            request = requestQueue.get(i);
            status = request.getStatus();
            toFloor = request.getToFloor();

            if (status.equals(elevatorId) && toFloor == currentFloor) {
                outRequests.add(requestQueue.remove(i));
                i--;
            }
        }

        return outRequests;
    }

    public synchronized void putSecondRequest(Request request) {
        if (requestHub.containsKey(request)) {
            putRequest(requestHub.remove(request));
        }
    }

    public synchronized ArrayList<Request> getInRequests(String elevatorId,
                                                         Floor floor,
                                                         int currentFloor,
                                                         int currentDirection,
                                                         int personNum,
                                                         int capacity) {
        ArrayList<Request> inRequests = new ArrayList<>();
        String status;
        int currentPersonNum = personNum;
        int fromFloor;
        int toFloor;
        int direction;

        for (Request request : requestQueue) {
            status = request.getStatus();
            fromFloor = request.getFromFloor();
            toFloor = request.getToFloor();
            if (toFloor > fromFloor) {
                direction = 1;
            } else {
                direction = -1;
            }

            if (status.equals("") && direction == currentDirection
                    && currentPersonNum < capacity && fromFloor == currentFloor
                    && floor.isReachable(toFloor)) {
                request.setStatus(elevatorId);
                inRequests.add(request);
                currentPersonNum++;
            }
        }

        return inRequests;
    }

    public synchronized boolean isInRightDirection(Floor floor,
                                                   int currentFloor,
                                                   int currentDirection) {
        String status;
        int fromFloor;
        int toFloor;
        int direction;

        if ((currentFloor == floor.getLowestFloor() && currentDirection == -1)
                || (currentFloor == floor.getHighestFloor()
                && currentDirection == 1)) {
            return false;
        }

        /* If there are requests in current direction, go ahead. */
        for (Request request : requestQueue) {
            status = request.getStatus();
            fromFloor = request.getFromFloor();
            if (status.equals("") && ((fromFloor > currentFloor
                    && currentDirection == 1) || (fromFloor < currentFloor
                    && currentDirection == -1))) {
                return true;
            }
        }

        /* If there is a request at current floor, whose direction is the same
         * as the current direction, then go head. */
        for (Request request : requestQueue) {
            status = request.getStatus();
            fromFloor = request.getFromFloor();
            toFloor = request.getToFloor();
            if (toFloor > fromFloor) {
                direction = 1;
            } else {
                direction = -1;
            }
            if (status.equals("") && fromFloor == currentFloor
                    && direction == currentDirection) {
                return true;
            }
        }

        /* According to the LOOK Algorithm, if there are no request in current
         * direction or at current floor, then turned the current direction. */
        return false;
    }

    public void setFinished() {
        this.finished = true;
    }
}

