package pack1;
import com.gurobi.gurobi.GRB;
import com.gurobi.gurobi.GRBEnv;
import com.gurobi.gurobi.GRBException;
import com.gurobi.gurobi.GRBLinExpr;
import com.gurobi.gurobi.GRBModel;
import com.gurobi.gurobi.GRBVar;

/**
 * Program solving the following LP using Gurobi:
 * max    x_0 +   x_1
 * s.t.   x_0 + 6 x_1 <= 27
 *      3 x_0 - 2 x_1 <= 15
 *      5 x_0 +   x_1 <= 35
 *           x_0, x_1    non-negative
 * 
 * @author Rostislav Stanek
 *
 */
public class LP3
{
	public static void main(String[] args) throws GRBException
	{
		//new Gurobi environment
	    GRBEnv env = new GRBEnv(true);
	    
	    //log file
	    env.set("logFile", "LP.log");
	    
	    //no console output
	    env.set("OutputFlag", "0");
	    
	    //start of the Gurobi environment
	    env.start();
	    
	    //new model
		GRBModel model = new GRBModel(env);
		
		//two variables in an array; both variables must be non-negative
		GRBVar[] x = new GRBVar[2];
		x[0] = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "x_0");
		x[1] = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "x_1");
		
		//objective function value (max x_0 + x_1)
		GRBLinExpr expr = new GRBLinExpr();
	    expr.addTerm(1.0, x[0]);
	    expr.addTerm(1.0, x[1]);
	    model.setObjective(expr, GRB.MAXIMIZE);
	    
	    //0th constraint (x_0 + 6 x_1 <= 27)
		expr = new GRBLinExpr();
	    expr.addTerm(1.0, x[0]);
	    expr.addTerm(6.0, x[1]);
	    model.addConstr(expr, GRB.LESS_EQUAL, 27.0, "st_0");
	    
	    //1st constraint (3 x_0 - 2 x_1 <= 15)
		expr = new GRBLinExpr();
	    expr.addTerm(3.0, x[0]);
	    expr.addTerm(-2.0, x[1]);
	    model.addConstr(expr, GRB.LESS_EQUAL, 15.0, "st_1");
	    
	    //2st constraint (5 x_0 + x_1 <= 35)
		expr = new GRBLinExpr();
	    expr.addTerm(5.0, x[0]);
	    expr.addTerm(1.0, x[1]);
	    //model.addConstr(expr, GRB.LESS_EQUAL, 35.0, "st_2");
	    
	    //3rd constraint (x_0 <= 6)
		expr = new GRBLinExpr();
	    expr.addTerm(1.0, x[0]);
	    model.addConstr(expr, GRB.LESS_EQUAL, 6.0, "st_3");
	    
	    //4th constraint (x_0 + x_1 <= 9)
		expr = new GRBLinExpr();
	    expr.addTerm(1.0, x[0]);
	    expr.addTerm(1.0, x[1]);
	    //model.addConstr(expr, GRB.LESS_EQUAL, 9.0, "st_4");
	    
	    //start the solver
		model.optimize();
		if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL)
		{
			//print the optimum objective function value
			System.out.println("Objective function value: " + model.get(GRB.DoubleAttr.ObjVal));
			
			//print the optimum variable values
			for (int i = 0; i < x.length; i++)
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
		}
		else
		{
			System.out.println("No optimum solution found.");
		}
	}
}
