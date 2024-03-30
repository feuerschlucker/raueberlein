package pack2;

public class Item {
	public String bezeichnung;
	public Double wert;
	
	protected Item (String bezeichnung, Double wert) {
		this.bezeichnung = bezeichnung;
		this.wert = wert;
	}
	
	public Double getWert() {
		return this.wert;
	}
	
	public void setWert(Double wert) {
		this.wert= wert;
	}

}
