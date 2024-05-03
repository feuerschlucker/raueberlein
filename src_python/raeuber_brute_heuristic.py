
from audioop import reverse
import pandas as pd
import numpy as np
import sys
import instances
import time
from multiprocessing import Process, Event, Queue, Lock
import multiprocessing
import copy

import gc


def create_solutions(start: int, stop: int, no_items: int, event, best_queue, best_ovf, weights, cutoff, offset):
    xxx = time.time()
    print(f"start : {start} , stop : {stop}")
    best = {"ovf": best_ovf, "sol": weights}
    for number in range(start, stop):
        if event.is_set():  # break condition if one of the other threads has already stoopped.
            break
        binary_number = format(number, f'0{no_items}b')
        permutation = [int(char) for char in binary_number]
        solution = np.array(permutation)
        ovf = np.dot((solution * 2-1), weights)
        ovf = ovf - offset
        if abs(ovf) < abs(best_ovf):
            best_ovf = abs(ovf)
            best_solution = tuple(permutation)
            best = {"ovf": best_ovf, "sol": best_solution}
            if best_ovf < cutoff:
                print(f'!!! ovf below cutoff of {cutoff} !!!')
                event.set()

    best_queue.put(best)
    gc.collect()
    print(f" stop after : {time.time()-xxx}")


def start_heuristic(items):
    weights = np.array(items["wert"])
    weights = np.sort(weights)[::-1]
    weights = np.sort(weights)
    weights = weights[::-1]
    solution = []
    # print(weights)
    sum1 = 0
    sum2 = 0
    for i in range(0, len(weights)):
        if sum1 >= sum2:
            sum2 = sum2+weights[i]
            solution.append(0)
        else:
            sum1 = sum1+weights[i]
            solution.append(1)
    print(solution)
    return (sum1-sum2)


def is_MonaLisa_distributed(items):
    sum_value = items['wert'].sum()
    max_value = items['wert'].max()
    if max_value >= sum_value/2:
        print(sum_value, max_value)
        return True
    else:
        print(sum_value, max_value)
        return False


def MonaLisa_solution(items):
    items["Raeuber1"] = 0
    items["Raeuber2"] = 1
    mona_row = items["wert"].idxmax()
    items.loc[mona_row, "Raeuber1"] = 1
    items.loc[mona_row, "Raeuber2"] = 0
    print(items)
    return items


def is_Banksy_distributed(items):
    weights = np.array(items["wert"])
    weights = np.sort(weights)[::-1]
    upper = weights[0:int(len(weights)/2)].sum()
    lower = weights[int(len(weights)/2):].sum()
    if lower > upper:
        return True


def Banksy_solution(items):
    items["Raeuber1"] = 0
    items["Raeuber2"] = 0
    items.loc[0:int(len(items)/2), "Raeuber1"] = 1
    items.loc[int(len(items)/2):, "Raeuber2"] = 1
    return items


def main():
    iterations = 5
    cutoff = 0.00000001
    for iteration in range(iterations):
        name = f"lognormal_dist_realisation {iteration+1}"
        timestart = time.time()
        no_items = 19
        items = instances.create_itemlist_lognormal(no_items, name)

        # items = instances.read_csv_instance("test_heuristic.csv")
        # print(items)
        # no_items = len(items)
        # print(no_items)

        # items.loc[0, "wert"] = 3000  # test Mona Lisa
        # items = pd.read_csv('Data/banksy.csv')  # Test Banksy
        # name = "xxx"
        if is_MonaLisa_distributed(items):
            name = name + "_solution"
            items = MonaLisa_solution(items)
            instances.save_as_csv(items, name)
            continue
        if len(items) % 2 == 1:
            if is_Banksy_distributed(items):
                items = Banksy_solution(items)
                instances.save_as_csv(items, name)

        if len(items) >= 26:
            offset = start_heuristic(items[0:len(items)-26])
            items = items.sort_values(by=["wert"], ascending=False)
            print(items)
            pass

        best_solution = []
        weights = np.array(items["wert"])
        best_ovf = abs(weights.sum())

        splits = multiprocessing.cpu_count()
        # print(len(items))

        if len(items) == 3:
            print("please do it by hand")
            continue

        if len(items) == 4:
            # print("xxx")
            splits = 1

        event = Event()
        best_queue = Queue()
        splits = 5
        arguments = []
        paramlist = []
        increment = int((2**(no_items))/2/splits)
        print(increment)
        start = 0
        offset = 0

        for i in range(splits):
            # print("start : ", start)
            if i == splits-1:
                stop = 2**(no_items)/2
            else:
                stop = start + increment
            paramlist = [event, int(start), int(stop), no_items,
                         best_queue, best_ovf, weights, cutoff, offset]
            arguments.append(paramlist)
            start = start+increment
            # print("stop : ", stop)

        processes = [Process(target=create_solutions, args=(
            start, stop, no_items, event, best_queue, best_ovf, weights, cutoff, offset)) for event, start, stop, no_items, best_queue, best_ovf, weights, cutoff, offset in arguments]

        for process in processes:
            process.start()

        for process in processes:
            process.join()

        while not best_queue.empty():
            best = best_queue.get()
            if best["ovf"] < best_ovf:
                best_ovf = best["ovf"]
                best_solution = best["sol"]
        # time.sleep(5)
        print(name)

        print(best_solution)
        items["Raeuber1"] = best_solution
        items["Raeuber2"] = 1 - items["Raeuber1"]
        print(items)

        print("best ovf : ", best_ovf)
        print("duration : ", time.time()-timestart, " sec")
        print("xxxxxxxxxxxxxxxxxxxxxxx Halas xxxxxxxxxxxxxxxxxxxxxxx")
        # stheu = start_heuristic(weights)
        # print("heuristic :", stheu)
        gc.collect()


if __name__ == "__main__":
    main()
