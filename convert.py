import sys
import re

if __name__ == "__main__":
    args = sys.argv
    sol = ""
    with open(args[1],"r") as f:
        sol = f.readline()
        sol = re.sub(' ','',sol)
        sol = re.sub('\t','',sol)
        sol = re.sub('\n','',sol)
        sol = re.sub('sin','SIN',sol)
        sol = re.sub('cos','COS',sol)
        sol = re.sub('ln','LN',sol)

        for i, x in enumerate(args[2:]):
            sol = re.sub(f"X{i+1}",x,sol)

    with open(args[1],"w") as f:
            f.write(sol)