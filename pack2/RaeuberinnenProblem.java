package pack2;

import java.util.*;

import com.gurobi.gurobi.GRBException;

public class RaeuberinnenProblem {
	
	public static void randomBeute(int no_items) {
		Beute beute = new Beute(no_items);
		beute.saveBeute("RandomBeute_"+no_items, true);
		try {
			AufteilungsAlgorithmus auft = new AufteilungsAlgorithmus(beute);
			auft.modelSetup();
		} catch (GRBException e) {
			System.out.println("Error in Gurobi !"); 
			e.printStackTrace();
		}
		
		
	}
	
	public static void csvBeute(ArrayList<String> csvList, String delimeter) {
		
		
	}
	
	private void saveSolution() { 
		
		
	}
	
	
	 public static void main(String[] args) {
		
		
	}
	

}
