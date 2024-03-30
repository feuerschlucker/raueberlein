package pack1;

import com.gurobi.gurobi.*;

import java.util.Arrays;

public class KnapsackProblem {
	private static class Item {
		private String name;
		private double profit;
		private double weight;

		public Item(String name, double weight, double profit) {
			this.name = name;
			this.weight = weight;
			this.profit = profit;
		}

		public String getName() {
			return this.name;
		}

		public double getWeight() {
			return this.weight;
		}

		public double getProfit() {
			return this.profit;
		}
	}

	private static double ov(boolean[] solution) {
		double result = 0;
		for (int i = 0; i < N; i++) {
			if (solution[i]) {
				result += ITEMS[i].getProfit();
			}
		}
		return result;
	}

	private static GRBEnv env;

//	private static final Item[] ITEMS = { new Item("gooseberries", 3, 30), new Item("cheese", 5, 70),
//			new Item("poppy", 6, 80), new Item("bread", 5, 20), new Item("wine", 7, 90) };

	private static final Item[] ITEMS = { new Item("0", 1, 100), new Item("1", 2, 90), new Item("2", 3, 80),
			new Item("3", 4, 70), new Item("4", 5, 60), new Item("5", 6, 50), new Item("6", 7, 40),
			new Item("7", 8, 30), new Item("8", 9, 20), new Item("9", 10, 10) };

//	private static final Item[] ITEMS = { new Item("0", 87, 17), new Item("1", 20, 18), new Item("2", 18, 5),
//			new Item("3", 3, 18), new Item("4", 14, 6), new Item("5", 94, 3), new Item("6", 49, 4),
//			new Item("7", 41, 20), new Item("8", 36, 19), new Item("9", 55, 17), new Item("10", 24, 16),
//			new Item("11", 60, 6), new Item("12", 46, 8), new Item("13", 6, 3), new Item("14", 99, 19) };

//	private static final Item[] ITEMS = { new Item("0", 39, 6), new Item("1", 74, 20), new Item("2", 99, 17),
//			new Item("3", 57, 14), new Item("4", 81, 5), new Item("5", 99, 5), new Item("6", 1, 11),
//			new Item("7", 22, 18), new Item("8", 63, 20), new Item("9", 16, 4), new Item("10", 40, 11),
//			new Item("11", 58, 13), new Item("12", 36, 3), new Item("13", 42, 7), new Item("14", 88, 9),
//			new Item("15", 67, 13), new Item("16", 67, 9), new Item("17", 40, 20), new Item("18", 55, 8),
//			new Item("19", 47, 10) };

//	private static final Item[] ITEMS = { new Item("0", 31, 4), new Item("1", 81, 18), new Item("2", 72, 19),
//			new Item("3", 22, 18), new Item("4", 59, 5), new Item("5", 78, 4), new Item("6", 85, 3),
//			new Item("7", 23, 6), new Item("8", 94, 8), new Item("9", 81, 10), new Item("10", 42, 15),
//			new Item("11", 1, 14), new Item("12", 47, 18), new Item("13", 13, 17), new Item("14", 90, 7),
//			new Item("15", 95, 1), new Item("16", 55, 15), new Item("17", 92, 17), new Item("18", 97, 9),
//			new Item("19", 35, 2), new Item("20", 15, 7), new Item("21", 45, 12), new Item("22", 6, 16),
//			new Item("23", 8, 10), new Item("24", 2, 15) };



//	private static final double CAPACITY = 10;
	private static final double CAPACITY = 7;
//	private static final double CAPACITY = 434;
//	private static final double CAPACITY = 726;
//	private static final double CAPACITY = 846;


	private static final int N = ITEMS.length;

	private static double GLOBAL_LOWER_BOUND = Double.MIN_VALUE;

	private static int numberOfLPsSolvedUsingGurobi = 0;

	private static boolean[] greedyStartHeuristic() {
		int[] sortedIndicesOfItems = new int[N];
		boolean[] used = new boolean[N];
		Arrays.fill(used, false);
		for (int i = 0; i < N; i++) {
			double maxProfitDividedByWeight = Double.MIN_VALUE;
			int indexOfTheItemProvidingMaxProfitDividedByWeight = -1;
			for (int j = 0; j < N; j++) {
				if (!used[j] && ITEMS[j].getProfit() / ITEMS[j].getWeight() > maxProfitDividedByWeight) {
					maxProfitDividedByWeight = ITEMS[j].getProfit() / ITEMS[j].getWeight();
					indexOfTheItemProvidingMaxProfitDividedByWeight = j;
				}
			}
			used[indexOfTheItemProvidingMaxProfitDividedByWeight] = true;
			sortedIndicesOfItems[i] = indexOfTheItemProvidingMaxProfitDividedByWeight;
		}

		boolean[] solution = new boolean[N];
		Arrays.fill(solution, false);
		double remainingCapacity = CAPACITY;
		for (int i = 0; i < N; i++) {
			if (ITEMS[sortedIndicesOfItems[i]].getWeight() <= remainingCapacity) {
				remainingCapacity -= ITEMS[sortedIndicesOfItems[i]].getWeight();
				solution[sortedIndicesOfItems[i]] = true;
			}
		}
		return solution;
	}

