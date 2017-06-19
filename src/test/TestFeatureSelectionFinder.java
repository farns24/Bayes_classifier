package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import model.MNB_Classifier;
import model.MNB_Probability;
import model.WordProbs;
import model.FeaturedWord;

public class TestFeatureSelectionFinder {

	@Before
	public void setUp() throws Exception {
	}

//	@Test
//	public void test() {
//	MNB_Classifier finder = new MNB_Classifier();
//	List<FeaturedWord> result = finder.getFeatureSelection(10);
//	System.out.println(result.toString());
//	
//	String label = finder.label(new File("C:\\Users\\wolfa\\workspace\\Bayes Model Classification\\20NG\\sci.crypt\\15000"));
//	System.out.println(label);
//	}
	
	@Test
	public void testProbs()
	{
		MNB_Probability probs = new MNB_Probability();
		
		Map<String, Double> classProbabilities = probs.computerClassProbabilities();
		
		
		WordProbs wordProbabilities = probs.computeWordProbability();
	}

		
}
