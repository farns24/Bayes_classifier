package model;

import java.util.Set;

public interface IClassifier {

	public IProbabilityTable trainData();
	
	public Set<String> featureSelection();
}
