package it.polito.tdp.extflightdelays.model;

public class Arco implements Comparable<Arco>{
	private Airport aorigine;
	private Airport adest;
	private double peso;
	public Airport getAorigine() {
		return aorigine;
	}
	public void setAorigine(Airport aorigine) {
		this.aorigine = aorigine;
	}
	public Airport getAdest() {
		return adest;
	}
	public void setAdest(Airport adest) {
		this.adest = adest;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	public Arco(Airport aorigine, Airport adest, double peso) {
		super();
		this.aorigine = aorigine;
		this.adest = adest;
		this.peso = peso;
	}
	@Override
	public int compareTo(Arco arg0) {
		// TODO Auto-generated method stub
		return (int) -(this.peso-arg0.getPeso());
	}
	@Override
	public String toString() {
		return String.format("aorigine=%s--->adest=%s : %s \n", aorigine.getAirportName(), adest.getAirportName(), peso);
	}
	
	
	

}
