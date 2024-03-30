package pack2;

import java.util.*;

public class CreateBeute {
	public ArrayList<Item> beute;

	public CreateBeute() {
	}

	public ArrayList<Item> getBeute() {
		return this.beute;
	}

	public void addItem(Item item) {
		beute.add(item);
	}

}
