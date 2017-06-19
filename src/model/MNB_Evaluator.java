package model;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import utils.Utils;

public class MNB_Evaluator {

	private MNB_Classifier classifier = new MNB_Classifier();
	public double accuracyMeasure()
	{
		double accuracy = 1.0;
		Map<String,File[]> trainingFiles = Utils.getTrainingFiles();
		
		for (Entry<String,File[]> entry: trainingFiles.entrySet())
		{
			for (File file:entry.getValue())
			{
				String label = classifier.label(file, 6200);
				if (label.equals(entry.getKey()))
				{
					System.out.println("Great");
				}
				else
				{
					System.out.println(label +" is wrong. Should be "+ entry.getKey());
				}
			}
		}
		
		
		return accuracy;
	}
}
