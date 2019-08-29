package homework;

import java.util.ArrayList;

public class Multi extends Rule {
    private String multi = "*";
    private ArrayList<Rule> func = new ArrayList<>();

    public String getString() {
        String res = "(";
        for (int i = 0; i < func.size(); i++) {
            if (i != 0) {
                res += multi;
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
        Add res = new Add();
        for (int i = 0; i < func.size(); i++) {
            Multi temp = new Multi();
            temp.add(func.get(i).dev());
            for (int j = 0; j < func.size(); j++) {
                if (j != i) {
                    temp.add(func.get(j).clone());
                }
            }
            res.add(temp);
        }
        return res;
    }

    public void setFunc(ArrayList<Rule> in) {
        func = in;
    }

    public Multi clone() {
        Multi copy = (Multi) super.clone();
        ArrayList<Rule> temp = new ArrayList<>();
        for (int i = 0; i < func.size(); i++) {
            temp.add(func.get(i).clone());
        }
        copy.setFunc(temp);
        return copy;
    }
}
