package derivation;

import java.util.ArrayList;

class Divisor {
    private String divisor;
    private Integer power;
    private String base;

    Divisor(String input) throws Exception {
        this.divisor = input;
        MergeDivisor();
    }

    void MergeDivisor() throws Exception {
        String[] strings = this.divisor.split("\\^");
        this.base = strings[0];
        if (strings.length == 2) {
            this.power = Integer.parseInt(strings[1]);
        } else {
            this.power = 1;
        }
        ChangeDivisor(this.base + "^" + this.power);
    }

    void ChangePower(Integer power) {
        this.power = power;
    }

    void ChangeBase(String base) {
        this.base = base;
    }

    void ChangeDivisor(String s) {
        this.divisor = s;
    }

    String Derivation() {
        return "0";
    }

    String GetDivisor() {
        return this.divisor;
    }

    Integer GetPower() {
        return this.power;
    }

    String GetBse() {
        return this.base;
    }

    String PolyTreeDer(PolyTree polyTree) {
        String derivation = "";
        ArrayList<String> divisorDer;
        divisorDer = polyTree.Derivation();
        for (String string: divisorDer
        ) {
            derivation = derivation.concat(string);
        }
        return derivation;
    }
}
