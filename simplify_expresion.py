from sympy import *
import sys
import re

def make_it_simpler(filename):
    sol = ""
    try:
        with open(filename,"r") as f:
            sol = f.readline()
            sol = str(simplify(sol))
            sol = re.sub('[*]{2}','^',sol)

        with open(filename[:-4]+"_simplified.txt","w") as f:
            f.write(sol)
    finally:
        print(sol)

if __name__ == "__main__":
    args = sys.argv
    sol = ""
    for i in range(1,7):
        for j in range(1,5):
            make_it_simpler(f"solutionSC\solutionSC{i}\solution_{i}_{j}.txt")

    for i in range(7,9):
            for j in range(1,2):
                make_it_simpler(f"solutionSC\solutionSC{i}\solution_{i}_{j}.txt")
#python -m simplify_expresion solution\solution1\solution_1_3.txt
