package pack1;

import com.gurobi.gurobi.*;

import java.util.Arrays;

public class KnapsackProblem 
{
	private static class Item
	{
		private String name;
		private double profit;
		private double weight;
		
		public Item(String name, double weight, double profit)
		{
			this.name = name;
			this.weight = weight;
			this.profit = profit;
		}
		
		public String useless() {
			return null;
		}
		
		
		
		public String getName()
		{
			return this.name;
		}
		
		public double getWeight()
		{
			return this.weight;
		}
		
		public double getProfit()
		{
			return this.profit;
		}
	}
	
	private static double ov(boolean[] solution)
	{
		double result = 0;
		for (int i = 0; i < N; i++)
		{
			if (solution[i])
			{
				result += ITEMS[i].getProfit();
			}
		}
		return result;
	}
	
	private static GRBEnv env;
	
//	private static final Item[] ITEMS = 
//	{
//		new Item("gooseberries", 3, 30),
//		new Item("cheese", 5, 70),
//		new Item("poppy", 6, 80),
//		new Item("bread", 5, 20),
//		new Item("wine", 7, 90)
//	};
	
//	private static final Item[] ITEMS = 
//	{
//		new Item("0", 1, 100),
//		new Item("1", 2, 90),
//		new Item("2", 3, 80),
//		new Item("3", 4, 70),
//		new Item("4", 5, 60),
//		new Item("5", 6, 50),
//		new Item("6", 7, 40),
//		new Item("7", 8, 30),
//		new Item("8", 9, 20),
//		new Item("9", 10, 10)
//	};

//	private static final Item[] ITEMS = 
//	{
//		new Item("0", 87, 17),
//		new Item("1", 20, 18),
//		new Item("2", 18, 5),
//		new Item("3", 3, 18),
//		new Item("4", 14, 6),
//		new Item("5", 94, 3),
//		new Item("6", 49, 4),
//		new Item("7", 41, 20),
//		new Item("8", 36, 19),
//		new Item("9", 55, 17),
//		new Item("10", 24, 16),
//		new Item("11", 60, 6),
//		new Item("12", 46, 8),
//		new Item("13", 6, 3),
//		new Item("14", 99, 19)
//	};

//	private static final Item[] ITEMS = 
//	{
//		new Item("0", 39, 6),
//		new Item("1", 74, 20),
//		new Item("2", 99, 17),
//		new Item("3", 57, 14),
//		new Item("4", 81, 5),
//		new Item("5", 99, 5),
//		new Item("6", 1, 11),
//		new Item("7", 22, 18),
//		new Item("8", 63, 20),
//		new Item("9", 16, 4),
//		new Item("10", 40, 11),
//		new Item("11", 58, 13),
//		new Item("12", 36, 3),
//		new Item("13", 42, 7),
//		new Item("14", 88, 9),
//		new Item("15", 67, 13),
//		new Item("16", 67, 9),
//		new Item("17", 40, 20),
//		new Item("18", 55, 8),
//		new Item("19", 47, 10)
//	};

//	private static final Item[] ITEMS = 
//	{
//		new Item("0", 31, 4),
//		new Item("1", 81, 18),
//		new Item("2", 72, 19),
//		new Item("3", 22, 18),
//		new Item("4", 59, 5),
//		new Item("5", 78, 4),
//		new Item("6", 85, 3),
//		new Item("7", 23, 6),
//		new Item("8", 94, 8),
//		new Item("9", 81, 10),
//		new Item("10", 42, 15),
//		new Item("11", 1, 14),
//		new Item("12", 47, 18),
//		new Item("13", 13, 17),
//		new Item("14", 90, 7),
//		new Item("15", 95, 1),
//		new Item("16", 55, 15),
//		new Item("17", 92, 17),
//		new Item("18", 97, 9),
//		new Item("19", 35, 2),
//		new Item("20", 15, 7),
//		new Item("21", 45, 12),
//		new Item("22", 6, 16),
//		new Item("23", 8, 10),
//		new Item("24", 2, 15)
//	};

//	private static final Item[] ITEMS = 
//	{
//		new Item("0", 10, 9),
//		new Item("1", 55, 15),
//		new Item("2", 34, 17),
//		new Item("3", 23, 3),
//		new Item("4", 50, 20),
//		new Item("5", 28, 14),
//		new Item("6", 67, 12),
//		new Item("7", 95, 7),
//		new Item("8", 59, 13),
//		new Item("9", 83, 3),
//		new Item("10", 2, 12),
//		new Item("11", 73, 3),
//		new Item("12", 2, 3),
//		new Item("13", 1, 12),
//		new Item("14", 4, 8),
//		new Item("15", 50, 19),
//		new Item("16", 26, 3),
//		new Item("17", 89, 18),
//		new Item("18", 74, 6),
//		new Item("19", 6, 10),
//		new Item("20", 70, 4),
//		new Item("21", 30, 3),
//		new Item("22", 9, 17),
//		new Item("23", 97, 3),
//		new Item("24", 73, 20),
//		new Item("25", 14, 8),
//		new Item("26", 54, 15),
//		new Item("27", 79, 2)
//	};

//	private static final Item[] ITEMS = 
//	{
//		new Item("0", 90, 3),
//		new Item("1", 45, 4),
//		new Item("2", 70, 17),
//		new Item("3", 87, 12),
//		new Item("4", 30, 9),
//		new Item("5", 21, 10),
//		new Item("6", 8, 17),
//		new Item("7", 44, 5),
//		new Item("8", 43, 15),
//		new Item("9", 42, 12),
//		new Item("10", 57, 13),
//		new Item("11", 93, 9),
//		new Item("12", 90, 8),
//		new Item("13", 87, 7),
//		new Item("14", 74, 12),
//		new Item("15", 52, 8),
//		new Item("16", 72, 10),
//		new Item("17", 46, 10),
//		new Item("18", 88, 8),
//		new Item("19", 35, 13),
//		new Item("20", 82, 10),
//		new Item("21", 84, 6),
//		new Item("22", 25, 9),
//		new Item("23", 30, 14),
//		new Item("24", 70, 12),
//		new Item("25", 52, 7),
//		new Item("26", 62, 15),
//		new Item("27", 38, 20),
//		new Item("28", 19, 12),
//		new Item("29", 93, 19)
//	};
	
