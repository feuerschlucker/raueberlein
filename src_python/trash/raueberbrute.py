import pandas as pd
import numpy as np
import sys
import instances
import time
from multiprocessing import Process, Event, Queue, Lock
import multiprocessing

best_ovf_lock = Lock()
best_solution_lock = Lock()


def objective_value_function(solution: np.array, event, best_queue):

    global weights
    global best_ovf
    global best_solution
    ovf = np.dot((solution * 2-1), weights)

    with best_ovf_lock:

        if abs(ovf) < best_ovf:
            best_ovf = abs(ovf)
            best_solution = solution
            best = {best_ovf: best_solution}
            best_queue.put(best)
            if best_ovf <= 10e-20:
                event.set()


def create_solutions(start: int, stop: int, no_items: int, event, best_queue):

    for number in range(start, stop):
        if event.is_set():  # break condition if one of the
            print(f' ended premature')
            return
        binary_number = format(number, f'0{no_items}b')
        permutation = [int(char) for char in binary_number]
        solution = np.array(permutation)
        objective_value_function(solution, event, best_queue)


def main():
    timestart = time.time()
    no_items = 15
    # items = instances.create_itemlist_normal(no_items)
    items = instances.create_itemlist_lognormal(no_items)

    global weights
    global best_solution
    best_solution = []
    weights = np.array(items["wert"])
    print(weights)
    global best_ovf
    best_ovf = abs(weights.sum())

    print("start solution = sum of items : ", best_ovf)

    splits = multiprocessing.cpu_count()
    print(splits)

    begin = time.time()
    event = Event()
    best_queue = Queue()
    arguments = []
    item = []

    print("total no of possible solutions: ", 2**no_items-1)
    increment = (2**(no_items))/2/splits
    print(increment)
    start = 0
    for i in range(splits):
        stop = start + increment
        item = [event, int(start), int(stop), no_items, best_queue]
        arguments.append(item)
        start = start+increment

    processes = [Process(target=create_solutions, args=(
        start, stop, no_items, event, best_queue)) for event, start, stop, no_items, best_queue in arguments]
    for process in processes:
        process.start()
    for process in processes:
        process.join()

    while not best_queue.empty():
        best = best_queue.get()
        ovf = list(best.keys())[0]
        solution = best[ovf]
        if ovf < best_ovf:
            best_ovf = ovf
            best_solution = solution

    print(best_solution)
    print("best ovf : ", best_ovf)
    print("duration : ", time.time()-timestart, " sec")


if __name__ == "__main__":
    main()
