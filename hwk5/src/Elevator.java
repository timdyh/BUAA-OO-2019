import com.oocourse.elevator1.PersonRequest;
import com.oocourse.TimableOutput;

public class Elevator extends Thread {
    private static final int transportTimePerFloor = 500;
    private static final int doorOpenCloseTime = 250;

    private Scheduler scheduler;

    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        PersonRequest request;
        int fromFloor;
        int toFloor;
        int personId;
        int currentFloor = 1;

        while (true) {
            request = scheduler.getRequest();
            if (request == null) {
                break;
            }

            fromFloor = request.getFromFloor();
            toFloor = request.getToFloor();
            personId = request.getPersonId();

            move(currentFloor, fromFloor);
            TimableOutput.println("OPEN-" + fromFloor);
            open();
            TimableOutput.println("IN-" + personId + "-" + fromFloor);
            close();
            TimableOutput.println("CLOSE-" + fromFloor);
            move(fromFloor, toFloor);
            TimableOutput.println("OPEN-" + toFloor);
            open();
            TimableOutput.println("OUT-" + personId + "-" + toFloor);
            close();
            TimableOutput.println("CLOSE-" + toFloor);

            currentFloor = toFloor;
        }
    }

    private static void move(int start, int end) {
        try {
            Thread.sleep(transportTimePerFloor
                    * Math.abs(end - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void open() {
        try {
            Thread.sleep(doorOpenCloseTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void close() {
        try {
            Thread.sleep(doorOpenCloseTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}