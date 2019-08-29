import java.util.ArrayList;

public class Interface {
    private final String interName;
    private final String interId;
    private ArrayList<Interface> superInter = new ArrayList<>();

    public Interface(String interName, String interId) {
        this.interName = interName;
        this.interId = interId;
    }

    public String getInterName() {
        return interName;
    }

    public String getInterId() {
        return interId;
    }

    public void addSuperInter(Interface superInter) {
        this.superInter.add(superInter);
    }

    public ArrayList<Interface> getSuperInter() {
        return superInter;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Interface
                && ((Interface) obj).interId.equals(this.interId));
    }

    @Override
    public int hashCode() {
        return interId.hashCode();
    }
}