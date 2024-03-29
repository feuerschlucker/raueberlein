package pack1;

import com.gurobi.gurobi.*;

public class KP {
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
		new Item("gooseberries", 3, 30),
		new Item("cheese", 5, 70),
		new Item("poppy", 6, 80),
		new Item("bread", 5, 20),
		new Item("wine", 7, 90)
	};
	
	private static final double CAPACITY = 10;
	
	private static final int N = ITEMS.length;
	
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
	}
}
