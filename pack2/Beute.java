package pack2;

import java.util.*;

public class Beute {
	public ArrayList<Item> beute;

	public Beute(ArrayList<Item> items) {
		this.beute = items;
	}

	public ArrayList<Item> getBeute() {
		return this.beute;
	}

	public void addItem(Item item) {
		beute.add(item);
	}

}
