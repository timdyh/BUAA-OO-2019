import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.format.UmlStateChartInteraction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.oocourse.uml2.models.common.ElementType.UML_FINAL_STATE;
import static com.oocourse.uml2.models.common.ElementType.UML_PSEUDOSTATE;

public class MyUmlStateChartInteraction implements UmlStateChartInteraction {
    private HashMap<String, UmlRegion> eleRegion = new HashMap<>();
    private HashMap<String, UmlTransition> eleTransition = new HashMap<>();

    private HashMap<String, StateMachine> id2Machine = new HashMap<>();
    private HashMap<String, StateMachine> name2Machine = new HashMap<>();
    private HashMap<String, StateMachine> regionId2Machine = new HashMap<>();
    private HashMap<String, State> id2State = new HashMap<>();

    public MyUmlStateChartInteraction(UmlElement[] elements) {
        for (UmlElement e : elements) {
            classify(e);
        }
        buildTransition();
        buildRegion();
        buildState();
    }

    private void classify(UmlElement e) {
        String name = e.getName();
        String id = e.getId();
        switch (e.getElementType()) {
            case UML_STATE_MACHINE:
                StateMachine sm = new StateMachine(name, id);
                id2Machine.put(id, sm);
                if (name2Machine.containsKey(name)) {
                    name2Machine.put(name, null);
                } else {
                    name2Machine.put(name, sm);
                }
                break;
            case UML_REGION:
                eleRegion.put(id, (UmlRegion) e);
                break;
            case UML_STATE:
            case UML_PSEUDOSTATE:
            case UML_FINAL_STATE:
                State s = new State(
                        name, id, e.getParentId(), e.getElementType());
                id2State.put(id, s);
                break;
            case UML_TRANSITION:
                eleTransition.put(id, (UmlTransition) e);
                break;
            default:
                break;
        }
    }

    private void buildTransition() {
        for (Map.Entry<String, UmlTransition> entry
                : eleTransition.entrySet()) {
            UmlTransition ut = entry.getValue();
            State fromState = id2State.get(ut.getSource());
            State toState = id2State.get(ut.getTarget());
            fromState.addNextState(toState);
        }
    }

    private void buildRegion() {
        for (Map.Entry<String, UmlRegion> entry : eleRegion.entrySet()) {
            UmlRegion ur = entry.getValue();
            StateMachine sm = id2Machine.get(ur.getParentId());
            regionId2Machine.put(ur.getId(), sm);
        }
    }

    private void buildState() {
        for (Map.Entry<String, State> entry : id2State.entrySet()) {
            State s = entry.getValue();
            StateMachine sm = regionId2Machine.get(s.getRegionId());
            sm.addState(s);
        }
    }

    private StateMachine checkStateMachineException(String stateMachineName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        if (!name2Machine.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        StateMachine sm;
        if ((sm = name2Machine.get(stateMachineName)) == null) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return sm;
    }

    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        StateMachine sm = checkStateMachineException(stateMachineName);
        return sm.getStateNum();
    }

    public int getTransitionCount(String stateMachineName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        StateMachine sm = checkStateMachineException(stateMachineName);
        return sm.getTransitionNum();
    }

    public int getSubsequentStateCount(
            String stateMachineName, String stateName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        StateMachine sm = checkStateMachineException(stateMachineName);
        State s = sm.getState(stateName);
        int cnt = 0;
        boolean start = false;
        boolean end = false;
        HashMap<State, Integer> visited = new HashMap<>();
        LinkedList<State> queue = new LinkedList<>();
        queue.offer(s);
        while (!queue.isEmpty()) {
            ArrayList<State> nextState = queue.poll().getNextState();
            for (State ns : nextState) {
                if (!visited.containsKey(ns)) {
                    ElementType type = ns.getType();
                    if (type.equals(UML_PSEUDOSTATE)) {
                        start = true;
                    } else if (type.equals(UML_FINAL_STATE)) {
                        end = true;
                    } else {
                        cnt++;
                    }
                    visited.put(ns, 1);
                    queue.offer(ns);
                }
            }
        }
        if (start) {
            cnt++;
        }
        if (end) {
            cnt++;
        }

        return cnt;
    }
}