	private static final Item[] ITEMS = 
	{
		new Item("0", 99, 12),
		new Item("1", 20, 19),
		new Item("2", 48, 15),
		new Item("3", 43, 6),
		new Item("4", 100, 8),
		new Item("5", 67, 11),
		new Item("6", 76, 20),
		new Item("7", 86, 17),
		new Item("8", 20, 11),
		new Item("9", 57, 1),
		new Item("10", 80, 6),
		new Item("11", 37, 18),
		new Item("12", 69, 14),
		new Item("13", 89, 17),
		new Item("14", 19, 19),
		new Item("15", 57, 5),
		new Item("16", 13, 14),
		new Item("17", 96, 4),
		new Item("18", 35, 6),
		new Item("19", 98, 3),
		new Item("20", 31, 8),
		new Item("21", 90, 13),
		new Item("22", 30, 4),
		new Item("23", 80, 7),
		new Item("24", 15, 6),
		new Item("25", 38, 5),
		new Item("26", 63, 4),
		new Item("27", 55, 16),
		new Item("28", 68, 7),
		new Item("29", 62, 8),
		new Item("30", 80, 12),
		new Item("31", 6, 17),
		new Item("32", 50, 18),
		new Item("33", 49, 1),
		new Item("34", 10, 1),
		new Item("35", 39, 9),
		new Item("36", 24, 7),
		new Item("37", 62, 18),
		new Item("38", 45, 1),
		new Item("39", 96, 18),
		new Item("40", 73, 18),
		new Item("41", 54, 18),
		new Item("42", 14, 1),
		new Item("43", 44, 15),
		new Item("44", 82, 12),
		new Item("45", 27, 9),
		new Item("46", 36, 6),
		new Item("47", 76, 13),
		new Item("48", 9, 14),
		new Item("49", 50, 12),
		new Item("50", 90, 17),
		new Item("51", 64, 3),
		new Item("52", 70, 10),
		new Item("53", 33, 13),
		new Item("54", 89, 20),
		new Item("55", 73, 8),
		new Item("56", 75, 10),
		new Item("57", 42, 6),
		new Item("58", 92, 5),
		new Item("59", 97, 6),
		new Item("60", 48, 2),
		new Item("61", 45, 16),
		new Item("62", 27, 12),
		new Item("63", 58, 9),
		new Item("64", 7, 19),
		new Item("65", 4, 8),
		new Item("66", 16, 20),
		new Item("67", 53, 19),
		new Item("68", 79, 4),
		new Item("69", 36, 2),
		new Item("70", 54, 1),
		new Item("71", 41, 5),
		new Item("72", 99, 8),
		new Item("73", 79, 17),
		new Item("74", 25, 20),
		new Item("75", 5, 6),
		new Item("76", 97, 4),
		new Item("77", 22, 18),
		new Item("78", 47, 1),
		new Item("79", 83, 4),
		new Item("80", 15, 16),
		new Item("81", 40, 3),
		new Item("82", 76, 8),
		new Item("83", 4, 18),
		new Item("84", 77, 9),
		new Item("85", 44, 17),
		new Item("86", 92, 19),
		new Item("87", 88, 19),
		new Item("88", 1, 1),
		new Item("89", 8, 12),
		new Item("90", 9, 9),
		new Item("91", 19, 19),
		new Item("92", 1, 13),
		new Item("93", 1, 13),
		new Item("94", 53, 8),
		new Item("95", 88, 20),
		new Item("96", 13, 17),
		new Item("97", 69, 2),
		new Item("98", 51, 8),
		new Item("99", 76, 9)
	};
	
//	private static final double CAPACITY = 10;
//	private static final double CAPACITY = 7;
//	private static final double CAPACITY = 434;
//	private static final double CAPACITY = 726;
//	private static final double CAPACITY = 846;
//	private static final double CAPACITY = 838;
//	private static final double CAPACITY = 1152;
	private static final double CAPACITY = 3428;
	
