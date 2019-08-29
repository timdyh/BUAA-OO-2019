package derivation;

import java.util.Stack;

class SplitString {

    String[] Split(Stack<CharAndLocation> stack,
                   StringBuilder stringBuilder) throws Exception {
        for (CharAndLocation x:stack
        ) {
            if (x.GetCharacter() == '(') {
                throw new Exception("WRONG FORMAT");
            }
        }
        if (!stack.empty() &&
                stack.peek().GetId() == stringBuilder.length() - 1) {
            throw new Exception("WRONG FORMAT!");
        }
        for (CharAndLocation x: stack
        ) {
            stringBuilder.setCharAt(x.GetId(),' ');
        }
        String string = stringBuilder.toString().trim();
        return string.split(" ");
    }

    Stack<CharAndLocation> CleanStack(StringBuilder stringBuilder
            ,Stack<CharAndLocation> stack,int loc) throws Exception {
        if (stringBuilder.charAt(loc) == ')') {
            while (stack.peek().GetCharacter() != '(' &&
                    !stack.empty()) {
                stack.pop();
            }
            if (stack.empty()) {
                throw new Exception("WRONG FORMAT!");
            }
            stack.pop();
        }
        return stack;
    }
}
