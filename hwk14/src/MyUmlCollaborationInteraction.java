import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.format.UmlCollaborationInteraction;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.HashMap;
import java.util.Map;

public class MyUmlCollaborationInteraction
        implements UmlCollaborationInteraction {
    private HashMap<String, UmlLifeline> eleLifeline = new HashMap<>();
    private HashMap<String, UmlMessage> eleMessage = new HashMap<>();
    private HashMap<String, UmlEndpoint> eleEndpoint = new HashMap<>();

    private HashMap<String, Interaction> id2Interaction = new HashMap<>();
    private HashMap<String, Interaction> name2Interaction = new HashMap<>();
    private HashMap<String, Integer> id2IncomingNum = new HashMap<>();

    public MyUmlCollaborationInteraction(UmlElement[] elements) {
        for (UmlElement e : elements) {
            classify(e);
        }
        buildMessage();
        buildLifeline();
        buildEndpoint();
    }

    private void classify(UmlElement e) {
        String name = e.getName();
        String id = e.getId();
        switch (e.getElementType()) {
            case UML_INTERACTION:
                Interaction inter = new Interaction(name, id);
                id2Interaction.put(id, inter);
                if (name2Interaction.containsKey(name)) {
                    name2Interaction.put(name, null);
                } else {
                    name2Interaction.put(name, inter);
                }
                break;
            case UML_LIFELINE:
                eleLifeline.put(id, (UmlLifeline) e);
                break;
            case UML_MESSAGE:
                eleMessage.put(id, (UmlMessage) e);
                break;
            default:
                eleEndpoint.put(id, (UmlEndpoint) e);
                break;
        }
    }

    private void buildMessage() {
        for (Map.Entry<String, UmlMessage> entry : eleMessage.entrySet()) {
            UmlMessage um = entry.getValue();
            String targetLifelineId = um.getTarget();
            if (id2IncomingNum.containsKey(targetLifelineId)) {
                id2IncomingNum.put(targetLifelineId,
                        id2IncomingNum.get(targetLifelineId) + 1);
            } else {
                id2IncomingNum.put(targetLifelineId, 1);
            }
        }
    }

    private void buildLifeline() {
        for (Map.Entry<String, UmlLifeline> entry : eleLifeline.entrySet()) {
            UmlLifeline ul = entry.getValue();
            String lifelineName = ul.getName();
            String lifelineId = ul.getId();
            Integer incomingNum;
            if ((incomingNum = id2IncomingNum.get(lifelineId)) == null) {
                incomingNum = 0;
            }
            Interaction inter = id2Interaction.get(ul.getParentId());
            inter.addLifeline(lifelineName, incomingNum);
        }
    }

    private void buildEndpoint() {
        for (Map.Entry<String, UmlEndpoint> entry : eleEndpoint.entrySet()) {
            UmlEndpoint ue = entry.getValue();
            String endpointId = ue.getId();
            Integer incomingNum;
            if ((incomingNum = id2IncomingNum.get(endpointId)) == null) {
                incomingNum = 0;
            }
            Interaction inter = id2Interaction.get(ue.getParentId());
            inter.addEndpoint(incomingNum);
        }
    }

    private Interaction checkInteractionException(String interactionName)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        if (!name2Interaction.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        Interaction inter;
        if ((inter = name2Interaction.get(interactionName)) == null) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return inter;
    }

    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        Interaction inter = checkInteractionException(interactionName);
        return inter.getLifelineNum();
    }

    public int getMessageCount(String interactionName)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        Interaction inter = checkInteractionException(interactionName);
        return inter.getMessageNum();
    }

    public int getIncomingMessageCount(
            String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        Interaction inter = checkInteractionException(interactionName);
        return inter.getIncomingNum(lifelineName);
    }
}
