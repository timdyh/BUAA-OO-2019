public class Floor {
    private final int[] reachableFloors;
    private final int size;
    private final int lowestFloor;
    private final int highestFloor;

    public Floor(int[] reachableFloors, int size) {
        this.reachableFloors = reachableFloors;
        this.size = size;
        this.lowestFloor = reachableFloors[0];
        this.highestFloor = reachableFloors[size - 1];
    }

    public boolean isReachable(int floor) {
        for (int i = 0; i < size; i++) {
            if (floor == reachableFloors[i]) {
                return true;
            }
        }
        return false;
    }

    public int getLowestFloor() {
        return lowestFloor;
    }

    public int getHighestFloor() {
        return highestFloor;
    }
}