	private static final int N = ITEMS.length;
	
	private static double GLOBAL_LOWER_BOUND = Double.MIN_VALUE;
	
	private static int numberOfLPsSolvedUsingGurobi = 0;
	
	private static boolean[] greedyStartHeuristic()
	{
		int[] sortedIndicesOfItems = new int[N];
		boolean[] used = new boolean[N];
		Arrays.fill(used, false);
		for (int i = 0; i < N; i++)
		{
			double maxProfitDividedByWeight = Double.MIN_VALUE;
			int indexOfTheItemProvidingMaxProfitDividedByWeight = -1;
			for (int j = 0; j < N; j++)
			{
				if (!used[j] && ITEMS[j].getProfit() / ITEMS[j].getWeight() > maxProfitDividedByWeight)
				{
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
		for (int i = 0; i < N; i++)
		{
			if (ITEMS[sortedIndicesOfItems[i]].getWeight() <= remainingCapacity)
			{
				remainingCapacity -= ITEMS[sortedIndicesOfItems[i]].getWeight();
				solution[sortedIndicesOfItems[i]] = true;
			}
		}
		return solution;
	}
	
	private static boolean[] branchAndBound(boolean[] fixedTo0, boolean[] fixedTo1) throws GRBException
	{
		GRBModel model = new GRBModel(env);
		
		GRBVar[] x = new GRBVar[N];
		for (int i = 0; i < N; i++)
		{
			x[i] = model.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, "x_" + i);
		}
		
		GRBLinExpr expr = new GRBLinExpr();
		for (int i = 0; i < N; i++)
		{
			expr.addTerm(ITEMS[i].getProfit(), x[i]);
		}
		model.setObjective(expr, GRB.MAXIMIZE);
		
		expr = new GRBLinExpr();
		for (int i = 0; i < N; i++)
		{
			expr.addTerm(ITEMS[i].getWeight(), x[i]);
		}
	    model.addConstr(expr, GRB.LESS_EQUAL, CAPACITY, "knapsack_constraint");
	    
	    for (int i = 0; i < N; i++)
	    {
	    	if (fixedTo0[i])
	    	{
				expr = new GRBLinExpr();
				expr.addTerm(1.0, x[i]);
			    model.addConstr(expr, GRB.EQUAL, 0.0, "fixation_to_zero_" + i);
	    	}
	    	if (fixedTo1[i])
	    	{
				expr = new GRBLinExpr();
				expr.addTerm(1.0, x[i]);
			    model.addConstr(expr, GRB.EQUAL, 1.0, "fixation_to_one_" + i);
	    	}
	    }
		
	    numberOfLPsSolvedUsingGurobi++;
	    model.optimize();
	    
		if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL)
		{
			System.out.println("objective function value: " + model.get(GRB.DoubleAttr.ObjVal));
			for (int i = 0; i < N; i++)
			{
				System.out.print(x[i].get(GRB.DoubleAttr.X));
				if (i < N - 1)
				{
					System.out.print(", ");
				}
				else
				{
					System.out.println();
				}
			}
			
			if (model.get(GRB.DoubleAttr.ObjVal) <= GLOBAL_LOWER_BOUND)
			{
				System.out.println("The local upper bound is smaller than or equal to the global lower bound!");
				System.out.println();
				model.dispose();
				return null;
			}
			
			for (int i = 0; i < N; i++)
			{
				if (x[i].get(GRB.DoubleAttr.X) > model.get(GRB.DoubleParam.IntFeasTol) && x[i].get(GRB.DoubleAttr.X) < 1 - model.get(GRB.DoubleParam.IntFeasTol))
				//if (x[i].get(GRB.DoubleAttr.X) >= model.get(GRB.DoubleParam.IntFeasTol) && x[i].get(GRB.DoubleAttr.X) <= 1 - model.get(GRB.DoubleParam.IntFeasTol))
				{
					boolean[] fixedTo0Enlarged = Arrays.copyOf(fixedTo0, N);
					fixedTo0Enlarged[i] = true;
					
					boolean[] fixedTo1Enlarged = Arrays.copyOf(fixedTo1, N);
					fixedTo1Enlarged[i] = true;
					
					System.out.println();
					model.dispose();
					
					boolean[] solutionFixedTo0 = branchAndBound(fixedTo0Enlarged, fixedTo1);
					boolean[] solutionFixedTo1 = branchAndBound(fixedTo0, fixedTo1Enlarged);
					
					if (solutionFixedTo0 == null && solutionFixedTo1 == null)
					{
						return null;
					}
					if (solutionFixedTo0 != null && solutionFixedTo1 == null)
					{
						return solutionFixedTo0;
					}
					if (solutionFixedTo0 == null && solutionFixedTo1 != null)
					{
						return solutionFixedTo1;
					}
					
					if (ov(solutionFixedTo0) > ov(solutionFixedTo1))
					{
						return solutionFixedTo0;
					}
					else
					{
						return solutionFixedTo1;
					}
				}
			}
			
			boolean[] solution = new boolean[N];
			for (int i = 0; i < N; i++)
			{
				if (x[i].get(GRB.DoubleAttr.X) < 0.5)
				{
					solution[i] = false;
				}
				else
				{
					solution[i] = true;
				}
			}
			
			GLOBAL_LOWER_BOUND = model.get(GRB.DoubleAttr.ObjVal);
			System.out.println("new global lower bound: " + GLOBAL_LOWER_BOUND);
			System.out.println();
			model.dispose();
			
			return solution;
		}
		else
		{
			model.dispose();
			return null;
		}
	}
	
	public static void main(String[] args) throws GRBException
	{
//		boolean[] startHeuristicSolution = greedyStartHeuristic();
//		GLOBAL_LOWER_BOUND = ov(startHeuristicSolution);
//		System.out.println("first global lower bound: " + GLOBAL_LOWER_BOUND);
//		System.out.println();
		
		env = new GRBEnv(true);
		env.set("logFile", "LP.log");
		env.set("OutputFlag", "0");
		env.start();
		
		boolean[] solution = branchAndBound(new boolean[N], new boolean[N]);
		if (solution == null)
		{
//			solution = startHeuristicSolution;
		}
		
		env.dispose();
		
		System.out.println("number of LPs solved using Gurobi: " + numberOfLPsSolvedUsingGurobi);
		System.out.println("optimum solution (total profit = " + ov(solution) + "):");
		for (int i = 0; i < N; i++)
		{
			if (solution[i])
			{
				System.out.println("\t" + ITEMS[i].getName());
			}
		}
	}
}
