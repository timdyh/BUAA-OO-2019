import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;

import java.util.HashMap;

public class Interaction {
    private String interactionName;
    private String interactionId;
    private HashMap<String, Integer> name2IncomingNum = new HashMap<>();
    private int lifelineNum = 0;
    private int messageNum = 0;

    public Interaction(String interactionName, String interactionId) {
        this.interactionName = interactionName;
        this.interactionId = interactionId;
    }

    public String getInteractionName() {
        return interactionName;
    }

    public String getInteractionId() {
        return interactionId;
    }

    public void addLifeline(String lifelineName, int incomingNum) {
        if (name2IncomingNum.containsKey(lifelineName)) {
            name2IncomingNum.put(lifelineName, null);
        } else {
            name2IncomingNum.put(lifelineName, incomingNum);
        }
        lifelineNum++;
        messageNum += incomingNum;
    }

    public void addEndpoint(int incomingNum) {
        messageNum += incomingNum;
    }

    public int getLifelineNum() {
        return lifelineNum;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public int getIncomingNum(String lifelineName)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        if (!name2IncomingNum.containsKey(lifelineName)) {
            throw new LifelineNotFoundException(interactionName, lifelineName);
        }
        Integer incomingNum;
        if ((incomingNum = name2IncomingNum.get(lifelineName)) == null) {
            throw new LifelineDuplicatedException(
                    interactionName, lifelineName);
        }
        return incomingNum;
    }
}
