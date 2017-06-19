package model;

import java.util.Map;

public class ClassProb {

	private Map<String, WordProb> probs;

	public ClassProb(Map<String, WordProb> classProbs) {
		probs = classProbs;
	}

	public Map<String, WordProb> getProbs() {
		return probs;
	}

	public void setProbs(Map<String, WordProb> probs) {
		this.probs = probs;
	}

}
