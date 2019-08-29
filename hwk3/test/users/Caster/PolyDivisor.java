package derivation;

import java.util.regex.Pattern;

class PolyDivisor extends Divisor {
    private PolyTree polyTree;
    private static final String X = "^x?$";
    private static final String CON = "^[+-]?\\d+$";

    PolyDivisor(String input) throws Exception {
        super(input);
    }

    @Override
    void MergeDivisor() throws Exception {
        StringBuilder body = new StringBuilder(super.GetDivisor());
        body.deleteCharAt(0);
        int len = body.length();
        body.deleteCharAt(len - 1);

        super.ChangeBase(body.toString());
        super.ChangePower(1);
        polyTree = new PolyTree(body.toString());
        polyTree.BuildPolyTree();
    }

    @Override
    String Derivation() {
        String base = super.GetBse();
        if (Pattern.compile(X).matcher(base).matches()) {
            return "1";
        } else if (Pattern.compile(CON).matcher(base).matches()) {
            return "0";
        }
        return  "(" + super.PolyTreeDer(this.polyTree) + ")";
    }
}
