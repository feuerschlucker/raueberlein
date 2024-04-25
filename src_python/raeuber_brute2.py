
from audioop import reverse
import pandas as pd
import numpy as np
import sys
import instances
import time
from multiprocessing import Process, Event, Queue, Lock
import multiprocessing
import copy


def create_solutions(start: int, stop: int, no_items: int, event, best_queue, best_ovf, weights, cutoff):
    best = {"ovf": best_ovf, "sol": weights}
    for number in range(start, stop):
        if event.is_set():  # break condition if one of the
            break
        binary_number = format(number, f'0{no_items}b')
        permutation = [int(char) for char in binary_number]
        solution = np.array(permutation)
        ovf = np.dot((solution * 2-1), weights)

        if abs(ovf) < abs(best_ovf):
            best_ovf = abs(ovf)
            best_solution = tuple(permutation)
            best = {"ovf": best_ovf, "sol": best_solution}
            if best_ovf < cutoff:
                print(f'!!! ovf below cutoff of {cutoff} !!!')
                event.set()

    best_queue.put_nowait(best)


def start_heuristic(weights):
    weights = np.sort(weights)
    weights = weights[::-1]
    # print(weights)
    sum1 = 0
    sum2 = 0
    for i in range(0, len(weights)):
        if sum1 >= sum2:
            sum2 = sum2+weights[i]
        else:
            sum1 = sum1+weights[i]
    return abs(sum1-sum2)


def main():

    durchlaeufe = 10
    cutoff = 0.000001

    for dulf in range(durchlaeufe):
        name = f"lognormal_dist_realisation {dulf+1}"
        timestart = time.time()
        no_items = 24

        items = instances.create_itemlist_lognormal(no_items, name)

        best_solution = []
        weights = np.array(items["wert"])
        # print(weights)
        best_ovf = abs(weights.sum())

        splits = multiprocessing.cpu_count()
        splits = 2
        event = Event()
        best_queue = Queue()

        arguments = []
        item = []
        increment = (2**(no_items))/2/splits
        start = 0

        for i in range(splits):
            stop = start + increment
            item = [event, int(start), int(stop), no_items,
                    best_queue, best_ovf, weights, cutoff]
            arguments.append(item)
            start = start+increment

        processes = [Process(target=create_solutions, args=(
            start, stop, no_items, event, best_queue, best_ovf, weights, cutoff)) for event, start, stop, no_items, best_queue, best_ovf, weights, cutoff in arguments]

        for process in processes:
            process.start()

        for process in processes:
            process.join()

        while not best_queue.empty():
            best = best_queue.get()
            if best["ovf"] < best_ovf:
                best_ovf = best["ovf"]
                best_solution = best["sol"]

        print(name)

        print(best_solution)

        print("best ovf : ", best_ovf)
        print("duration : ", time.time()-timestart, " sec")
        stheu = start_heuristic(weights)
        print("heuristic :", stheu)


if __name__ == "__main__":
    main()
