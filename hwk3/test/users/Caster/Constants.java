package derivation;

import java.math.BigInteger;

class Constants extends Divisor {

    Constants(String input) throws Exception {
        super(input);
    }

    @Override
    void MergeDivisor() {
        BigInteger con = new BigInteger(super.GetDivisor());
        String newCon = String.valueOf(con);
        super.ChangeDivisor(newCon);
    }
}
