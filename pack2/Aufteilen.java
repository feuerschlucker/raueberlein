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
		int no_items = 20;
		
		Beute beute = new Beute(no_items);
		ArrayList<Item> items = beute.getBeute();
		
		
		
		Aufteilen aufteilen = new Aufteilen();
		
		

		

	}

}
