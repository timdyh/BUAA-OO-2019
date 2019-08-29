package homework;

public abstract class Rule implements Cloneable {
    public Rule clone() {
        Rule clone = null;
        try {
            clone = (Rule) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    public abstract String getString();

    public abstract Rule dev();
}
