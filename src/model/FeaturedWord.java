package model;

public class FeaturedWord implements Comparable<FeaturedWord> {

	private String word;
	private double score;
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public FeaturedWord(String word, double score) {
		super();
		this.word = word;
		this.score = score;
	}
	@Override
	public int compareTo(FeaturedWord o) {
		return -1 * Double.compare(this.score, o.score);
	}
	@Override
	public String toString() {
		return "FeaturedWord [word=" + word + ", score=" + score + "]";
	}
	
}
