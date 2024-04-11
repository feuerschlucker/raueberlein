package pack1;
import com.gurobi.gurobi.GRB;
import com.gurobi.gurobi.GRBEnv;
import com.gurobi.gurobi.GRBException;
import com.gurobi.gurobi.GRBLinExpr;
import com.gurobi.gurobi.GRBModel;
import com.gurobi.gurobi.GRBVar;

public class KP3
{
	private static GRBEnv env;
	
	private static class Item
	{
		private String name;
		private double weight;
		private double profit;
		
		public Item(String name, double weight, double profit)
		{
			this.name = name;
			this.weight = weight;
			this.profit = profit;
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
	
//	private static final Item[] ITEMS = 
//	{
//		new Item("gooseberries", 3, 30),
//		new Item("cheese", 5, 70),
//		new Item("poppy", 6, 80),
//		new Item("bread", 5, 20),
//		new Item("wine", 7, 90)
//	};
	
	private static final Item[] ITEMS = 
	{
		new Item("0", 87, 17),
		new Item("1", 20, 18),
		new Item("2", 18, 5),
		new Item("3", 3, 18),
		new Item("4", 14, 6),
		new Item("5", 94, 3),
		new Item("6", 49, 4),
		new Item("7", 41, 20),
		new Item("8", 36, 19),
		new Item("9", 55, 17),
		new Item("10", 24, 16),
		new Item("11", 60, 6),
		new Item("12", 46, 8),
		new Item("13", 6, 3),
		new Item("14", 99, 19)
	};
	
//	private static final Item[] ITEMS = 
//	{
//		new Item("0", 99, 12),
//		new Item("1", 20, 19),
//		new Item("2", 48, 15),
//		new Item("3", 43, 6),
//		new Item("4", 100, 8),
//		new Item("5", 67, 11),
//		new Item("6", 76, 20),
//		new Item("7", 86, 17),
//		new Item("8", 20, 11),
//		new Item("9", 57, 1),
//		new Item("10", 80, 6),
//		new Item("11", 37, 18),
//		new Item("12", 69, 14),
//		new Item("13", 89, 17),
//		new Item("14", 19, 19),
//		new Item("15", 57, 5),
//		new Item("16", 13, 14),
//		new Item("17", 96, 4),
//		new Item("18", 35, 6),
//		new Item("19", 98, 3),
//		new Item("20", 31, 8),
//		new Item("21", 90, 13),
//		new Item("22", 30, 4),
//		new Item("23", 80, 7),
//		new Item("24", 15, 6),
//		new Item("25", 38, 5),
//		new Item("26", 63, 4),
//		new Item("27", 55, 16),
//		new Item("28", 68, 7),
//		new Item("29", 62, 8),
//		new Item("30", 80, 12),
//		new Item("31", 6, 17),
//		new Item("32", 50, 18),
//		new Item("33", 49, 1),
//		new Item("34", 10, 1),
//		new Item("35", 39, 9),
//		new Item("36", 24, 7),
//		new Item("37", 62, 18),
//		new Item("38", 45, 1),
//		new Item("39", 96, 18),
//		new Item("40", 73, 18),
//		new Item("41", 54, 18),
//		new Item("42", 14, 1),
//		new Item("43", 44, 15),
//		new Item("44", 82, 12),
//		new Item("45", 27, 9),
//		new Item("46", 36, 6),
//		new Item("47", 76, 13),
//		new Item("48", 9, 14),
//		new Item("49", 50, 12),
//		new Item("50", 90, 17),
//		new Item("51", 64, 3),
//		new Item("52", 70, 10),
//		new Item("53", 33, 13),
//		new Item("54", 89, 20),
//		new Item("55", 73, 8),
//		new Item("56", 75, 10),
//		new Item("57", 42, 6),
//		new Item("58", 92, 5),
//		new Item("59", 97, 6),
//		new Item("60", 48, 2),
//		new Item("61", 45, 16),
//		new Item("62", 27, 12),
//		new Item("63", 58, 9),
//		new Item("64", 7, 19),
//		new Item("65", 4, 8),
//		new Item("66", 16, 20),
//		new Item("67", 53, 19),
//		new Item("68", 79, 4),
//		new Item("69", 36, 2),
//		new Item("70", 54, 1),
//		new Item("71", 41, 5),
//		new Item("72", 99, 8),
//		new Item("73", 79, 17),
//		new Item("74", 25, 20),
//		new Item("75", 5, 6),
//		new Item("76", 97, 4),
//		new Item("77", 22, 18),
//		new Item("78", 47, 1),
//		new Item("79", 83, 4),
//		new Item("80", 15, 16),
//		new Item("81", 40, 3),
//		new Item("82", 76, 8),
//		new Item("83", 4, 18),
//		new Item("84", 77, 9),
//		new Item("85", 44, 17),
//		new Item("86", 92, 19),
//		new Item("87", 88, 19),
//		new Item("88", 1, 1),
//		new Item("89", 8, 12),
//		new Item("90", 9, 9),
//		new Item("91", 19, 19),
//		new Item("92", 1, 13),
//		new Item("93", 1, 13),
//		new Item("94", 53, 8),
//		new Item("95", 88, 20),
//		new Item("96", 13, 17),
//		new Item("97", 69, 2),
//		new Item("98", 51, 8),
//		new Item("99", 76, 9)
//	};
	
//	private static final double CAPACITY = 10;
	private static final double CAPACITY = 434;
//	private static final double CAPACITY = 3428;
	
	private static final double EPSILON = 1e-4;
	
	private static final int N = ITEMS.length;
	
	private static int numberOfLPsSolvedUsingGurobi = 0;
	
	private static boolean optimumFound = false;
	private static double globalLowerBound= Double.MIN_VALUE;
	private static double globalUpperBound = Double.MAX_VALUE;
	
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
	
	private static double globalUpperBound1()
	{
		double result = 0;
		for (final Item item : ITEMS)
		{
			result += item.getProfit();
		}
		return result;
	}
	
	
	private static boolean[] branchAndBound(boolean[] fixedTo0, boolean[] fixedTo1) throws GRBException
//	private static boolean[] branchAndBound(boolean[] smallerEqual, Double[] rSSE, boolean[] greaterEqual[], Double[] rSGE)

	{
	    //new model
		GRBModel model = new GRBModel(env);
		
		//two variables in an array; both variables must be non-negative
		GRBVar[] x = new GRBVar[N];
		for (int i = 0; i < N; i++)
		{
			if (fixedTo0[i])
			{
				x[i] = model.addVar(0.0, 0.0, 0.0, GRB.CONTINUOUS, "x_" + i);
			}
			else if (fixedTo1[i])
			{
				x[i] = model.addVar(1.0, 1.0, 0.0, GRB.CONTINUOUS, "x_" + i);
			}
			else
			{
				x[i] = model.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, "x_" + i);
			}
		}
		
		GRBLinExpr expr = new GRBLinExpr();
		for (int i = 0; i < N; i++)
		{
			expr.addTerm(ITEMS[i].getProfit(), x[i]);
		}
	    model.setObjective(expr, GRB.MAXIMIZE);
		
	    //0th constraint (x_0 + 6 x_1 <= 27)
		expr = new GRBLinExpr();
		for (int i = 0; i < N; i++)
		{
			expr.addTerm(ITEMS[i].getWeight(), x[i]);
		}
	    model.addConstr(expr, GRB.LESS_EQUAL, CAPACITY, "st_C");
	    
	    model.optimize();
	    
	    numberOfLPsSolvedUsingGurobi++;
	    System.out.print("iteration " + numberOfLPsSolvedUsingGurobi + ": ");
	    
		boolean[] result = new boolean[N];
		if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL)
		{
			System.out.println("objective function value: " + model.get(GRB.DoubleAttr.ObjVal));
		    System.out.println("\tlower bound = " + globalLowerBound + ", upper bound = " + globalUpperBound);
			System.out.println();
			if (model.get(GRB.DoubleAttr.ObjVal) <= globalLowerBound)
			{
				model.dispose();
				return null;
			}
		    
//			for (int i = 0; i < N; i++)
//			{
//				System.out.print(x[i].get(GRB.StringAttr.VarName) + " = " + x[i].get(GRB.DoubleAttr.X));
//				if (i < x.length - 1)
//				{
//					System.out.print(", ");
//				}
//				else
//				{
//					System.out.println();
//				}
//			}
			
			for (int i = 0; i < N; i++)
			{
				if (x[i].get(GRB.DoubleAttr.X) > model.get(GRB.DoubleParam.IntFeasTol) && x[i].get(GRB.DoubleAttr.X) < 1 - model.get(GRB.DoubleParam.IntFeasTol))
				{
					fixedTo0[i] = true;
					boolean[] solution0 = branchAndBound(fixedTo0, fixedTo1);
					if (optimumFound)
					{
						model.dispose();
						return solution0;
					}
					fixedTo0[i] = false;

					fixedTo1[i] = true;
					boolean[] solution1 = branchAndBound(fixedTo0, fixedTo1);
					if (optimumFound)
					{
						model.dispose();
						return solution1;
					}
					fixedTo1[i] = false;
					
					if (solution0 == null)
					{
						model.dispose();
						return solution1;
					}
					else if (solution1 == null)
					{
						model.dispose();
						return solution0;
					}
					else
					{
						double ov0 = ov(solution0);
						double ov1 = ov(solution1);
						if (ov0 > ov1)
						{
							model.dispose();
							return solution0;
						}
						else
						{
							model.dispose();
							return solution1;
						}
					}
				}
			}
			
			for (int i = 0; i < N; i++)
			{
				if (x[i].get(GRB.DoubleAttr.X) >= 1 - model.get(GRB.DoubleParam.IntFeasTol))
				result[i] = true;
			}
			double ovResult = ov(result);
			if (ovResult > globalLowerBound)
			{
				globalLowerBound = ov(result);
				System.out.println("new lower bound = " + globalLowerBound + ", gap = " + ((globalUpperBound - globalLowerBound) / globalUpperBound));
				if (Math.abs(globalLowerBound - globalUpperBound) < EPSILON)
				{
					optimumFound = true;
				}
			}
		}
		else
		{
			System.out.println("No optimum solution found.");
			System.out.println();
			result = null;
		}
		
		model.dispose();
		return result;
	}
	
	
	
	public static void main(String[] args) throws GRBException
	{
		globalUpperBound = globalUpperBound1();
		System.out.println("upper bound = " + globalUpperBound);
		globalUpperBound = 161.0;
		System.out.println("upper bound = " + globalUpperBound);
		
		
		env = new GRBEnv(true);
		env.set("logFile", "LP.log");
		env.set("OutputFlag", "0");
		env.start();
		
		boolean[] solution = branchAndBound(new boolean[N], new boolean[N]);
		
		env.dispose();
		
		System.out.println("optimum solution (total profit = " + ov(solution) + "):");
		for (int i = 0; i < N; i++)
		{
			if (solution[i])
			{
				System.out.println("\t" + ITEMS[i].getName());
			}
		}
		System.out.println(numberOfLPsSolvedUsingGurobi + " calls of an LP solver were necessary.");
	}
}
