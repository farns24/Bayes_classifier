package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import model.MNB_Classifier;
import model.MNB_Evaluator;
import model.MNB_Probability;
import model.WordProbs;
import model.FeaturedWord;

public class TestFeatureSelectionFinder {

	@Before
	public void setUp() throws Exception {
	}

//	@Test
//	public void testUndermining()
//	{
//		
//		String undermining = "the";
//		MNB_Classifier finder = new MNB_Classifier();
//		finder.getInformationGain(undermining);
//	}
	
	
//	@Test
//	public void test() {
//	MNB_Classifier finder = new MNB_Classifier();
//	List<FeaturedWord> result = finder.getFeatureSelection(10);
//	System.out.println(result.toString());
//	
//	String label = finder.label(new File("/users/guest/f/farns24/workspace/Bayes_classifier/20NG/comp.graphics/37261"),6200);
//	System.out.println(label);
//	}
	
//	@Test
//	public void testProbs()
//	{
//		MNB_Probability probs = new MNB_Probability();
//		
//		Map<String, Double> classProbabilities = probs.computerClassProbabilities();
//		
//		
//		WordProbs wordProbabilities = probs.computeWordProbability();
//	}
	@Test
	public void testEvaluator()
	{
		MNB_Evaluator eval = new MNB_Evaluator();
		eval.accuracyMeasure();
	}
		
}
