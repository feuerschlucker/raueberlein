package pack2;

import java.io.*;
import java.util.*;


public class Beute {

	private ArrayList<Item> items;
	private int no_items;
	private Random rnd = new Random(10);
	private String delimiter = ","; 
	private String filename;
	 

	public Beute(int no_items) {
		items = new ArrayList<Item>();
		this.no_items = no_items;
		this.filename = "NewBeute_"+this.no_items;
		this.createBeute();
	}
	
	public Beute (String filename, String delimiter, Boolean header,  String filepath) 
	{
		items = new ArrayList<Item>();
		if (delimiter != null) {this.delimiter = delimiter;}
		int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) {
            this.filename = filename;
        } else {
        	this.filename = filename.substring(0, dotIndex);
        }
		readCSV(filepath+this.filename+".csv",header);
		no_items = items.size();
		
	}
 // Read from the CSV-File
	private void readCSV1(String filepath, Boolean header) {

		List<List<String>> records = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File(filepath))) {
			// Suppress header
			if (scanner.hasNextLine() && header) {
				scanner.nextLine();
			}
		    while (scanner.hasNextLine()) {
		        records.add(getRecordFromLine1(scanner.nextLine()));
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
	private List<String> getRecordFromLine1(String line) {
	    List<String> values = new ArrayList<String>();
	    try (Scanner rowScanner = new Scanner(line)) {
	        rowScanner.useDelimiter(this.delimiter);
	        while (rowScanner.hasNext()) {
	            values.add(rowScanner.next());
	        }
	    }
	    return values;
	}
	
    public void readCSV(String filepath, boolean header) {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
        	// Suppress header
        	if (header) {
                br.readLine(); 
            }
            String line;
            while ((line = br.readLine()) != null) {
                List<String> record = getRecordFromLine(line);
                if (record.size() >= 2) {
                    items.add(new Item(record.get(0), Double.parseDouble(record.get(1))));
                }
            }
        } catch (IOException e) {
            System.out.println("Error in CSVReader!");
            e.printStackTrace();
        }
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(this.delimiter);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
	
		
	// Write to CSV-File
	private void writeCSV(File csvFile, Boolean header) {
		try {
	        // Create a BufferWriter object for the CSV file. 
			BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFile));
	        
	        // If true, write a header row. 
	        if(header){
	        	csvWriter.write(String.join(delimiter,"Name","Wert"));
	        	csvWriter.newLine();
	        }
	        for (Item i : this.items) { 
	        	// Write a row of data
	        	csvWriter.write(String.join(delimiter, i.getBezeichnung(),String.valueOf(i.getWert()))); 
	        	csvWriter.newLine();
	        }
	        
	        // Close the FileWriter object. 
	        csvWriter.close();
		} catch (IOException e) {
			System.out.println("Error in CsvBufferWriter !"); 
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
	
	// Save Beute as CSV-File
	public void saveBeute(String filename, Boolean header) {
		this.filename = filename;
		this.delimiter = ",";
		File csvFile = new File(this.filename+".csv");
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
    
    // Set Delimeter for reading and writing Beute
    public void setDelimeter(String delimiter) {
    	this.delimiter = delimiter;
    }
    
    // Get Filename of Beute
    public String getFilename() {
    	return this.filename;
    }
    
    @Override
    public String toString() {
		return "Beute("+this.no_items+"): Gesamtwert = "+getBeuteValue();
    }
    
    public int getBeuteSize() {
    	return this.no_items;
    }
    
    // Get Sum of beute Items
    public double getBeuteValue() {
    	double gesamtWert = 0;
    	for (Item i: this.items) {
    		gesamtWert += i.getWert();
    	}
    	return gesamtWert;
    }
    
    public static void main(String[] args) {
    	Beute b1 = new Beute ("TestItems.csv",",",false,"C:\\Users\\tasug\\Documents\\Leoben\\Operations Research\\RaeuberInnenProject\\");
    	System.out.println(b1.no_items);
    	Beute b2 = new Beute(10);
    	System.out.println(b2);
    	for (Item i:b2.getBeute()) {
    		System.out.println(i);
    	}
    	b2.saveBeute("BeuteSpeicherung",true);
    	Beute b3 = new Beute ("BeuteSpeicherung.csv",",",true,"");
    	for (Item i:b3.getBeute()) {
    	    System.out.println(i);
    	}
    }
    
	
}
