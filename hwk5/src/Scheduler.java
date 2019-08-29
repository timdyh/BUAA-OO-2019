import java.util.ArrayList;
import com.oocourse.elevator1.PersonRequest;

public class Scheduler {
    private static ArrayList<PersonRequest> requestList = new ArrayList<>();
    private static boolean finished = false;

    public void setFinished() {
        finished = true;
    }

    public synchronized void putRequest(PersonRequest request) {
        requestList.add(request);
        notifyAll();
    }

    public synchronized PersonRequest getRequest() {
        if (finished && requestList.isEmpty()) {
            return null;
        }

        while (requestList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return requestList.remove(0);
    }
}
