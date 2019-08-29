package homework;

import java.math.BigInteger;

public class Nest extends Rule {
    private Function out;
    private Rule in = null;

    public String getString() {
        if (out != null && in != null) {
            String neg = out.getCoef().toString() + "*";
            if (out.getCoef().compareTo(BigInteger.ONE) == 0) {
                neg = "";
            }
            String triangle = "cos";
            if (((Triangle) out).getSin()) {
                triangle = "sin";
            }
            if (out.getIndex().compareTo(BigInteger.ONE) == 0) {
                return neg + triangle + "(" + in.getString() + ")";
            } else {
                return neg + triangle + "(" + in.getString() + ")^"
                        + out.getIndex().toString();
            }
        } else if (out != null) {
            return out.getString();
        } else if (in != null) {
            return in.getString();
        } else {
            return "";
        }
    }

    public void addOut(Function newOut) {
        out = newOut;
    }

    public void addIn(Rule newIn) {
        in = newIn;
    }

    public Multi dev() {
        Multi res = new Multi();
        Function outDev = null;
        if (out != null) {
            String outTemp = out.dev();
            if (out instanceof Triangle) {
                if (outTemp.split("\\*").length == 2) {
                    Nest newTerm = new Nest();
                    newTerm.setFunc(new Triangle(outTemp
                            .split("\\*")[1], 1), in);
                    res.add(newTerm);
                    outDev = new Triangle(outTemp.split("\\*")[0], 1);
                } else {
                    outDev = new Triangle(outTemp.split("\\*")[0], 1);
                }
            } else if (out instanceof Power) {
                outDev = new Power(outTemp, 1);
            } else if (out instanceof Constant) {
                outDev = new Constant(outTemp, 1);
            }
        }
        if (out != null && in != null) {
            Nest temp = new Nest();
            temp.setFunc(outDev, in);
            res.add(temp);
            res.add(in.dev());
        } else if (out != null) {
            Nest temp = new Nest();
            temp.setFunc(outDev, in);
            res.add(temp);
        } else if (in != null) {
            res.add(in.dev());
        }
        return res;
    }

    public void setFunc(Function outIn, Rule inIn) {
        out = outIn;
        in = inIn;
    }

    public Nest clone() {
        Nest copy = (Nest) super.clone();
        if (in != null && out != null) {
            copy.setFunc(out.clone(), in.clone());
        } else if (in != null) {
            copy.setFunc(null, in.clone());
        } else if (out != null) {
            copy.setFunc(out.clone(), null);
        }
        return copy;
    }
}
