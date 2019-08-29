import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class RequestProducer extends Thread {
    private static ElevatorInput input = new ElevatorInput(System.in);

    private Scheduler scheduler;

    public RequestProducer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        PersonRequest request;

        while ((request = input.nextPersonRequest()) != null) {
            scheduler.putRequest(request);
        }
        scheduler.setFinished();

        try {
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}