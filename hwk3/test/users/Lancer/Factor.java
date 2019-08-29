import java.math.BigInteger;
import java.util.ArrayList;

public abstract class Factor {
    protected abstract ArrayList<Factor> diff();

    protected abstract boolean isConst();

    protected abstract BigInteger getConst();

    protected abstract BigInteger getExp();

    protected abstract String print();
}