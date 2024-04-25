import pandas as pd
import numpy as np
import sys
import instances
import time
from multiprocessing import Pool, Event, Lock, Queue
import multiprocessing


def create_solutions(items, no_items, best_ovf, queue):
    weights = np.array(items["wert"])
    for number in range(2**no_items):
        binary_number = format(number, f'0{no_items}b')
        permutation = [int(char) for char in binary_number]
        solution = np.array(permutation)
        ovf = np.dot((solution * 2-1), weights)

        if abs(ovf) < best_ovf:
            best_ovf = abs(ovf)
            best_solution = tuple(permutation)
            return {"ovf": best_ovf, "sol": best_solution}
    # If no better solution found, return None
    return None


def main():
    global weights
    timestart = time.time()
    no_items = 14
    items = instances.create_itemlist_lognormal(no_items)

    best_solution = []
    weights = np.array(items["wert"])
    best_ovf = abs(weights.sum())

    print("start solution = sum of items : ", best_ovf)
    splits = multiprocessing.cpu_count()
    splits = 8

    # Use a Pool for easier process management
    pool = Pool(processes=splits)

    # Separate results queue for each process
    results = [pool.apply_async(create_solutions, args=(
        items, no_items, best_ovf, Queue())) for _ in range(splits)]

    # Wait for all processes to finish and collect results
    best_result = None
    for result in results:
        solution = result.get()
        if solution and solution["ovf"] < best_ovf:
            best_result = solution

    if best_result:
        best_ovf = best_result["ovf"]
        best_solution = best_result["sol"]

    print(best_solution)
    print("best ovf : ", best_ovf)
    print("duration : ", time.time()-timestart, " sec")


if __name__ == "__main__":
    main()
