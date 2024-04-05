package pack2;

import java.util.ArrayList;

import com.gurobi.gurobi.GRB;
import com.gurobi.gurobi.GRBEnv;
import com.gurobi.gurobi.GRBException;
import com.gurobi.gurobi.GRBLinExpr;
import com.gurobi.gurobi.GRBModel;
import com.gurobi.gurobi.GRBVar;
import com.gurobi.gurobi.*;

public class Aufteilen {

	private GRBEnv env;
	private int no_items;
	private double[] werte;
	private ArrayList<Item> items = new ArrayList<>();

	public Aufteilen(ArrayList<Item> items) throws GRBException {
		this.items = items;
		this.no_items = items.size();
		this.werte = new double[no_items];
		for (int i = 0; i< no_items;i++) {
			werte[i] = items.get(i).getWert();
		}
		
	}

	public void modelSetup() throws GRBException {
		this.env = new GRBEnv(true);
		env.set("logFile", "LP.log");
		env.set("OutputFlag", "0");
		env.start();
		int N = 21;
		
		//soem stuff
		GRBModel model = new GRBModel(env);
		GRBVar[] x = new GRBVar[N];
		for (int i = 0; i < N; i++) {
			x[i] = model.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, "x_" + i);
		}
		GRBVar[] eins = new GRBVar[1];
		eins[0]= model.addVar(-1.0, -1.0, -1.0, GRB.CONTINUOUS, "eins");
		
		
		GRBLinExpr expr = new GRBLinExpr();
		for (int i = 0; i < N; i++) {
			expr.addTerm(items.get(i).getWert()*2, x[i]);
			expr.addTerm(items.get(i).getWert(), eins[0]);	
		}
		
		GRBVar exprvar = model.addVar(-GRB.INFINITY, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "exprvar");
		GRBVar absexpr = model.addVar(0.0, GRB.INFINITY, 1.0, GRB.CONTINUOUS, "absexpr");
		
		model.addConstr(exprvar, GRB.EQUAL, expr, "expr constraint");
		
		model.addGenConstrAbs(absexpr,exprvar,"abs_const");
		GRBLinExpr expr_fin = new GRBLinExpr();
		
		expr_fin.addTerm(1, absexpr);
	    model.setObjective(expr_fin, GRB.MINIMIZE);
	    
	    
		
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

	public double valueFunction(double[] aufteilung) {
		double vf = 0;
		for (int i = 0; i<no_items;i++) {
			vf += (2 * aufteilung[i] - 1)*werte[i];
		}
		return vf;
	}
	
	
	

	public static void main(String[] args) throws GRBException {
		int no_items = 21;

		Beute beute = new Beute(no_items);
		ArrayList<Item> items = beute.getBeute();

		for (Item item : items) {
			System.out.println(item.getWert() + "   " + item.getBezeichnung());
		}
		double[] aufteilung = new double[no_items];
		
		for (int i = 0;i<no_items;i++) {
			aufteilung[i]=0.5;
		}
		


		Aufteilen auft = new Aufteilen(items);
		auft.modelSetup();

		System.out.print(auft.valueFunction(aufteilung));

	}

}
