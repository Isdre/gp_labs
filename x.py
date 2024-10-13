
import numpy as np
import pandas as pd

import numpy as np

# Define the functions
def f1(x, y):
    return x + 2 * y

def f2(x,y):
    return np.sin(x / 2) + 2 * np.cos(x)

def f3(x, y):
    return x**2 + 3*x*y - 7*y + 1

# Domains for the functions
domains_f1 = [
    (np.linspace(0, 1, 50), np.linspace(0, 1, 50)),
    (np.linspace(-10, 10, 50), np.linspace(-10, 10, 50)),
    (np.linspace(0, 100, 50), np.linspace(0, 100, 50)),
    (np.linspace(-1000, 1000, 50), np.linspace(-1000, 1000, 50)),
]

domains_f2 = [
    (np.linspace(-3.14, 3.14, 50),np.linspace(-3.14, 3.14, 50)),
    (np.linspace(0, 7, 50),np.linspace(0, 7, 50)),
    (np.linspace(0, 100, 50),np.linspace(0, 100, 50)),
    (np.linspace(-100, 100, 50),np.linspace(-100, 100, 50)),
]

domains_f3 = [
    (np.linspace(-10, 10, 50), np.linspace(-10, 10, 50)),
    (np.linspace(0, 100, 50), np.linspace(0, 100, 50)),
    (np.linspace(-1, 1, 50), np.linspace(-1, 1, 50)),
    (np.linspace(-1000, 1000, 50), np.linspace(-1000, 1000, 50)),
]

# Function to save arrays to file with x, y, f(x, y) format
def save_to_file_2d(file_name, x_vals, y_vals, func):
    with open(file_name, "w") as f:
        for x in x_vals:
            for y in y_vals:
                f.write(f"{x}\t{y}\t{func(x, y)}\n")


# Generate values for f1 and save to .txt files
for i, (x_vals, y_vals) in enumerate(domains_f1):
    save_to_file_2d(f"data_4_{i+1}.txt", x_vals, y_vals, f1)

# Generate values for f2 and save to .txt files
for i, (x_vals, y_vals) in enumerate(domains_f2):
    save_to_file_2d(f"data_5_{i+1}.txt", x_vals, y_vals, f2)

# Generate values for f3 and save to .txt files
for i, (x_vals, y_vals) in enumerate(domains_f3):
    save_to_file_2d(f"data_6_{i+1}.txt", x_vals, y_vals, f3)