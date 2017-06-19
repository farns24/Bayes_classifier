package utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Utils {

	
//	private static PorterStemmer porter = new PorterStemmer();
//	private static MyDictionary dictionary = new MyDictionary();
//	private static StopWords stopWords = new StopWords();
	
	
//	public static List<Score> getTop10Scores(String query,IndexedStructure struct)
//	{
//		List<Score> result = new ArrayList<Score>();
//		String[] words = prepWords(query);
//		File[] allFiles = getWikiFileNames();
//		for (File file :allFiles)
//		{
//			String fileName = file.getName();
//			double[] score =getScore(words, fileName, struct);
//			result.add(new Score(score, file));
//		}
//		
//		Collections.sort(result);
//		return result;
//	}
	
	/**
	 * Gets file names for wiki documents
	 * @return
	 */
	public static Map<String,File[]> getTrainingFiles() {
		
		Map<String,File[]>results = new HashMap<String,File[]>();
		String filePath = new File("").getAbsolutePath();
		
		File folder = new File(filePath.concat("/20NG/"));
		String[] children = folder.list();
		
		for (String child: children)
		{
			File cDir = new File(folder.getAbsolutePath().concat("/").concat(child));
			File[] listOfFiles = cDir.listFiles();
			results.put(child, listOfFiles);
		}
		
		
		return results;
	}
	
//	public static double[] getScore(String[] words, String document, IndexedStructure struct)
//	{
//		double score = 0.0;
//		double[] results = new double[words.length+1];
//		int index =1;
//		for (String word : words)
//		{
//			int documentCount = struct.getDocumentCount();
//			int matchCount;
//			try {
//				matchCount = struct.getTop10Documents(word).length;
//			} catch (WordNotFoundException e) {
//				continue;
//			}
//			double wordScore = getTF(document, word, struct)* getIDF(documentCount, matchCount);
//			//System.out.printf("%s with score of %f\n",word,wordScore);
//			score+= wordScore;
//			results[index++]=wordScore;
//		}
//		results[0] = score;
//		return results;
//		
//	}
//	
//	public static double getIDF(double documentCount,double matchCount)
//	{
//		return Math.log(documentCount/matchCount)/Math.log(2);
//	}
//	
//	public static double getTF(String document, String word ,IndexedStructure struct)
//	{
//		Integer freq = struct.getData().get(word).getReferenceCount().get(document);
//		if(freq == null)
//		{
//			return 0.0;
//		}
//		 double wFreq = (double)freq;
//		 double lMaxFreq;
//		try {
//			lMaxFreq = struct.getMaxWordCount(document);
//		} catch (WordNotFoundException e) {
//			
//			e.printStackTrace();
//			return 0;
//		}
//		
//		return wFreq/lMaxFreq;
//	}
//
//	public static String stripToLetters(String word){
//		StringBuilder wdBldr = new StringBuilder();
//		for (char a : word.toCharArray())
//		{
//			if (Character.isAlphabetic(a))
//			{
//				wdBldr.append(Character.toLowerCase(a));
//			}
//			else if (a =='\'')
//			{
//				wdBldr.append(a);
//			}
//		}
//		return wdBldr.toString();
//	}
	
//	public static String[] prepWords(String nextLine) {
//		String[] res = nextLine.split(" ");
//		List<String> validWords = new ArrayList<String>();
//		for (String word: res)
//		{
//			if (word.contains("-"))
//			{
//				String[] parts =word.split("-");
//				for (String part:parts)
//				{
//					part = stripToLetters(part);
//					addIfValid(validWords, part);
//				}
//				
//			}
//			
//				word =stripToLetters(word);
//				addIfValid(validWords,word);
//			
//		}
//		return validWords.toArray(new String[validWords.size()]);
//	}
//
//	private static void addIfValid(List<String> validWords, String word) {
//		if (dictionary.contains(word))
//		{
//			if (!stopWords.contains(word))
//			{
//				//Words are valid
//				word = porter .stem(word);
//				validWords.add(word);
//			}
//		}
//	}
}
