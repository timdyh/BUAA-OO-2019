import com.oocourse.elevator3.PersonRequest;

public class Request extends PersonRequest {
    private String status = "";

    public Request(int fromFloor, int toFloor, int personId) {
        super(fromFloor, toFloor, personId);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

