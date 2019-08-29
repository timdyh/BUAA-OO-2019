import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private ArrayList<UmlElement> eleClassModel = new ArrayList<>();
    private ArrayList<UmlElement> eleStateChart = new ArrayList<>();
    private ArrayList<UmlElement> eleCollaboration = new ArrayList<>();

    private MyUmlClassModelInteraction myUmlClassModelInteraction;
    private MyUmlStateChartInteraction myUmlStateChartInteraction;
    private MyUmlCollaborationInteraction myUmlCollaborationInteraction;
    private MyUmlStandardPreCheck myUmlStandardPreCheck;

    public MyUmlGeneralInteraction(UmlElement... elements) {
        for (UmlElement e : elements) {
            classify(e);
        }
        myUmlClassModelInteraction = new MyUmlClassModelInteraction(
                eleClassModel.toArray(new UmlElement[0]));
        myUmlStateChartInteraction = new MyUmlStateChartInteraction(
                eleStateChart.toArray(new UmlElement[0]));
        myUmlCollaborationInteraction = new MyUmlCollaborationInteraction(
                eleCollaboration.toArray(new UmlElement[0]));
        myUmlStandardPreCheck = new MyUmlStandardPreCheck(
                myUmlClassModelInteraction);
    }

    private void classify(UmlElement e) {
        switch (e.getElementType()) {
            case UML_CLASS:
            case UML_ASSOCIATION:
            case UML_ASSOCIATION_END:
            case UML_ATTRIBUTE:
            case UML_OPERATION:
            case UML_PARAMETER:
            case UML_GENERALIZATION:
            case UML_INTERFACE:
            case UML_INTERFACE_REALIZATION:
                eleClassModel.add(e);
                break;
            case UML_STATE_MACHINE:
            case UML_REGION:
            case UML_STATE:
            case UML_PSEUDOSTATE:
            case UML_FINAL_STATE:
            case UML_TRANSITION:
            case UML_EVENT:
            case UML_OPAQUE_BEHAVIOR:
                eleStateChart.add(e);
                break;
            default:
                eleCollaboration.add(e);
                break;
        }
    }

    public void checkForUml002() throws UmlRule002Exception {
        myUmlStandardPreCheck.checkForUml002();
    }

    public void checkForUml008() throws UmlRule008Exception {
        myUmlStandardPreCheck.checkForUml008();
    }

    public void checkForUml009() throws UmlRule009Exception {
        myUmlStandardPreCheck.checkForUml009();
    }

    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        return myUmlStateChartInteraction.getStateCount(stateMachineName);
    }

    public int getTransitionCount(String stateMachineName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        return myUmlStateChartInteraction.getTransitionCount(stateMachineName);
    }

    public int getSubsequentStateCount(
            String stateMachineName, String stateName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return myUmlStateChartInteraction.getSubsequentStateCount(
                stateMachineName, stateName);
    }

    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        return myUmlCollaborationInteraction.getParticipantCount(
                interactionName);
    }

    public int getMessageCount(String interactionName)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        return myUmlCollaborationInteraction.getMessageCount(interactionName);
    }

    public int getIncomingMessageCount(
            String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return myUmlCollaborationInteraction.getIncomingMessageCount(
                interactionName, lifelineName);
    }

    public int getClassCount() {
        return myUmlClassModelInteraction.getClassCount();
    }

    public int getClassOperationCount(
            String className, OperationQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getClassOperationCount(
                className, queryType);
    }

    public int getClassAttributeCount(
            String className, AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getClassAttributeCount(
                className, queryType);
    }

    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getClassAssociationCount(className);
    }

    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getClassAssociatedClassList(
                className);
    }

    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getClassOperationVisibility(
                className, operationName);
    }

    public Visibility getClassAttributeVisibility(
            String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        return myUmlClassModelInteraction.getClassAttributeVisibility(
                className, attributeName);
    }

    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getTopParentClass(className);
    }

    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getImplementInterfaceList(className);
    }

    public List<AttributeClassInformation> getInformationNotHidden(
            String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getInformationNotHidden(className);
    }
}