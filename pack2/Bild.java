package pack2;

public class Bild extends Item{
	public Double laenge;
	public Double breite;
	public Double gewicht;

	public Bild(String bezeichnung, Double wert, Double laenge, Double breite, Double gewicht) {
		super(bezeichnung, wert);
		this.laenge = laenge;
		this.breite = breite;
		this.gewicht = gewicht;
	
	}

}
