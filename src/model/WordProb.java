package model;

public class WordProb {

	double probabilityExist;
	double probabilityNoExist;

	public WordProb(double probabilityExist, double probabilityNoExist) {
		super();
		this.probabilityExist = probabilityExist;
		this.probabilityNoExist = probabilityNoExist;

	}
	public double getProbabilityExist() {
		return probabilityExist;
	}
	public void setProbabilityExist(double probabilityExist) {
		this.probabilityExist = probabilityExist;
	}
	public double getProbabilityNoExist() {
		return probabilityNoExist;
	}
	public void setProbabilityNoExist(double probabilityNoExist) {
		this.probabilityNoExist = probabilityNoExist;
	}

	
}
