from sympy import *
import re

str1 = input()
str2 = input()
str1 = str1.replace("^", "**")
str2 = str2.replace("^", "**")
str1 = re.sub(r'(?<!\d)0+(?=\d)', "", str1)
str2 = re.sub(r'(?<!\d)0+(?=\d)', "", str2)

x = Symbol('x')
expr1 = eval(str1 + "+x-x")
expr2 = eval(str2 + "+x-x")
if expr1.equals(expr2):
    print("\\033[32mCorrect\\033[0m")
else:
    print("\\033[31mIncorrect\\033[0m")
