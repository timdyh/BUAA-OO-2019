import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.models.common.ElementType;

import static com.oocourse.uml2.models.common.ElementType.UML_FINAL_STATE;
import static com.oocourse.uml2.models.common.ElementType.UML_PSEUDOSTATE;

import java.util.HashMap;

public class StateMachine {
    private final String stateMachineName;
    private final String stateMachineId;
    private HashMap<String, State> name2State = new HashMap<>();
    private boolean start = false;
    private boolean end = false;
    private int ordinaryStateNum = 0;
    private int transitionNum = 0;

    public StateMachine(String stateMachineName, String stateMachineId) {
        this.stateMachineName = stateMachineName;
        this.stateMachineId = stateMachineId;
    }

    public String getStateMachineName() {
        return stateMachineName;
    }

    public String getStateMachineId() {
        return stateMachineId;
    }

    public void addState(State s) {
        String stateName = s.getStateName();
        ElementType type = s.getType();
        if (type.equals(UML_PSEUDOSTATE)) {
            start = true;
        } else if (type.equals(UML_FINAL_STATE)) {
            end = true;
        } else {
            if (name2State.containsKey(stateName)) {
                name2State.put(stateName, null);
            } else {
                name2State.put(stateName, s);
            }
            ordinaryStateNum++;
        }

        transitionNum += s.getNextState().size();
    }

    public int getStateNum() {
        int stateNum = ordinaryStateNum;
        if (start) {
            stateNum++;
        }
        if (end) {
            stateNum++;
        }
        return stateNum;
    }

    public int getTransitionNum() {
        return transitionNum;
    }

    public State getState(String stateName) throws
            StateNotFoundException, StateDuplicatedException {
        if (!name2State.containsKey(stateName)) {
            throw new StateNotFoundException(stateMachineName, stateName);
        }
        State s;
        if ((s = name2State.get(stateName)) == null) {
            throw new StateDuplicatedException(stateMachineName, stateName);
        }
        return s;
    }
}