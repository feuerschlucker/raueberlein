package pack2;

import java.io.*;
import java.util.*;

import com.gurobi.gurobi.GRBException;

public class RaeuberinnenProblem {
	// Solve RauberInnen Problem of random Beute
	public static void randomBeute(int no_items) {
		Beute b = new Beute(no_items);
		b.saveBeute("RandomBeute_"+no_items, true);
		getResults(b);
	}
	// Solve RauberInnen Problem of given Beute out of csv
	public static void csvBeute(String csvFile, String delimiter, Boolean header, String csvFilePath) {
		Beute b = new Beute (csvFile, delimiter, header, csvFilePath);
		getResults(b);
	}
	
	// Solve and Save Problem of given Beute
	private static void getResults(Beute beute) {
		try {
			AufteilungsAlgorithmus auft = new AufteilungsAlgorithmus(beute);
			long startTime = System.nanoTime();
			double [] solution = auft.solve();
			long endTime = System.nanoTime();
			long elapsedTime = endTime - startTime;
			double elapsedTimeInSecs = elapsedTime / 1_000_000_000.0;
			saveSolution(beute,auft,solution);
			saveStats(beute,auft,solution,elapsedTimeInSecs);
		} catch (GRBException e) {
			System.out.println("Error in Gurobi !"); 
			e.printStackTrace();
		}
	}
	
	// Save Statistics of given Probelm / Beute
	private static void saveStats(Beute b, AufteilungsAlgorithmus a, double[] sol, double time) {
		try {
			File csvFile = new File(b.getFilename()+"_Stats"+".csv");
			BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFile));
			csvWriter.write(String.join(",","Number Items",String.valueOf(b.getBeuteSize())));
			csvWriter.newLine();
			csvWriter.write(String.join(",","Beute Value",String.valueOf(b.getBeuteValue())));
			csvWriter.newLine();
			double[] summen = a.getRauberWert(sol);
			csvWriter.write(String.join(",","Räuber 1",String.valueOf(summen[0])));
			csvWriter.newLine();
			csvWriter.write(String.join(",","Räuber 2",String.valueOf(summen[1])));
			csvWriter.newLine();
			csvWriter.write(String.join(",","Value Function",String.valueOf(Math.abs(a.valueFunction(sol)))));
			csvWriter.newLine();
			csvWriter.write(String.join(",","Iterations needed",String.valueOf(AufteilungsAlgorithmus.numberOfLPsSolvedUsingGurobi)));
			csvWriter.newLine();
			csvWriter.write(String.join(",","Time needed [s]",String.valueOf(time)));
			csvWriter.newLine();
			
			// Close Writer
			csvWriter.close();
			System.out.println("Saved Statisitcs!");
			
		} catch (IOException e) {
			System.out.println("Error in CsvBufferWriter !"); 
    		e.printStackTrace();
		}
	}

	// Save Solution of given Beute
	private static void saveSolution(Beute b, AufteilungsAlgorithmus a, double[] sol) { 
		try {
			File csvFile = new File(b.getFilename()+"_Solution"+".csv");
			BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFile));
			csvWriter.write(String.join(",","Name","Wert","Räuber 1","Räuber 2"));
			csvWriter.newLine();
			List<Item> items = b.getBeute();
			int r1 = -1;
			int r2 = -1;
			int s = 0;
			// Get which Räuber gets the item 
			for (Item i : items) { 
				if(sol[s]==0) {
					r1 = 1;
					r2 = 0;
				} else {
					r1 = 0;
					r2 = 1;
				}
	        	// Write a row for individually item
	        	csvWriter.write(String.join(",", i.getBezeichnung(),String.valueOf(i.getWert()),String.valueOf(r1),String.valueOf(r2))); 
	        	csvWriter.newLine();
	        	s++;
	        }
			double[] summen = a.getRauberWert(sol);
			// Write Sum for RauberInnen and Value Function 
			csvWriter.write(String.join(",",null,"Summe",String.valueOf(summen[0]),String.valueOf(summen[1]),String.valueOf(Math.abs(summen[0]-summen[1]))));
	
			// Close Writer
			csvWriter.close();	
			System.out.println("Saved Solution!");
		} catch (IOException e) {
			System.out.println("Error in CsvBufferWriter !"); 
    		e.printStackTrace();
		}
        
    }
	
	
	 public static void main(String[] args) {
		// Random Beute Aufteilung
		//RaeuberinnenProblem.randomBeute(10);
		
		// Given csv File Beute Aufteilung
		RaeuberinnenProblem.csvBeute("TestItems.csv",",",false,"C:\\Users\\tasug\\Documents\\Leoben\\Operations Research\\RaeuberInnenProject\\");
	}
	

}
