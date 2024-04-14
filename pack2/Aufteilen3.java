package pack2;

import java.util.ArrayList;

import com.gurobi.gurobi.GRB;
import com.gurobi.gurobi.GRBEnv;
import com.gurobi.gurobi.GRBException;
import com.gurobi.gurobi.GRBLinExpr;
import com.gurobi.gurobi.GRBModel;
import com.gurobi.gurobi.GRBVar;

public class Aufteilen3 {

	// Neues Kommentar
	private GRBEnv env;
	private int no_items;
	private double[] werte;
	private ArrayList<Item> items = new ArrayList<>();
	private static int numberOfLPsSolvedUsingGurobi;
	private double startbound;

	public Aufteilen3(ArrayList<Item> items) throws GRBException {
		this.items = items;
		this.no_items = items.size();
		this.werte = new double[no_items];
		for (int i = 0; i < no_items; i++) {
			werte[i] = items.get(i).getWert();
		}
	}

	public void modelSetup(double startbound) throws GRBException {
		this.startbound = startbound;
		this.env = new GRBEnv(true);
		env.set("logFile", "LP.log");
		env.set("OutputFlag", "0");
		env.start();
		// int N = 21;
	}
	// set up Model

	private double[] branchAndBound(boolean[] fixedTo0, boolean[] fixedTo1) throws GRBException {
		GRBModel model = new GRBModel(env);
		GRBVar[] x = new GRBVar[no_items];
		for (int i = 0; i < no_items; i++) {
			if (fixedTo0[i]) {
				x[i] = model.addVar(0.0, 0.0, 0.0, GRB.CONTINUOUS, "x_" + i);
			} else if (fixedTo1[i]) {
				x[i] = model.addVar(1.0, 1.0, 0.0, GRB.CONTINUOUS, "x_" + i);
			} else {
				x[i] = model.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, "x_" + i);
			}
		}
		GRBVar[] eins = new GRBVar[1];
		eins[0] = model.addVar(-1.0, -1.0, -1.0, GRB.CONTINUOUS, "eins");
		GRBLinExpr expr = new GRBLinExpr();
		for (int i = 0; i < no_items; i++) {
			expr.addTerm(items.get(i).getWert() * 2, x[i]);
			expr.addTerm(items.get(i).getWert(), eins[0]);
		}
		GRBVar exprvar = model.addVar(-GRB.INFINITY, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "exprvar");
		GRBVar absexpr = model.addVar(0.0, GRB.INFINITY, 1.0, GRB.CONTINUOUS, "absexpr");
		model.addConstr(exprvar, GRB.EQUAL, expr, "expr constraint");
		model.addGenConstrAbs(absexpr, exprvar, "abs_const");
		GRBLinExpr expr_fin = new GRBLinExpr();
		expr_fin.addTerm(1, absexpr);
		model.setObjective(expr_fin, GRB.MINIMIZE);
		model.optimize();
		numberOfLPsSolvedUsingGurobi++;
		System.out.println(numberOfLPsSolvedUsingGurobi);

		double[] result = new double[no_items];

		if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL) {
			System.out.println("Objective function value: " + model.get(GRB.DoubleAttr.ObjVal));
			for (int i = 0; i < x.length; i++) {
				System.out.print(x[i].get(GRB.StringAttr.VarName) + " = " + x[i].get(GRB.DoubleAttr.X) + ", ");
			}
			System.out.println("");
			
			for (int i = 0; i < no_items; i++)
			{
				//System.out.println("i= "+i+" "+x[i].get(GRB.DoubleAttr.X)+ "  "+model.get(GRB.DoubleParam.IntFeasTol));
				if (x[i].get(GRB.DoubleAttr.X) > model.get(GRB.DoubleParam.IntFeasTol) && x[i].get(GRB.DoubleAttr.X) < 1 - model.get(GRB.DoubleParam.IntFeasTol))
					
				{
					fixedTo0[i] = true;
					double[] checkbound = new double[no_items];
					for (int j = 0;j<no_items;j++) {
						if (j != i) {
							checkbound[j] = x[j].get(GRB.DoubleAttr.X);
						}else{
							checkbound[j] =0;
						};
					}
					double[] solution0;
					if (Math.abs(valueFunction(checkbound))< startbound) {
						startbound = Math.abs(valueFunction(checkbound));
						solution0 = branchAndBound(fixedTo0, fixedTo1);
						fixedTo0[i] = false;
					}else{
						solution0 = null;
					}


					fixedTo1[i] = true;
					double[] solution1 = branchAndBound(fixedTo0, fixedTo1);
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
						double ov0 = valueFunction(solution0);
						double ov1 = valueFunction(solution1);
						if (Math.abs(ov0) < Math.abs(ov1))
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
			
			for (int i = 0; i < no_items; i++)
			{			
				result[i]=  x[i].get(GRB.DoubleAttr.X);	
			}
			
		} else {
			System.out.println("No optimum solution found.");
		}
		model.dispose();
		return result;

	}

	public double valueFunction(double[] aufteilung) {
		double vf = 0;
		for (int i = 0; i < no_items; i++) {
			vf += (2 * aufteilung[i] - 1) * werte[i];
		}
		return vf;
	}
	
	
	public double startHeuristic(ArrayList<Item> items) {
		double sum1 = 0.0;
		double sum2 =0.0;
		for (int i = 0; i<items.size();i++) {
			if(items.get(i).getWert()+sum1 >=sum2) {
				sum2 += items.get(i).getWert();
			}else {
				sum1+=items.get(i).getWert();
			}	
		}
		
		return Math.abs(sum2-sum1);
	}

	public double xxxBound(ArrayList<Item> items) {

		return 0.0;
	}

	public static void main(String[] args) throws GRBException {
		int no_items = 6;

		Beute beute = new Beute(no_items);
		ArrayList<Item> items = beute.getBeute();

		beute.sortItemsByWert();

		for (Item item : items) {
			System.out.println(item.getWert() + "   " + item.getBezeichnung());
		}

		Aufteilen3 auft = new Aufteilen3(items);
		double startbound = auft.startHeuristic(items);
		
		auft.modelSetup(startbound);

		System.out.println(startbound);
		
		double[] solution = auft.branchAndBound(new boolean[no_items], new boolean[no_items]);
		
		System.out.println(solution);

		System.out.println("optimum solution (Differenz = " + auft.valueFunction((solution)) + "):");
		for (int i = 0; i < no_items; i++) {
			System.out.println(solution[i]);
			if (solution[i] == 0) {
				System.out.println("\t" + items.get(i).getBezeichnung() + "  Raeuber 1");
			} else {
				System.out.println("\t" + items.get(i).getBezeichnung() + "Raeuber 2");
			}
		}

		// System.out.print(auft.valueFunction(aufteilung));

	}

}
