package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import provided.MyReader;

import java.util.Set;

import utils.Utils;

public class MNB_Classifier implements IFeatureSelectProbabilityFinder {
    Map<String,Map<String,Integer>> word2class2Count = new HashMap<String, Map<String,Integer>>();
	Map<String,Integer> classDocumentCounts = new HashMap<String,Integer>();
	private int totalDocumentCount= 0;
	
	Map<String,Set<String>> word2Documents = new HashMap<String,Set<String>>();
	
	
	public MNB_Classifier() {
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
					if (word2Documents.containsKey(word)==false)
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
						class2count = new HashMap<String, Integer>();
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
					
				}
			}
			
		}
	}


	@Override
	public double getInformationGain(String word) {
		
		double pw = getWordProbability(word);
		double pSansw = getWordAbsenseProbability(word);
		double sumPc = 0;
		double sumPcw = 0;
		double sumPcSansw = 0;
		for (String className:classDocumentCounts.keySet())
		{
			double pc = getClassProbability(className);
			sumPc += (pc * Math.log(pc)/Math.log(2));
			
			double pcw = getClassProbGivenWord(word, className);
			sumPcw += (pcw * Math.log(pcw)/Math.log(2));
			
			
			double pcSansw = getClassProbGivenNotWord(word, className);
			sumPcSansw += (pcSansw * Math.log(pcSansw)/Math.log(2));
			
			
		}
		double ig = (-1.0 * sumPc)+(pw*sumPcw)+(pSansw*sumPcSansw);
		return ig;
	}

	@Override
	public double getClassProbability(String className) {
		
		double docCount = classDocumentCounts.get(className);
		double totalDoc = this.totalDocumentCount;
		
		return docCount/totalDoc;
	}

	@Override
	public double getWordProbability(String word) {
		
		//# of documents that contain word
		//# of documents in set
		
		double docCount =  word2Documents.get(word).size();
		double totalDoc = this.totalDocumentCount;
		return docCount/totalDoc;
	}

	@Override
	public double getWordAbsenseProbability(String word) {
		double docCount =  word2Documents.get(word).size();
		double totalDoc = this.totalDocumentCount;
		return (totalDoc-docCount)/totalDoc;
	}

	

	@Override
	public double getClassProbGivenNotWord(String word, String className) {
		double docCount;
		if(this.word2class2Count.get(word).containsKey(className)==false)
		{
			docCount = 0;
		}
		else
		{
		docCount = this.word2class2Count.get(word).get(className);
		}
		double totalDoc = this.word2Documents.get(word).size();
		return (this.totalDocumentCount -docCount)/(this.totalDocumentCount -totalDoc);
	}


	@Override
	public double getClassProbGivenWord(String word, String className) {
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
		double totalDoc = this.word2Documents.get(word).size();
		return docCount/totalDoc;
	}


	@Override
	public List<FeaturedWord> getFeatureSelection(int m) {
		
		List<FeaturedWord> result = new ArrayList<FeaturedWord>();
		for (String word:word2Documents.keySet())
		{
			double score = getInformationGain(word);
			result.add(new FeaturedWord(word, score));
		}
		Collections.sort(result);
		return result.subList(0, m-1);
		//return result;
	}

	public double getWordProbGivenClass(String word, String className) {
		
		double classBarWord = getClassProbGivenWord(word, className);
		
		double classProb = getClassProbability(className);
		double wordProb = getWordProbability(word);
		
		return (classBarWord * wordProb)/classProb;
	}


	@Override
	public String label(File testFile,int M) {
	//	Set<String> words = new HashSet<>();
		
		List<FeaturedWord> fws = null;
		if (M== -1)
		{
			fws = getFeatureSelection(word2Documents.size());
		}
		fws = getFeatureSelection(M);
		int[] vect = new int[M];
		for (String word: MyReader.readEmail(testFile.toPath()))
		{
			int i = 0;
			for (FeaturedWord fw:fws)
			{
				if (fw.getWord().equals(word))
				{
					vect[i]++;
				}
				i++;
			}
		//	words.add(word);
		}
		
		List<FeaturedWord> classes = new ArrayList<FeaturedWord>();
		
		for (String className:classDocumentCounts.keySet() )
		{
			double classProb = 1.0;
			for (String word: word2Documents.keySet())
			{
				
				
				int i = 0;
				for (FeaturedWord fw:fws)
				{
					if (fw.getWord().equals(word))
					{
						classProb+= Math.log(getWordProbGivenClass(word, className)) * vect[i];
					}
					else
					{
//						classProb+= (1.0 -getWordProbGivenClass(word, className));
					}
					i++;
				}
			

				
			}
			classes.add(new FeaturedWord(className, Math.pow(Math.E,classProb)));
			
		}
		Collections.sort(classes);
		
		return classes.get(0).getWord();
	}



}