	private static boolean[] branchAndBound(boolean[] fixedTo0, boolean[] fixedTo1) throws GRBException {
		GRBModel model = new GRBModel(env);

		GRBVar[] x = new GRBVar[N];
		for (int i = 0; i < N; i++) {
			x[i] = model.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, "x_" + i);
		}

		GRBLinExpr expr = new GRBLinExpr();
		for (int i = 0; i < N; i++) {
			expr.addTerm(ITEMS[i].getProfit(), x[i]);
		}
		model.setObjective(expr, GRB.MAXIMIZE);

		expr = new GRBLinExpr();
		for (int i = 0; i < N; i++) {
			expr.addTerm(ITEMS[i].getWeight(), x[i]);
		}
		model.addConstr(expr, GRB.LESS_EQUAL, CAPACITY, "knapsack_constraint");

		for (int i = 0; i < N; i++) {
			if (fixedTo0[i]) {
				expr = new GRBLinExpr();
				expr.addTerm(1.0, x[i]);
				model.addConstr(expr, GRB.EQUAL, 0.0, "fixation_to_zero_" + i);
			}
			if (fixedTo1[i]) {
				expr = new GRBLinExpr();
				expr.addTerm(1.0, x[i]);
				model.addConstr(expr, GRB.EQUAL, 1.0, "fixation_to_one_" + i);
			}
		}

		numberOfLPsSolvedUsingGurobi++;
		model.optimize();

		if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL) {
			System.out.println("objective function value: " + model.get(GRB.DoubleAttr.ObjVal));
			for (int i = 0; i < N; i++) {
				System.out.print(x[i].get(GRB.DoubleAttr.X));
				if (i < N - 1) {
					System.out.print(", ");
				} else {
					System.out.println();
				}
			}

			if (model.get(GRB.DoubleAttr.ObjVal) <= GLOBAL_LOWER_BOUND) {
				System.out.println("The local upper bound is smaller than or equal to the global lower bound!");
				System.out.println();
				model.dispose();
				return null;
			}

			for (int i = 0; i < N; i++) {
				if (x[i].get(GRB.DoubleAttr.X) > model.get(GRB.DoubleParam.IntFeasTol)
						&& x[i].get(GRB.DoubleAttr.X) < 1 - model.get(GRB.DoubleParam.IntFeasTol))
				// if (x[i].get(GRB.DoubleAttr.X) >= model.get(GRB.DoubleParam.IntFeasTol) &&
				// x[i].get(GRB.DoubleAttr.X) <= 1 - model.get(GRB.DoubleParam.IntFeasTol))
				{
					boolean[] fixedTo0Enlarged = Arrays.copyOf(fixedTo0, N);
					fixedTo0Enlarged[i] = true;

					boolean[] fixedTo1Enlarged = Arrays.copyOf(fixedTo1, N);
					fixedTo1Enlarged[i] = true;

					System.out.println();
					model.dispose();

					boolean[] solutionFixedTo0 = branchAndBound(fixedTo0Enlarged, fixedTo1);
					boolean[] solutionFixedTo1 = branchAndBound(fixedTo0, fixedTo1Enlarged);

					if (solutionFixedTo0 == null && solutionFixedTo1 == null) {
						return null;
					}
					if (solutionFixedTo0 != null && solutionFixedTo1 == null) {
						return solutionFixedTo0;
					}
					if (solutionFixedTo0 == null && solutionFixedTo1 != null) {
						return solutionFixedTo1;
					}

					if (ov(solutionFixedTo0) > ov(solutionFixedTo1)) {
						return solutionFixedTo0;
					} else {
						return solutionFixedTo1;
					}
				}
			}

			boolean[] solution = new boolean[N];
			for (int i = 0; i < N; i++) {
				if (x[i].get(GRB.DoubleAttr.X) < 0.5) {
					solution[i] = false;
				} else {
					solution[i] = true;
				}
			}

			GLOBAL_LOWER_BOUND = model.get(GRB.DoubleAttr.ObjVal);
			System.out.println("new global lower bound: " + GLOBAL_LOWER_BOUND);
			System.out.println();
			model.dispose();

			return solution;
		} else {
			model.dispose();
			return null;
		}
	}

	public static void main(String[] args) throws GRBException {
//		boolean[] startHeuristicSolution = greedyStartHeuristic();
//		GLOBAL_LOWER_BOUND = ov(startHeuristicSolution);
//		System.out.println("first global lower bound: " + GLOBAL_LOWER_BOUND);
//		System.out.println();

		env = new GRBEnv(true);
		env.set("logFile", "LP.log");
		env.set("OutputFlag", "0");
		env.start();

		boolean[] solution = branchAndBound(new boolean[N], new boolean[N]);
		if (solution == null) {
//			solution = startHeuristicSolution;
		}

		env.dispose();

		System.out.println("number of LPs solved using Gurobi: " + numberOfLPsSolvedUsingGurobi);
		System.out.println("optimum solution (total profit = " + ov(solution) + "):");
		for (int i = 0; i < N; i++) {
			if (solution[i]) {
				System.out.println("\t" + ITEMS[i].getName());
			}
		}
	}
}
