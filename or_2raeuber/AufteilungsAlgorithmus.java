package or_2raeuber;

import java.util.ArrayList;

import com.gurobi.gurobi.GRB;
import com.gurobi.gurobi.GRBEnv;
import com.gurobi.gurobi.GRBException;
import com.gurobi.gurobi.GRBLinExpr;
import com.gurobi.gurobi.GRBModel;
import com.gurobi.gurobi.GRBVar;

public class AufteilungsAlgorithmus {

	private GRBEnv env; // Gurobi Environment
	private int no_items; // Number of items
	private double[] werte; // Array of item values
	private ArrayList<Item> items = new ArrayList<>(); // Item-List
	private static int numberOfLPsSolvedUsingGurobi; // LP Counter

	// Create new Algorithm with given Beute
	public AufteilungsAlgorithmus(Beute beute) throws GRBException {
		numberOfLPsSolvedUsingGurobi = 0;
		ArrayList<Item> items = beute.getBeute();
		this.items = items; // Item-List
		this.no_items = items.size(); // List size
		this.werte = new double[no_items];
		for (int i = 0; i < no_items; i++) {
			werte[i] = items.get(i).getWert(); // Item values
		}
	}
	
	// solve Problem of saved Beute
	public double[] solve() throws GRBException {
		modelSetup();
		double[] solution = branchAndBound(new boolean[no_items], new boolean[no_items]);
		System.out.println(numberOfLPsSolvedUsingGurobi);
		return solution;
	}
	
	// set up Model
	public void modelSetup() throws GRBException {
		this.env = new GRBEnv(true); // New Gurobi enviroment
		env.set("logFile", "LP.log");
		env.set("OutputFlag", "0");
		env.start();
	}

	// Branch and Bound algorithm 
	private double[] branchAndBound(boolean[] fixedTo0, boolean[] fixedTo1) throws GRBException {
		GRBModel model = new GRBModel(env);
		GRBVar[] x = new GRBVar[no_items]; // Array Gurobi variables weights
		for (int i = 0; i < no_items; i++) {
			if (fixedTo0[i]) {
				x[i] = model.addVar(0.0, 0.0, 0.0, GRB.CONTINUOUS, "x_" + i);
			} else if (fixedTo1[i]) {
				x[i] = model.addVar(1.0, 1.0, 0.0, GRB.CONTINUOUS, "x_" + i);
			} else {
				x[i] = model.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, "x_" + i);
			}
		}
		// Set up LP and boundaries
		GRBVar[] eins = new GRBVar[1]; // Constant Variable
		eins[0] = model.addVar(-1.0, -1.0, -1.0, GRB.CONTINUOUS, "eins"); // Set Constant to minus one
		GRBLinExpr expr = new GRBLinExpr(); // New expression
		for (int i = 0; i < no_items; i++) { // Create expression a + b
			expr.addTerm(items.get(i).getWert() * 2, x[i]); // a = w*2*x(i)
			expr.addTerm(items.get(i).getWert(), eins[0]); // b = w*1
		}
		GRBVar exprvar = model.addVar(-GRB.INFINITY, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "exprvar"); // Variable
																									// expression
		GRBVar absexpr = model.addVar(0.0, GRB.INFINITY, 1.0, GRB.CONTINUOUS, "absexpr"); // Variable absolute
																							// expression
		model.addConstr(exprvar, GRB.EQUAL, expr, "expr constraint"); // Variable == expression
		model.addGenConstrAbs(absexpr, exprvar, "abs_const"); // Absolute Variable == abs(Variable)
		GRBLinExpr expr_fin = new GRBLinExpr(); // Final expression
		expr_fin.addTerm(1, absexpr); // Absolute Variable *1
		model.setObjective(expr_fin, GRB.MINIMIZE); // Minimize final expression

		// optimize
		model.optimize();
		numberOfLPsSolvedUsingGurobi++;		

		double[] result = new double[no_items];

		// Check if Gurobi found a solution
		if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL) {
			// check the solution for each item
			for (int i = 0; i < no_items; i++) {
				if (x[i].get(GRB.DoubleAttr.X) > model.get(GRB.DoubleParam.IntFeasTol)
						&& x[i].get(GRB.DoubleAttr.X) < 1 - model.get(GRB.DoubleParam.IntFeasTol)) {
					double[] solution0;
					double[] solution1;
					// Fix first Variable only too zero (half tree)	
					if (numberOfLPsSolvedUsingGurobi <= 1) {
						fixedTo0[i] = true;
						solution0 = branchAndBound(fixedTo0, fixedTo1); // Get solution for fixed to zero
						fixedTo0[i] = false; // Un-fix it from zero
						return solution0;
					} else {
						fixedTo0[i] = true;
						solution0 = branchAndBound(fixedTo0, fixedTo1);
						fixedTo0[i] = false;

						fixedTo1[i] = true;
						solution1 = branchAndBound(fixedTo0, fixedTo1);
						fixedTo1[i] = false;
					}
					// Get valueFunction for both Solutions
					double ov0 = valueFunction(solution0);
					double ov1 = valueFunction(solution1);
					if (Math.abs(ov0) < Math.abs(ov1)) // compare solutions, return better one
					{
						model.dispose();
						return solution0;
					} else {
						model.dispose();
						return solution1;
					}
				}
			}

			for (int i = 0; i < no_items; i++) {
				result[i] = x[i].get(GRB.DoubleAttr.X);
			}

		} else {
			System.out.println("No optimum solution found.");
		}
		model.dispose();
		return result;
	}

	// Get ValueFunction of Solution
	public double valueFunction(double[] aufteilung) {
		double vf = 0;
		for (int i = 0; i < no_items; i++) {
			vf += (2 * aufteilung[i] - 1) * werte[i];
		}
		return vf;
	}
	
	// Get Number of Solved Gurobi LPs
	public int getNumbSolvedGurobi() {
		return numberOfLPsSolvedUsingGurobi;
	}
	
	// Get Sum of Value what both Raubers get
	public double[] getRauberWert(double[] aufteilung){
		double[] summen = new double[2];
		int s = 0;
		double sum0 = 0;
		double sum1 = 0;
		for(Item i: items) {
			if(aufteilung[s]==0) {
				sum0+=i.getWert();
			} else {
				sum1+=i.getWert();
			}
			s++;
		}
		summen[0] = sum0;
		summen[1] = sum1;
		return summen;
		
	}

	// simple Startheuristic
	public double startHeuristic(ArrayList<Item> items) {
		double sum1 = 0.0;
		double sum2 = 0.0;
		for (int i = 0; i < items.size(); i++) {
			if (sum1 >= sum2) {
				sum2 += items.get(i).getWert();
			} else {
				sum1 += items.get(i).getWert();
			}
		}

		return Math.abs(sum2 - sum1);
	}

}
