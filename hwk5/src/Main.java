import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        Scheduler scheduler = new Scheduler();
        RequestProducer producer = new RequestProducer(scheduler);
        Elevator elevator = new Elevator(scheduler);
        producer.start();
        elevator.start();
    }
}