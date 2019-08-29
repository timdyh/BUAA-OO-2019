import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;

public class Client extends Thread {
    private final ElevatorInput input = new ElevatorInput(System.in);
    private final Scheduler scheduler;

    public Client(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        PersonRequest personRequest;
        Request request;
        while ((personRequest = input.nextPersonRequest()) != null) {
            request = new Request(personRequest.getFromFloor(),
                                  personRequest.getToFloor(),
                                  personRequest.getPersonId());
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
