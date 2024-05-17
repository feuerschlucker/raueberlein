package main

import (
	"fmt"
	"math"
	"math/rand"
	"runtime"
	"sort"
	"time"
)

type BestSolution struct {
	Ovf float64
	Sol []int
}

type Item struct {
	Name string
	Wert float64
}

type SolutionAndSignal struct {
	Solution BestSolution
	Done     bool
}

func createSolutions(start int, stop int, weights []float64, bestOvf chan<- float64) {
	bestXXX := 0.0
	for i := range weights {
		bestXXX += weights[i]
	}
	for number := start; number < stop; number++ {
		binaryNumber := fmt.Sprintf("%0*b", len(weights), number)
		permutation := make([]int, len(binaryNumber))
		for i, char := range binaryNumber {
			permutation[i] = int(char - '0')
		}
		solution := make([]float64, len(permutation))
		for i, v := range permutation {
			solution[i] = float64(v*2 - 1)
		}
		ovf := 0.0
		for i := range weights {
			ovf += solution[i] * weights[i]
		}
		if math.Abs(ovf) < math.Abs(bestXXX) {
			bestXXX = math.Abs(ovf)
		}
	}
	bestOvf <- bestXXX
}

func startHeuristic(weights []float64) float64 {
	// Sorting in descending order
	sort.Slice(weights, func(i, j int) bool {
		return weights[i] > weights[j]
	})

	sum1, sum2 := 0.0, 0.0
	for _, weight := range weights {
		if sum1 >= sum2 {
			sum2 += weight
		} else {
			sum1 += weight
		}
	}
	return math.Abs(sum1 - sum2)
}

func createItemListLognormal(noItems int, name string) []Item {
	rand.Seed(time.Now().UnixNano())
	items := make([]Item, noItems)
	for i := 0; i < noItems; i++ {
		wert := rand.NormFloat64()*1 + 3 // mean 3, std deviation 1 (lognormal parameters)
		// Exponentiate to create lognormal distribution
		lognormalWert := math.Exp(wert)
		items[i] = Item{
			Name: fmt.Sprintf("LOGNORM%d", i),
			Wert: lognormalWert,
		}
	}
	return items
}

func main() {
	durchlaeufe := 1
	var noItems int
	for dulf := 0; dulf < durchlaeufe; dulf++ {
		name := fmt.Sprintf("lognormal_dist_realisation %d", dulf+1)
		timestart := time.Now()
		noItems = 30
		items := createItemListLognormal(noItems, name)
		weights := make([]float64, len(items))
		for i, item := range items {
			weights[i] = item.Wert
		}
		bestOvf := 0.0
		for i := range weights {
			bestOvf += weights[i]
		}
		//fmt.Println(bestOvf)
		splits := runtime.NumCPU()

		//    set number of threads to use manually
		splits = 4
		//fmt.Println(noItems)
		increment := int(math.Pow(2, float64(noItems))) / 2 / splits
		//fmt.Println(increment)
		start := 0

		ch := make(chan float64)
		for i := 0; i < splits; i++ {
			if i != splits-1 {
				stop := start + increment
				fmt.Println(start, stop)
				go createSolutions(start, stop, weights, ch)
				start = stop
			} else {
				stop := int(math.Pow(2, float64(noItems))) / 2
				fmt.Println(start, stop)
				go createSolutions(start, stop, weights, ch)
			}

		}
		ovf_arr := make([]float64, splits)
		for i := 0; i < splits; i++ {
			ovf_arr[i] = <-ch
			fmt.Println(ovf_arr[i])
		}

		fmt.Println(name)

		fmt.Printf("sum : %f\n", bestOvf)
		fmt.Printf("duration : %.2f sec\n", time.Since(timestart).Seconds())

		stheu := startHeuristic(weights)
		fmt.Printf("heuristic : %f\n", stheu)
	}
}
