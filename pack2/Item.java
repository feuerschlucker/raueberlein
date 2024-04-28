package pack2;

import java.util.Random;

public class Item {
	private String bezeichnung;
	private Double wert;
	private Random rnd;
	
	public Item (String bezeichnung, Double wert) {
		this.bezeichnung = bezeichnung;
		this.wert = wert;
	}
	
	public Item () {
		this.bezeichnung = "Item"+(rnd.nextInt());
	}
	
	public Double getWert() {
		return this.wert;
	}
	
	public void setWert(Double wert) {
		this.wert= wert;
	}
	public String getBezeichnung() {
		return this.bezeichnung;
	}
	
	@Override
	public String toString() {
		return this.bezeichnung+":"+this.wert;		
	}
}
