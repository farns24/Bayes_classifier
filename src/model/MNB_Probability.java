package model;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import provided.MyReader;
import utils.Utils;

public class MNB_Probability  {
   private Map<String,Map<String,Integer>> word2class2Count = new TreeMap<String, Map<String,Integer>>();
	private Map<String,Integer> classDocumentCounts = new TreeMap<String,Integer>();
	private Map<String,Map<String,Set<String>>> class2word2Document = new TreeMap<>();
	private int totalDocumentCount= 0;
	
	/*
	 * How is this used?
	 */
	private Map<String,Set<String>> word2Documents = new TreeMap<String,Set<String>>();

	
	public MNB_Probability() {
		super();
		
		Map<String,File[]> trainingFiles = Utils.getTrainingFiles();
		
		for (Entry<String,File[]>entry: trainingFiles.entrySet())
		{
			classDocumentCounts.put(entry.getKey(), entry.getValue().length);
			this.totalDocumentCount+=entry.getValue().length;
			
			for (File file: entry.getValue())
			{
				for (String word: MyReader.readEmail(file.toPath()))
				{
					Set<String> fileNames = null;
					if (word2Documents.containsKey(word)==false)//Existance of word
					{
						fileNames = new HashSet<String>();
					}
					else
					{
						fileNames = word2Documents.get(word);
					}
					fileNames.add(file.getName());
					
					word2Documents.put(word, fileNames);
					
					Map<String,Integer> class2count = null;
					if (word2class2Count.containsKey(word)==false)
					{
						class2count = new TreeMap<String, Integer>();
					}
					else
					{
						class2count = word2class2Count.get(word);
					}
					
					
					if (class2count.containsKey(entry.getKey())==false)
					{
						class2count.put(entry.getKey(), 1);
					}
					else
					{
						class2count.put(entry.getKey(), class2count.get(entry.getKey())+1);
					}
					word2class2Count.put(word, class2count);
					
					Map<String, Set<String>> classword2Document = null;
					if (class2word2Document.containsKey(entry.getKey()))
					{
						classword2Document = class2word2Document.get(entry.getKey());
					}
					else
					{
						classword2Document = new TreeMap<>();
					}
					
					Set<String> classDocSet = null;
					if (classword2Document.containsKey(word))
					{
						classDocSet = classword2Document.get(word);
					}
					else
					{
						classDocSet = new TreeSet<>();
					}
					classDocSet.add(file.getName());
					classword2Document.put(word, classDocSet);
					class2word2Document.put(entry.getKey(),classword2Document );
					
				}
			}
			
		}
	}

	
	public Map<String,Double> computerClassProbabilities()
	{
		Map<String, Double> result = new TreeMap<String, Double>();
		for (Entry<String,Integer> className :classDocumentCounts.entrySet())
		{
			double count = className.getValue();
			result.put(className.getKey(), count/((double)totalDocumentCount));
			
			
		}
		
		return result;
	}
	
	public WordProbs computeWordProbability()
	{
		
		
		WordProbs result = new WordProbs();
		
		//ClassProb[] allProbs = new ClassProb[classDocumentCounts.keySet().size()];
//		int j = 0;
		for (String className: classDocumentCounts.keySet())
		{
			Map<String,double[]> classProbs = new TreeMap<>();
			//int i = 0;
			for (String word: word2Documents.keySet())//iterate through words
			{
				double docCount;
				if(this.class2word2Document.get(className).containsKey(word)==false)
				{
					docCount = 0;
				}
				else
				{
					docCount = this.class2word2Document.get(className).get(word).size();
				}
				 
				
				
				docCount+=1.0;
				
				double docInClassCount = classDocumentCounts.get(className)+1;

				double docNotCount = docInClassCount -docCount;
				docCount+=1.0;
				
				double probExist = docCount/docInClassCount;
				
				double probNoExist = docNotCount/docInClassCount;
				
				if (probExist>1.0)
				{
					throw new RuntimeException("Probability too high");
				}
				
//				WordProb wordProb = new WordProb(probExist, probNoExist); 
				classProbs.put(word, new double[]{probExist,probNoExist});
			}
			
			result.put(className, classProbs);
		}
	
		return result ;
	}


	public double getClassProbability(String className) {
		
		double docCount = classDocumentCounts.get(className);
		double totalDoc = this.totalDocumentCount;
		
		return docCount/totalDoc;
	}

	public double getWordProbability(String word) {
		
		//# of documents that contain word
		//# of documents in set
		
		double docCount =  word2Documents.get(word).size();//size of vocabulary
		double totalDoc = this.totalDocumentCount;
		return docCount/totalDoc;
	}


	private double getClassProbGivenWord(String word, String className) {
		double docCount;
		if(this.word2class2Count.get(word).containsKey(className)==false)
		{
			docCount = 0;
		}
		else
		{
			docCount = this.word2class2Count.get(word).get(className);
		}
//		double docCount = this.word2class2Count.get(word).get(className);
		double totalDoc = this.word2Documents.get(word).size();//size of vocabulary
		return docCount/totalDoc;
	}




	public double getWordProbGivenClass(String word, String className) {
		
		double classBarWord = getClassProbGivenWord(word, className);
		
		double classProb = getClassProbability(className);
		double wordProb = getWordProbability(word);
		
		return (classBarWord * wordProb)/classProb;
	}



}
