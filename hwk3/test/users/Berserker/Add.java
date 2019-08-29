package homework;

import java.util.ArrayList;

public class Add extends Rule {
    private String add = "+";
    private ArrayList<Rule> func = new ArrayList<>();

    public String getString() {
        String res = "(";
        for (int i = 0; i < func.size(); i++) {
            if (i != 0) {
                res += add;
            }
            res += func.get(i).getString();
        }
        res += ")";
        return res;
    }

    public void add(Rule newFunc) {
        func.add(newFunc);
    }

    public Add dev() {
        Add temp = new Add();
        for (int i = 0; i < func.size(); i++) {
            temp.add(func.get(i).dev());
        }
        return temp;
    }

    public void setFunc(ArrayList<Rule> in) {
        func = in;
    }

    public Add clone() {
        Add copy = (Add) super.clone();
        ArrayList<Rule> temp = new ArrayList<>();
        for (int i = 0; i < func.size(); i++) {
            temp.add(func.get(i).clone());
        }
        copy.setFunc(temp);
        return copy;
    }
}
