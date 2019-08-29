package derivation;

class PowerFunction extends Divisor {

    PowerFunction(String input) throws  Exception {
        super(input);
    }

    @Override
    String Derivation() {
        String derivation = "";
        if (super.GetPower() == 1) {
            derivation = derivation + "1";
        } else {
            derivation = derivation
                    + super.GetPower().toString() + "*"
                    + "x^";
            String power = Integer.toString(super.GetPower() - 1);
            derivation = derivation.concat(power);
        }
        return derivation;
    }
}
