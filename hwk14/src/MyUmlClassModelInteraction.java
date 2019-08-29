import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.format.UmlClassModelInteraction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class MyUmlClassModelInteraction implements UmlClassModelInteraction {
    private HashMap<String, UmlClass> eleClass = new HashMap<>();
    private HashMap<String, UmlInterface> eleInter = new HashMap<>();
    private HashMap<String, UmlAssociation> eleAsso = new HashMap<>();
    private HashMap<String, UmlAssociationEnd> eleAssoEnd = new HashMap<>();
    private HashMap<String, UmlAttribute> eleAttr = new HashMap<>();
    private HashMap<String, UmlOperation> eleOper = new HashMap<>();
    private HashMap<String, UmlParameter> eleParam = new HashMap<>();
    private HashMap<String, UmlGeneralization> eleGener = new HashMap<>();
    private HashMap<String, UmlInterfaceRealization> eleInterReal
            = new HashMap<>();

    private HashMap<String, Class> classId2Class = new HashMap<>();
    private HashMap<String, Class> className2Class = new HashMap<>();
    private HashMap<String, Interface> interId2Interface = new HashMap<>();

    public MyUmlClassModelInteraction(UmlElement[] elements) {
        for (UmlElement e : elements) {
            classify(e);
        }
        buildGeneralization();
        buildInterfaceRealization();
        buildAssociation();
        buildAttribute();
        buildOperation();
        buildParameter();
    }

    private void classify(UmlElement e) {
        switch (e.getElementType()) {
            case UML_CLASS:
                eleClass.put(e.getId(), (UmlClass) e);
                Class c = new Class(e.getName(), e.getId());
                classId2Class.put(e.getId(), c);
                if (className2Class.containsKey(e.getName())) {
                    className2Class.put(e.getName(), null);
                } else {
                    className2Class.put(e.getName(), c);
                }
                break;
            case UML_ASSOCIATION:
                eleAsso.put(e.getId(), (UmlAssociation) e);
                break;
            case UML_ASSOCIATION_END:
                eleAssoEnd.put(e.getId(), (UmlAssociationEnd) e);
                break;
            case UML_ATTRIBUTE:
                eleAttr.put(e.getId(), (UmlAttribute) e);
                break;
            case UML_OPERATION:
                eleOper.put(e.getId(), (UmlOperation) e);
                break;
            case UML_PARAMETER:
                eleParam.put(e.getId(), (UmlParameter) e);
                break;
            case UML_GENERALIZATION:
                eleGener.put(e.getId(), (UmlGeneralization) e);
                break;
            case UML_INTERFACE:
                eleInter.put(e.getId(), (UmlInterface) e);
                Interface inter = new Interface(e.getName(), e.getId());
                interId2Interface.put(e.getId(), inter);
                break;
            default:
                eleInterReal.put(e.getId(), (UmlInterfaceRealization) e);
                break;
        }
    }

    private void buildGeneralization() {
        for (Map.Entry<String, UmlGeneralization> entry : eleGener.entrySet()) {
            UmlGeneralization ug = entry.getValue();
            String subId = ug.getSource();
            String superId = ug.getTarget();
            if (classId2Class.containsKey(subId)) {
                Class subClass = classId2Class.get(subId);
                Class superClass = classId2Class.get(superId);
                subClass.setSuperClass(superClass);
            } else {
                Interface subInter = interId2Interface.get(subId);
                Interface superInter = interId2Interface.get(superId);
                subInter.addSuperInter(superInter);
            }
        }
    }

    private void buildInterfaceRealization() {
        for (Map.Entry<String, UmlInterfaceRealization> entry
                : eleInterReal.entrySet()) {
            UmlInterfaceRealization uir = entry.getValue();
            String classId = uir.getSource();
            String interId = uir.getTarget();
            Class c = classId2Class.get(classId);
            Interface inter = interId2Interface.get(interId);
            c.addRealInter(inter);
        }
    }

    private void buildAssociation() {
        for (Map.Entry<String, UmlAssociation> entry : eleAsso.entrySet()) {
            UmlAssociation uas = entry.getValue();
            String end1Id = uas.getEnd1();
            String end2Id = uas.getEnd2();
            UmlAssociationEnd end1 = eleAssoEnd.get(end1Id);
            UmlAssociationEnd end2 = eleAssoEnd.get(end2Id);
            String ref1 = end1.getReference();
            String ref2 = end2.getReference();
            Class c1;
            Class c2;
            Interface inter;
            if (classId2Class.containsKey(ref1)) {
                c1 = classId2Class.get(ref1);
                if (classId2Class.containsKey(ref2)) {
                    c2 = classId2Class.get(ref2);
                    c1.addAsso(c2);
                    c2.addAsso(c1);
                    c1.addAssoEnd(end2);
                    c2.addAssoEnd(end1);
                } else {
                    inter = interId2Interface.get(ref2);
                    c1.addAsso(inter);
                    c1.addAssoEnd(end2);
                }
            } else {
                inter = interId2Interface.get(ref1);
                if (classId2Class.containsKey(ref2)) {
                    c1 = classId2Class.get(ref2);
                    c1.addAsso(inter);
                    c1.addAssoEnd(end1);
                }
            }
        }
    }

    private void buildAttribute() {
        for (Map.Entry<String, UmlAttribute> entry : eleAttr.entrySet()) {
            UmlAttribute ua = entry.getValue();
            String classId = ua.getParentId();
            Class c = classId2Class.get(classId);
            if (c != null) {
                c.addAttr(ua);
            }
        }
    }

    private void buildOperation() {
        for (Map.Entry<String, UmlOperation> entry : eleOper.entrySet()) {
            UmlOperation uo = entry.getValue();
            String classId = uo.getParentId();
            Class c = classId2Class.get(classId);
            if (c != null) {
                c.addOper(uo);
            }
        }
    }

    private void buildParameter() {
        for (Map.Entry<String, UmlParameter> entry : eleParam.entrySet()) {
            UmlParameter up = entry.getValue();
            UmlOperation uo = eleOper.get(up.getParentId());
            Class c = classId2Class.get(uo.getParentId());
            if (c != null) {
                c.updateOperInfo(up);
            }
        }
    }

    private Class checkClassException(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!className2Class.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        Class c;
        if ((c = className2Class.get(className)) == null) {
            throw new ClassDuplicatedException(className);
        }
        return c;
    }

    public int getClassCount() {
        return classId2Class.size();
    }

    public int getClassOperationCount(String className,
                                      OperationQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        Class c = checkClassException(className);
        switch (queryType) {
            case NON_RETURN:
                return c.getNonReturnOperNum();
            case RETURN:
                return c.getReturnOperNum();
            case NON_PARAM:
                return c.getNonParamOperNum();
            case PARAM:
                return c.getParamOperNum();
            default:
                return c.getAllOperNum();
        }
    }

    public int getClassAttributeCount(String className,
                                      AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        Class c = checkClassException(className);
        int self = c.getAttrNum();
        if (queryType.equals(AttributeQueryType.SELF_ONLY)) {
            return self;
        }
        int all = 0;
        while (c != null) {
            all += c.getAttrNum();
            c = c.getSuperClass();
        }
        return all;
    }

    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        Class c = checkClassException(className);
        int cnt = 0;
        while (c != null) {
            cnt += c.getAssoNum();
            c = c.getSuperClass();
        }
        return cnt;
    }

    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        Class c = checkClassException(className);
        HashSet<Class> set = new HashSet<>();
        while (c != null) {
            ArrayList<Class> assoClass = c.getAssoClass();
            set.addAll(assoClass);
            c = c.getSuperClass();
        }
        ArrayList<String> list = new ArrayList<>();
        for (Class cc : set) {
            list.add(cc.getClassName());
        }
        return list;
    }

    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        Class c = checkClassException(className);
        HashMap<Visibility, Integer> map = new HashMap<>();
        map.put(Visibility.PUBLIC, 0);
        map.put(Visibility.PROTECTED, 0);
        map.put(Visibility.PRIVATE, 0);
        map.put(Visibility.PACKAGE, 0);
        ArrayList<UmlOperation> oper = c.getOperByName(operationName);
        for (UmlOperation uo : oper) {
            Visibility v = uo.getVisibility();
            map.put(v, map.get(v) + 1);
        }
        return map;
    }

    public Visibility getClassAttributeVisibility(String className,
                                                  String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        Class c = checkClassException(className);
        Visibility v = null;
        while (c != null) {
            HashMap<String, UmlAttribute> attr = c.getAttr();
            if (!attr.containsKey(attributeName)) {
                c = c.getSuperClass();
                continue;
            }
            UmlAttribute ua;
            if ((ua = attr.get(attributeName)) == null || v != null) {
                throw new AttributeDuplicatedException(className,
                        attributeName);
            }
            v = ua.getVisibility();
            c = c.getSuperClass();
        }
        if (v == null) {
            throw new AttributeNotFoundException(className, attributeName);
        }
        return v;
    }

    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        Class c = checkClassException(className);
        while (c.getSuperClass() != null) {
            c = c.getSuperClass();
        }
        return c.getClassName();
    }

    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        Class c = checkClassException(className);
        ArrayList<String> list = new ArrayList<>();
        HashMap<Interface, Integer> visited = new HashMap<>();
        while (c != null) {
            LinkedList<Interface> queue = new LinkedList<>();
            ArrayList<Interface> realInter = c.getRealInter();
            for (Interface inter : realInter) {
                if (!visited.containsKey(inter)) {
                    queue.offer(inter);
                    list.add(inter.getInterName());
                    visited.put(inter, 1);
                }
            }
            while (!queue.isEmpty()) {
                ArrayList<Interface> superInter = queue.poll().getSuperInter();
                for (Interface inter : superInter) {
                    if (!visited.containsKey(inter)) {
                        list.add(inter.getInterName());
                        visited.put(inter, 1);
                        queue.offer(inter);
                    }
                }
            }
            c = c.getSuperClass();
        }
        return list;
    }

    public List<AttributeClassInformation> getInformationNotHidden(
            String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        Class c = checkClassException(className);
        ArrayList<AttributeClassInformation> list = new ArrayList<>();
        while (c != null) {
            String curClassName = c.getClassName();
            ArrayList<UmlAttribute> nonPrivateAttr = c.getNonPrivateAttr();
            for (UmlAttribute ua : nonPrivateAttr) {
                String attrName = ua.getName();
                list.add(new AttributeClassInformation(attrName, curClassName));
            }
            c = c.getSuperClass();
        }
        return list;
    }

    public HashMap<String, Class> getAllClass() {
        return classId2Class;
    }

    public HashMap<String, Interface> getAllInter() {
        return interId2Interface;
    }

    public UmlClass getUmlClass(String classId) {
        return eleClass.get(classId);
    }

    public UmlInterface getUmlInterface(String interId) {
        return eleInter.get(interId);
    }
}