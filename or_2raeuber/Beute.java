package or_2raeuber;

import java.io.*;
import java.util.*;


public class Beute {

	private ArrayList<Item> items;
	private int no_items;
	private Random rnd = new Random();
	private String delimiter = ","; 
	private String filename;
	 
	// Random Beute 
	public Beute(int no_items) {
		items = new ArrayList<Item>();
		this.no_items = no_items;
		this.filename = "NewBeute_"+this.no_items;
		this.createBeute();
	}
	
	// Beute from csv File
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
	
	// Create Random Beute
	public void createBeute() {
		for (int i = 0; i < this.no_items; i++) {
			String bezeichnung = "AA" + i;
			Double wert = rnd.nextDouble() * 1000;
			Item item = new Item(bezeichnung, wert);
			items.add(item);
		}
	}
	
	// Give List of Beute
	public ArrayList<Item> getBeute() {
		return this.items;
	}
	

    // Get Filename of Beute
    public String getFilename() {
    	return this.filename;
    }
    
    // Get numbers of Items of Beute
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
	
	// Save Beute as CSV-File
	public void saveBeute(String filename, Boolean header) {
		this.filename = filename;
		this.delimiter = ",";
		File csvFile = new File(this.filename+".csv");
		writeCSV(csvFile, header);
	}
	
	// Set Delimeter for reading and writing Beute
    public void setDelimeter(String delimiter) {
    	this.delimiter = delimiter;
    }
	
	// Read from the CSV-File
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
    
    // Read one line of CSV-File
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
	
    public void sortItemsByWert() {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item item2, Item item1) {
                // Compare the 'wert' attributes of items
                return Double.compare(item1.getWert(), item2.getWert());
            }
        });
    }
    
    @Override
    public String toString() {
		return "Beute("+this.no_items+"): Gesamtwert = "+getBeuteValue();
    }
   	
}
