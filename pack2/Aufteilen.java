package pack2;

import java.util.*;
import com.gurobi.gurobi.*;

public class Aufteilen {



	public Aufteilen() throws GRBException {

		GRBEnv env = new GRBEnv(true);
		env.set("logFile", "LP.log");
		env.set("OutputFlag", "0");
		env.start();
	}


		

	

	public static void main(String[] args) throws GRBException {

		Aufteilen aft = new Aufteilen();
		int no_items = 20;
		

		

	}

}
