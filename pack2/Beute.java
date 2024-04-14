package pack2;

import java.util.*;

public class Beute {

	private ArrayList<Item> items = new ArrayList<Item>();
	private int no_items;
	private Random rnd = new Random(10);

	public Beute(int no_items) {

		this.no_items = no_items;
		this.createBeute();
	}

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

    public void sortItemsByWert() {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item item2, Item item1) {
                // Compare the 'wert' attributes of items
                return Double.compare(item1.getWert(), item2.getWert());
            }
        });
    }
	
}
