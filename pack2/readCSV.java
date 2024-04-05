package pack2;

import java.util.*;
import java.io.*;

public class readCSV {
	List<List<String>> records = new ArrayList<>();
	String delimeter;
	public readCSV(List<Item> items, String delimeter,  String filepath) throws FileNotFoundException{
		this.delimeter = delimeter;
		try (Scanner scanner = new Scanner(new File(filepath))) {
		    while (scanner.hasNextLine()) {
		        records.add(getRecordFromLine(scanner.nextLine()));
		    }
		}
		getItems(items);
	}
	
	private void getItems(List<Item> items) {
		for(List<String> list:this.records) {
			items.add(new Item (list.get(0),Double.parseDouble(list.get(1))));
		}
	}
	
	private List<String> getRecordFromLine(String line) {
	    List<String> values = new ArrayList<String>();
	    try (Scanner rowScanner = new Scanner(line)) {
	        rowScanner.useDelimiter(delimeter);
	        while (rowScanner.hasNext()) {
	            values.add(rowScanner.next());
	        }
	    }
	    return values;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		List<Item> items = new ArrayList<>();
		readCSV a = new readCSV(items,",", "C:\\Users\\tasug\\Documents\\Leoben\\Operations Research\\RaeuberInnenProject\\TestItems.csv");
		for (Item i : items) {
			System.out.println(i.getBezeichnung()+": "+ i.getWert()+" €");
		}
		System.out.println("Teste GitHub");
	}
	
}
