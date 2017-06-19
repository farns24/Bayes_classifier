package model;

import java.io.File;
import java.util.List;

public interface IFeatureSelectProbabilityFinder {
	public double getInformationGain(String word);
	public double getClassProbability(String className);
	public double getWordProbability(String word);
	public double getWordAbsenseProbability(String word);
	public double getClassProbGivenWord(String word, String className);
	public double getClassProbGivenNotWord(String word, String className);
	public List<FeaturedWord> getFeatureSelection(int m);
//	public double getWordProbGivenClass(String word,String className);
//	public String label(File testFile);
	String label(File testFile, int M);
}
