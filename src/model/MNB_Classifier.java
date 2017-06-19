package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import provided.MyReader;

import java.util.Set;

import main.StopWords;
import utils.Utils;

public class MNB_Classifier implements IFeatureSelectProbabilityFinder {
  
	Map<String,Integer> classDocumentCounts = new HashMap<String,Integer>();
	private int totalDocumentCount= 0;
	private Map<String,Map<String,Set<String>>> class2word2Document = new TreeMap<>();
	Map<String,Set<String>> word2Documents = new HashMap<String,Set<String>>();
	
	
	public MNB_Classifier() {
		super();
		StopWords sw = new StopWords();

		Map<String,File[]> trainingFiles = Utils.getTrainingFiles();
		
		for (Entry<String,File[]>entry: trainingFiles.entrySet())
		{
			classDocumentCounts.put(entry.getKey(), entry.getValue().length);
			this.totalDocumentCount+=entry.getValue().length;
			
			for (File file: entry.getValue())
			{
				for (String word: MyReader.readEmail(file.toPath()))
				{
					if (sw.contains(word))
					{
						continue;
					}
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
			sumPc += pc!=0? (pc * Math.log(pc)/Math.log(2)):0.0;
			
			double pcw = getClassProbGivenWord(word, className);
			sumPcw += pcw!=0.0?(pcw * Math.log(pcw)/Math.log(2)):0.0;
			
			
			double pcSansw = getClassProbGivenNotWord(word, className);
			sumPcSansw += pcSansw!=0.0 ? (pcSansw * Math.log(pcSansw)/Math.log(2)):0.0;
			
			
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
		if(this.class2word2Document.get(className).containsKey(word)==false)
		{
			docCount = 0;
		}
		else
		{
			docCount = this.class2word2Document.get(className).get(word).size();
		}
		double totalDoc = this.word2Documents.get(word).size();
		double classDocCount = this.classDocumentCounts.get(className);
		return ( classDocCount-docCount)/(this.totalDocumentCount -totalDoc);
	}


	@Override
	public double getClassProbGivenWord(String word, String className) {
		double docCount;
		if(this.class2word2Document.get(className).containsKey(word)==false)
		{
			docCount = 0;
		}
		else
		{
			docCount = this.class2word2Document.get(className).get(word).size();
		}

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
		if (m==-1)
		{
			return result;
		}
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
		
		Map<String,Integer> vect = new HashMap<String, Integer>();
		
		List<FeaturedWord> fws = null;
		if (M== -1)
		{
			M = word2Documents.size();
		}
		
		fws = getFeatureSelection(M);
		//int[] vect = new int[M];
		for (String word: MyReader.readEmail(testFile.toPath()))
		{
			if (vect.containsKey(word)==false)
			{
				vect.put(word, 1);
			}
			else
			{
				vect.put(word,1+vect.get(word));
			}
		}
		
		List<FeaturedWord> classes = new ArrayList<FeaturedWord>();
		
		for (String className:classDocumentCounts.keySet() )
		{
			double classProb = 1.0;
			for (String word: word2Documents.keySet())
			{
				
					
					if (vect.containsKey(word))
					{
						if (getWordProbGivenClass(word, className)==0)
						{
							continue;
						}
						classProb+= Math.log(getWordProbGivenClass(word, className)) * vect.get(word);
					}
					else
					{
//						classProb+= (1.0 -getWordProbGivenClass(word, className));
					}
				
				
			

				
			}
			classes.add(new FeaturedWord(className, classProb));
			
		}
		Collections.sort(classes);
		Collections.reverse(classes);
		return classes.get(0).getWord();
	}



}
