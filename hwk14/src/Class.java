import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;

public class Class {
    private final String className;
    private final String classId;
    private Class superClass = null;
    private HashMap<String, UmlAttribute> attr = new HashMap<>();
    private ArrayList<UmlAttribute> nonPrivateAttr = new ArrayList<>();
    private int duplicatedAttrNum = 0;
    private HashMap<String, ArrayList<UmlOperation>> oper = new HashMap<>();
    private HashMap<String, UmlOperation> nonReturnOper = new HashMap<>();
    private HashMap<String, UmlOperation> nonParamOper = new HashMap<>();
    private int allOperNum = 0;
    private ArrayList<Class> assoClass = new ArrayList<>();
    private ArrayList<Interface> assoInter = new ArrayList<>();
    private ArrayList<Interface> realInter = new ArrayList<>();
    private HashMap<String, UmlAssociationEnd> assoEnd = new HashMap<>();

    public Class(String className, String classId) {
        this.className = className;
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public String getClassId() {
        return classId;
    }

    public void setSuperClass(Class superClass) {
        this.superClass = superClass;
    }

    public Class getSuperClass() {
        return superClass;
    }

    public void addAttr(UmlAttribute ua) {
        String attrName = ua.getName();
        if (attr.containsKey(attrName)) {
            attr.put(attrName, null);
            duplicatedAttrNum++;
        } else {
            attr.put(attrName, ua);
        }
        if (ua.getVisibility() != Visibility.PRIVATE) {
            nonPrivateAttr.add(ua);
        }
    }

    public HashMap<String, UmlAttribute> getAttr() {
        return attr;
    }

    public ArrayList<UmlAttribute> getNonPrivateAttr() {
        return nonPrivateAttr;
    }

    public int getAttrNum() {
        return (attr.size() + duplicatedAttrNum);
    }

    public void addOper(UmlOperation uo) {
        String operName = uo.getName();
        String operId = uo.getId();
        if (!oper.containsKey(operName)) {
            oper.put(operName, new ArrayList<>());
        }
        oper.get(operName).add(uo);

        nonReturnOper.put(operId, uo);
        nonParamOper.put(operId, uo);

        allOperNum++;
    }

    public ArrayList<UmlOperation> getOperByName(String operName) {
        if (oper.containsKey(operName)) {
            return oper.get(operName);
        }
        return new ArrayList<>();
    }

    public void updateOperInfo(UmlParameter up) {
        String operId = up.getParentId();
        Direction direction = up.getDirection();
        if (direction.equals(Direction.RETURN)) {
            nonReturnOper.remove(operId);
        } else {
            nonParamOper.remove(operId);
        }
    }

    public int getNonReturnOperNum() {
        return nonReturnOper.size();
    }

    public int getReturnOperNum() {
        return (allOperNum - nonReturnOper.size());
    }

    public int getNonParamOperNum() {
        return nonParamOper.size();
    }

    public int getParamOperNum() {
        return (allOperNum - nonParamOper.size());
    }

    public int getAllOperNum() {
        return allOperNum;
    }

    public void addRealInter(Interface inter) {
        realInter.add(inter);
    }

    public ArrayList<Interface> getRealInter() {
        return realInter;
    }

    public void addAsso(Class c) {
        assoClass.add(c);
    }

    public void addAsso(Interface inter) {
        assoInter.add(inter);
    }

    public ArrayList<Class> getAssoClass() {
        return assoClass;
    }

    public int getAssoNum() {
        return (assoClass.size() + assoInter.size());
    }

    public void addAssoEnd(UmlAssociationEnd uae) {
        String assoEndName = uae.getName();
        if (assoEnd.containsKey(assoEndName)) {
            assoEnd.put(assoEndName, null);
        } else {
            assoEnd.put(assoEndName, uae);
        }
    }

    public HashMap<String, UmlAssociationEnd> getAssoEnd() {
        return assoEnd;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Class
                && ((Class) obj).classId.equals(this.classId));
    }

    @Override
    public int hashCode() {
        return classId.hashCode();
    }
}