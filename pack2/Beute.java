package pack2;

import java.util.*;

public class Beute {

	private ArrayList<Item> items = new ArrayList<Item>();
	private int no_items;
	private Random rnd;

	public Beute(int no_items) {
		Random rnd = new Random();
		this.no_items = no_items;
		this.createBeute();
	}

	public ArrayList<Item> getBeute() {
		return this.items;
	}

	public void createBeute() {
		for (int i = 0; i < this.no_items; i++) {
			String bezeichnung = "AA" + i;
			Double wert = rnd.nextDouble()*1000;
			Item item = new Item(bezeichnung, wert);
			items.add(item);		
		}
	}

}
