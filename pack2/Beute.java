package pack2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Beute {

	private ArrayList<Item> items;
	private int no_items;
	private Random rnd = new Random(10);
	private String delimeter; 
	private static final String NEW_LINE_SEPARATOR = "\n"; 

	public Beute(int no_items) {
		items = new ArrayList<Item>();
		this.delimeter = ",";
		this.no_items = no_items;
		this.createBeute();
	}
	
	public Beute (String delimeter, Boolean header, String filepath) 
	{
		items = new ArrayList<Item>();
		this.delimeter = delimeter;
		readCSV(filepath,header);
		no_items = items.size();
	}
 // Read from the CSV-File
	private void readCSV(String filepath, Boolean header) {
		// UNTERDRÜCKEN HEADER!!!!!!
		List<List<String>> records = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File(filepath))) {
			if (scanner.hasNextLine() && header) {
				scanner.nextLine();
			}
		    while (scanner.hasNextLine()) {
		        records.add(getRecordFromLine(scanner.nextLine()));
		    }
		} catch (IOException e) {
			System.out.println("Error in CsvFileReader!"); 
    		e.printStackTrace();			
		}
		for(List<String> list: records) {
			items.add(new Item (list.get(0),Double.parseDouble(list.get(1))));
		}
	}
 // Read one line of CSV-File
	private List<String> getRecordFromLine(String line) {
	    List<String> values = new ArrayList<String>();
	    try (Scanner rowScanner = new Scanner(line)) {
	        rowScanner.useDelimiter(this.delimeter);
	        while (rowScanner.hasNext()) {
	            values.add(rowScanner.next());
	        }
	    }
	    return values;
	}
	
	// Write to CSV-File
	private void writeCSV(File csvFile, Boolean header) {
		try {
	        // Create a FileWriter object for the CSV file. 
	        FileWriter csvWriter = new FileWriter(csvFile); 
	        
	        // If true, write a header row. 
	        if(header){
	        	csvWriter.write("Name"+delimeter+"Wert");
	        	csvWriter.write(NEW_LINE_SEPARATOR);
	        }
	        for (Item i : this.items) { 
	        	// Write a row of data
	        	csvWriter.write(i.getBezeichnung()+delimeter+String.valueOf(i.getWert())); 
	        	csvWriter.write(NEW_LINE_SEPARATOR);
	        }
	        
	        // Close the FileWriter object. 
	        csvWriter.close();
		} catch (IOException e) {
			System.out.println("Error in CsvFileWriter !"); 
    		e.printStackTrace();
		}
	}
	// Give List of Beute
	public ArrayList<Item> getBeute() {
		return this.items;
	}

	public void createBeute() {
		for (int i = 0; i < this.no_items; i++) {
			String bezeichnung = "AA" + i;
			Double wert = rnd.nextDouble() * 1000;
			Item item = new Item(bezeichnung, wert);
			items.add(item);
		}
	}
	
	// Beute als CSV speichern
	public void saveBeute(String filename, Boolean header) {
		this.delimeter = ",";
		File csvFile = new File(filename+".csv");
		writeCSV(csvFile, header);
	}

    public void sortItemsByWert() {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item item2, Item item1) {
                // Compare the 'wert' attributes of items
                return Double.compare(item1.getWert(), item2.getWert());
            }
        });
    }
    
    public void setDelimeter(String delimeter) {
    	this.delimeter = delimeter;
    }
    
    @Override
    public String toString() {
		return "Beute("+this.no_items+"): Gesamtwert = "+getBeuteValue();
    }
    
    private double getBeuteValue() {
    	double gesamtWert = 0;
    	for (Item i: this.items) {
    		gesamtWert += i.getWert();
    	}
    	return gesamtWert;
    }
    
    public static void main(String[] args) {
    	Beute b1 = new Beute (",",false,"C:\\Users\\tasug\\Documents\\Leoben\\Operations Research\\RaeuberInnenProject\\TestItems.csv");
    	System.out.println(b1.no_items);
    	Beute b2 = new Beute(20);
    	System.out.println(b2);
    	for (Item i:b2.getBeute()) {
    		System.out.println(i);
    	}
    	b2.saveBeute("BeuteSpeicherung",true);
    	Beute b3 = new Beute (",",true,"C:\\Users\\tasug\\eclipse-workspace\\raueberlein\\BeuteSpeicherung.csv");
    	for (Item i:b3.getBeute()) {
    	    System.out.println(i);
    	}
    }
    
	
}
