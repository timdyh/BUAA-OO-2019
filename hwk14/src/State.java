import com.oocourse.uml2.models.common.ElementType;

import java.util.ArrayList;

public class State {
    private final String stateName;
    private final String stateId;
    private final String regionId;
    private final ElementType type;
    private ArrayList<State> nextState = new ArrayList<>();

    public State(String stateName, String stateId, String regionId,
                 ElementType type) {
        this.stateName = stateName;
        this.stateId = stateId;
        this.regionId = regionId;
        this.type = type;
    }

    public String getStateName() {
        return stateName;
    }

    public String getStateId() {
        return stateId;
    }

    public String getRegionId() {
        return regionId;
    }

    public ElementType getType() {
        return type;
    }

    public void addNextState(State state) {
        nextState.add(state);
    }

    public ArrayList<State> getNextState() {
        return nextState;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof State
                && ((State) obj).stateId.equals(this.stateId));
    }

    @Override
    public int hashCode() {
        return stateId.hashCode();
    }
}