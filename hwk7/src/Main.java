import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        int[] reachableFloorA = {-3, -2, -1, 1, 15, 16, 17, 18, 19, 20};
        int[] reachableFloorB = {-2, -1, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                                 13, 14, 15};
        int[] reachableFloorC = {1, 3, 5, 7, 9, 11, 13, 15};
        Floor floorA = new Floor(reachableFloorA, reachableFloorA.length);
        Floor floorB = new Floor(reachableFloorB, reachableFloorB.length);
        Floor floorC = new Floor(reachableFloorC, reachableFloorC.length);
        Floor[] reachableFloor = {floorA, floorB, floorC};

        Scheduler scheduler = new Scheduler(reachableFloor, 3);

        Client client = new Client(scheduler);
        client.start();

        Elevator elevatorA = new Elevator("A", 6, 400, 200, floorA, scheduler);
        Elevator elevatorB = new Elevator("B", 8, 500, 200, floorB, scheduler);
        Elevator elevatorC = new Elevator("C", 7, 600, 200, floorC, scheduler);

        elevatorA.start();
        elevatorB.start();
        elevatorC.start();
    }
}