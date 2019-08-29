import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {
    private String cleanPoly;

    public InputHandler(String str) {
        //see if there's illegal char
        String legalCharReg = "\\dcinosx\\(\\)\\*\\+\\-\\^ \t";
        legalCharReg = "[" + legalCharReg + "]" + "+";
        if (!str.matches(legalCharReg)) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }

        //blank char errors
        String bce1 = "\\d\\s+\\d";
        String bce2 = "[\\^\\*]\\s*[\\+\\-]\\s+\\d";
        String bce3 = "s\\s+i";
        String bce4 = "i\\s+n";
        String bce5 = "c\\s+o";
        String bce6 = "o\\s+s";
        String bce7 = "[\\+\\-]\\s*[\\+\\-]\\s*[\\+\\-]\\s+";

        Pattern bcep1 = Pattern.compile(bce1);
        Pattern bcep2 = Pattern.compile(bce2);
        Pattern bcep3 = Pattern.compile(bce3);
        Pattern bcep4 = Pattern.compile(bce4);
        Pattern bcep5 = Pattern.compile(bce5);
        Pattern bcep6 = Pattern.compile(bce6);
        Pattern bcep7 = Pattern.compile(bce7);

        Matcher bcem1 = bcep1.matcher(str);
        Matcher bcem2 = bcep2.matcher(str);
        Matcher bcem3 = bcep3.matcher(str);
        Matcher bcem4 = bcep4.matcher(str);
        Matcher bcem5 = bcep5.matcher(str);
        Matcher bcem6 = bcep6.matcher(str);
        Matcher bcem7 = bcep7.matcher(str);

        boolean bces1 = bcem1.find();
        boolean bces2 = bcem2.find();
        boolean bces3 = bcem3.find();
        boolean bces4 = bcem4.find();
        boolean bces5 = bcem5.find();
        boolean bces6 = bcem6.find();
        boolean bces7 = bcem7.find();

        if (bces1 || bces2 || bces3 || bces4 || bces5 || bces6 || bces7) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }

        String noSpace = str.replaceAll("\\s", "");
        if (noSpace.equals("")) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }

        //op errors
        String oe1 = "[\\+\\-][\\+\\-][\\+\\-]\\D";
        String oe2 = "[\\^\\*][\\+\\-]\\D";
        String oe3 = "[\\+\\-\\*]$";

        Pattern oep1 = Pattern.compile(oe1);
        Pattern oep2 = Pattern.compile(oe2);
        Pattern oep3 = Pattern.compile(oe3);

        Matcher oepm1 = oep1.matcher(noSpace);
        Matcher oepm2 = oep2.matcher(noSpace);
        Matcher oepm3 = oep3.matcher(noSpace);

        boolean oeps1 = oepm1.find();
        boolean oeps2 = oepm2.find();
        boolean oeps3 = oepm3.find();

        if (oeps1 || oeps2 || oeps3) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }

        String nestedNumErrrorReg = "[ns]\\((([\\+\\-][\\+\\-])|" +
                "([\\+\\-][\\+\\-][\\+\\-]))\\d+\\)";
        Pattern nestedNumErrorPattern = Pattern.compile(nestedNumErrrorReg);
        Matcher nestedNumErrorMatcher = nestedNumErrorPattern.matcher(noSpace);
        boolean nestedNumError = nestedNumErrorMatcher.find();
        if (nestedNumError) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }

        //replace "--" with "+"
        noSpace = noSpace.replaceAll("\\-\\-", "+");

        //replace one or more "+" with "+"
        noSpace = noSpace.replaceAll("\\++", "+");

        //replace "+-+" with "-"
        noSpace = noSpace.replaceAll("\\+\\-\\+", "-");

        //replace "-+-" with "+"
        noSpace = noSpace.replaceAll("\\-\\+\\-", "+");

        //replace "-+", "+-" with "-"
        noSpace = noSpace.replaceAll("(\\-\\+)|(\\+\\-)", "-");

        //remove "+" after '^' or '*'
        for (int i = 1; i < noSpace.length(); i++) {
            boolean state1 = noSpace.charAt(i) == '+';
            boolean state2 = noSpace.charAt(i - 1) == '^';
            boolean state3 = noSpace.charAt(i - 1) == '*';
            if (state1 && state2 || state1 && state3) {
                noSpace = noSpace.substring(0, i) + noSpace.substring(i + 1);
            }
        }

        this.cleanPoly = noSpace;
    }

    protected String getCleanPoly() {
        return cleanPoly;
    }
}