package pack2;

import java.util.ArrayList;

import com.gurobi.gurobi.GRB;
import com.gurobi.gurobi.GRBEnv;
import com.gurobi.gurobi.GRBException;
import com.gurobi.gurobi.GRBModel;
import com.gurobi.gurobi.GRBVar;

public class Aufteilen {

	private GRBEnv env;
	private int no_items;
	private double[] werte;

	public Aufteilen(ArrayList<Item> items) throws GRBException {
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
		int N = 20;
		
		//soem stuff
		GRBModel model = new GRBModel(env);
		GRBVar[] x = new GRBVar[N];
		for (int i = 0; i < N; i++) {
			x[i] = model.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, "x_" + i);
		}

	}

	public double valueFunction(double[] aufteilung) {
		double vf = 0;
		for (int i = 0; i<no_items;i++) {
			vf += (2 * aufteilung[i]*werte[i] - 1);
		}
		return vf;
	}
	
	
	

	public static void main(String[] args) throws GRBException {
		int no_items = 20;

		Beute beute = new Beute(no_items);
		ArrayList<Item> items = beute.getBeute();

		for (Item item : items) {
			System.out.println(item.getWert() + "   " + item.getBezeichnung());
		}
		double[] aufteilung = new double[no_items];
		


		Aufteilen auft = new Aufteilen(items);

		System.out.print(auft.valueFunction(aufteilung));

	}

}
