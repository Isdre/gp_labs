import numpy as np
import pandas as pd

# Define the functions
def f1(x):
    return np.sin(x + 3.141592/2)

def f2(x):
    return np.tan(2*x +1)

# Domains for the functions
domain_f1 = np.linspace(-np.pi, np.pi, 50)

domain_f2 = np.linspace(-np.pi/2 + 0.01, np.pi/2 - 0.01, 50)

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

def save_to_file(file_name, x_vals, func):
    with open(file_name, "w") as f:
        for x in x_vals:
            f.write(f"{x}\t{func(x)}\n")


# # Generate values for f1 and save to .txt files
# for i, (x_vals, y_vals) in enumerate(domains_f1):
#     save_to_file_2d(f"data_4_{i+1}.txt", x_vals, y_vals, f1)
#
# # Generate values for f2 and save to .txt files
# for i, (x_vals, y_vals) in enumerate(domains_f2):
#     save_to_file_2d(f"data_5_{i+1}.txt", x_vals, y_vals, f2)
#
# # Generate values for f3 and save to .txt files
# for i, (x_vals, y_vals) in enumerate(domains_f3):
#     save_to_file_2d(f"data_6_{i+1}.txt", x_vals, y_vals, f3)

save_to_file("data/data7/data_7_1.txt",domain_f1,f1)
save_to_file("data/data8/data_8_1.txt",domain_f2,f2)