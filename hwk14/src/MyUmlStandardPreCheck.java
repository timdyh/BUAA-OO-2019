import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.interact.format.UmlStandardPreCheck;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlClassOrInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.LinkedList;

public class MyUmlStandardPreCheck implements UmlStandardPreCheck {
    private MyUmlClassModelInteraction myUmlClassModelInteraction;
    private HashSet<AttributeClassInformation> duplicateName = new HashSet<>();
    private HashSet<UmlClassOrInterface> circle = new HashSet<>();
    private HashMap<Interface, Integer> dfsVisited = new HashMap<>();
    private HashSet<UmlClassOrInterface> duplicateInheritance = new HashSet<>();

    public MyUmlStandardPreCheck(
            MyUmlClassModelInteraction myUmlClassModelInteraction) {
        this.myUmlClassModelInteraction = myUmlClassModelInteraction;
    }

    public void checkForUml002() throws UmlRule002Exception {
        HashMap<String, Class> allClass =
                myUmlClassModelInteraction.getAllClass();
        for (Map.Entry<String, Class> entry : allClass.entrySet()) {
            HashSet<String> attrAndAssoEnd = new HashSet<>();
            Class c = entry.getValue();
            HashMap<String, UmlAttribute> attr = c.getAttr();
            for (Map.Entry<String, UmlAttribute> entry1 : attr.entrySet()) {
                String name = entry1.getKey();
                if (name == null) {
                    continue;
                }
                UmlAttribute ua = entry1.getValue();
                if (ua == null || attrAndAssoEnd.contains(name)) {
                    duplicateName.add(new AttributeClassInformation(
                            name, c.getClassName()));
                } else {
                    attrAndAssoEnd.add(name);
                }
            }
            HashMap<String, UmlAssociationEnd> assoEnd = c.getAssoEnd();
            for (Map.Entry<String, UmlAssociationEnd> entry2
                    : assoEnd.entrySet()) {
                String name = entry2.getKey();
                if (name == null) {
                    continue;
                }
                UmlAssociationEnd uae = entry2.getValue();
                if (uae == null || attrAndAssoEnd.contains(name)) {
                    duplicateName.add(new AttributeClassInformation(
                            name, c.getClassName()));
                } else {
                    attrAndAssoEnd.add(name);
                }
            }
        }

        if (!duplicateName.isEmpty()) {
            throw new UmlRule002Exception(duplicateName);
        }
    }

    private void dfs(Interface inter, Interface start) {
        dfsVisited.put(inter, 1);
        ArrayList<Interface> superInter = inter.getSuperInter();
        for (Interface si : superInter) {
            if (!dfsVisited.containsKey(si)) {
                dfs(si, start);
            } else if (si.equals(start)) {
                UmlInterface ui = myUmlClassModelInteraction.getUmlInterface(
                        start.getInterId());
                circle.add(ui);
                return;
            }
        }
    }

    public void checkForUml008() throws UmlRule008Exception {
        HashMap<String, Class> allClass =
                myUmlClassModelInteraction.getAllClass();
        for (Map.Entry<String, Class> entry : allClass.entrySet()) {
            HashMap<Class, Integer> visited = new HashMap<>();
            Class start = entry.getValue();
            Class c = start.getSuperClass();
            while (c != null && !visited.containsKey(c)) {
                visited.put(c, 1);
                if (c.equals(start)) {
                    UmlClass uc = myUmlClassModelInteraction.getUmlClass(
                            start.getClassId());
                    circle.add(uc);
                    break;
                }
                c = c.getSuperClass();
            }
        }

        HashMap<String, Interface> allInter =
                myUmlClassModelInteraction.getAllInter();
        for (Map.Entry<String, Interface> entry : allInter.entrySet()) {
            dfsVisited.clear();
            Interface start = entry.getValue();
            dfs(start, start);
        }

        if (!circle.isEmpty()) {
            throw new UmlRule008Exception(circle);
        }
    }

    private void checkClass() {
        HashMap<String, Class> allClass =
                myUmlClassModelInteraction.getAllClass();
        for (Map.Entry<String, Class> entry : allClass.entrySet()) {
            HashSet<Interface> interSet = new HashSet<>();
            Class c = entry.getValue();
            UmlClass uc =
                    myUmlClassModelInteraction.getUmlClass(c.getClassId());
            label1:
            while (c != null) {
                LinkedList<Interface> queue = new LinkedList<>();
                ArrayList<Interface> realInter = c.getRealInter();
                for (Interface inter : realInter) {
                    queue.offer(inter);
                    if (interSet.contains(inter)) {
                        duplicateInheritance.add(uc);
                        break label1;
                    }
                    interSet.add(inter);
                }
                while (!queue.isEmpty()) {
                    ArrayList<Interface> superInter =
                            queue.poll().getSuperInter();
                    for (Interface inter : superInter) {
                        if (interSet.contains(inter)) {
                            duplicateInheritance.add(uc);
                            break label1;
                        }
                        interSet.add(inter);
                        queue.offer(inter);
                    }
                }
                c = c.getSuperClass();
            }
        }
    }

    private void checkInterface() {
        HashMap<String, Interface> allInter =
                myUmlClassModelInteraction.getAllInter();
        for (Map.Entry<String, Interface> entry : allInter.entrySet()) {
            HashSet<Interface> interSet = new HashSet<>();
            Interface inter = entry.getValue();
            UmlInterface ui = myUmlClassModelInteraction.getUmlInterface(
                    inter.getInterId());
            LinkedList<Interface> queue = new LinkedList<>();
            interSet.add(inter);
            queue.offer(inter);
            label2:
            while (!queue.isEmpty()) {
                ArrayList<Interface> superInter = queue.poll().getSuperInter();
                for (Interface si : superInter) {
                    if (interSet.contains(si)) {
                        duplicateInheritance.add(ui);
                        break label2;
                    }
                    interSet.add(si);
                    queue.offer(si);
                }
            }
        }
    }

    public void checkForUml009() throws UmlRule009Exception {
        checkClass();
        checkInterface();

        if (!duplicateInheritance.isEmpty()) {
            throw new UmlRule009Exception(duplicateInheritance);
        }
    }
}