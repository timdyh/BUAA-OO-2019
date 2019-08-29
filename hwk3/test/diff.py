from sympy import *
import re

str0 = input()
str0 = re.sub(r'(?<!\d)0+(?=\d)', "", str0)
str0 = str0.replace("^", "**")

x = Symbol('x')
expr = eval(str0)
expr_diff = diff(expr, x)
expr_diff = expr_diff.__str__().replace("**", "^")
print(expr_diff)