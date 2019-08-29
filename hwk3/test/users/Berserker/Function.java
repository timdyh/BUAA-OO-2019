package homework;

import java.math.BigInteger;

public abstract class Function implements Cloneable {
    private BigInteger coef;
    private BigInteger index;

    public Function clone() {
        Function clone = null;
        try {
            clone = (Function) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    public abstract String dev();

    public abstract String getString();

    public void setCoef(BigInteger in) {
        coef = in;
    }

    public BigInteger getCoef() {
        return coef;
    }

    public void setIndex(BigInteger in) {
        index = in;
    }

    public BigInteger getIndex() {
        return index;
    }
}
