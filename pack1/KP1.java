package pack1;
import com.gurobi.gurobi.*;

public class KP1
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
	
	private static final double CAPACITY = 434;
	private static final int N = ITEMS.length;
	private static int numberOfLPsSolvedUsingGurobi = 0;
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
	
	private static boolean[] branchAndBound(boolean[] fixedTo0, boolean[] fixedTo1) throws GRBException
	{
		GRBModel model = new GRBModel(env);
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
		
		expr = new GRBLinExpr();
		for (int i = 0; i < N; i++)
		{
			expr.addTerm(ITEMS[i].getWeight(), x[i]);
		}
	    model.addConstr(expr, GRB.LESS_EQUAL, CAPACITY, "st_C");
	    model.optimize();
	    numberOfLPsSolvedUsingGurobi++;
	    
		boolean[] result = new boolean[N];
		if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL)
		{
			for (int i = 0; i < N; i++)
			{
				System.out.print(x[i].get(GRB.StringAttr.VarName) + " = " + x[i].get(GRB.DoubleAttr.X));
				if (i < x.length - 1)
				{
					System.out.print(", ");
				}
				else
				{
					System.out.println();
				}
			}
			
			for (int i = 0; i < N; i++)
			{
				if (x[i].get(GRB.DoubleAttr.X) > model.get(GRB.DoubleParam.IntFeasTol) && x[i].get(GRB.DoubleAttr.X) < 1 - model.get(GRB.DoubleParam.IntFeasTol))
				{
					fixedTo0[i] = true;
					boolean[] solution0 = branchAndBound(fixedTo0, fixedTo1);
					fixedTo0[i] = false;

					fixedTo1[i] = true;
					boolean[] solution1 = branchAndBound(fixedTo0, fixedTo1);
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
		}
		else
		{
			result = null;
		}
		
		model.dispose();
		return result;
	}
	
	
	
	public static void main(String[] args) throws GRBException
	{
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
