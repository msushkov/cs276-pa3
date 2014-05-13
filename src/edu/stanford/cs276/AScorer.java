package edu.stanford.cs276;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AScorer 
{
	
	Map<String,Double> idfs;
	String[] TFTYPES = {"url","title","body","header","anchor"};
	
	public AScorer(Map<String,Double> idfs)
	{
		this.idfs = idfs;
	}
	
	//scores each document for each query
	public abstract double getSimScore(Document d, Query q);
	
	//handle the query vector
	public Map<String,Double> getQueryFreqs(Query q)
	{
		Map<String,Double> tfQuery = new HashMap<String,Double>();
		
		for (String term : q.queryWords) {
			if (tfQuery.containsKey(term)) {
				tfQuery.put(term, tfQuery.get(term) + 1.0);
			} else {
				tfQuery.put(term, 1.0);
			}
		}
		
		return tfQuery;
	}
	

	
	////////////////////Initialization/Parsing Methods/////////////////////
	
	/*
	 * @//TODO : Your code here
	 */
	
	
    ////////////////////////////////////////////////////////
	
	
	/*/
	 * Creates the various kinds of term frequences (url, title, body, header, and anchor)
	 * You can override this if you'd like, but it's likely that your concrete classes will share this implementation
	 */
	public Map<String,Map<String, Double>> getDocTermFreqs(Document d, Query q)
	{
		System.out.println("In getDocTermFreqs(). DOCUMENT: " + d.toString());
		
		//map from tf type -> queryWord -> score
		Map<String,Map<String, Double>> tfs = new HashMap<String,Map<String, Double>>();
		
		//////////handle counts//////
		
		// TODO: DEBUG THE COUNTS!
		
		// go through url, title, etc
		for (String type : TFTYPES) {
			Map<String, Double> currTermMap;
			
			if (!tfs.containsKey(type)) {
				currTermMap = new HashMap<String, Double>();
				tfs.put(type, currTermMap);
			} else {
				currTermMap = tfs.get(type);
			}
			
			//loop through query terms increasing relevant tfs
			for (String queryWord : q.queryWords)
			{
				queryWord = queryWord.toLowerCase();
				
				// is this a term we haven't seen before?
				if (!currTermMap.containsKey(queryWord)) {
					// url
					if (type.equals("url") && d.url != null) {
						currTermMap.put(queryWord, countNumOfOccurrencesInString(queryWord, d.url));
					}
					
					// title
					if (type.equals("title") && d.title != null) {
						currTermMap.put(queryWord, countNumOfOccurrencesInString(queryWord, d.title));
					}
					
					// headers
					if (type.equals("header") && d.headers != null) {
						// create a single string out of the headers list
						StringBuffer concatenatedHeader = new StringBuffer();
						for (String header : d.headers) {
							concatenatedHeader.append(header);
						}
						
						currTermMap.put(queryWord, countNumOfOccurrencesInString(queryWord, concatenatedHeader.toString()));
					}
					
					// body
					if (type.equals("body") && d.body_hits != null) {
						currTermMap.put(queryWord, (double) d.body_hits.size());
					}
					
					// anchor
					if (type.equals("anchor") && d.anchors != null) {
						int count = 0;
						for (String anchor : d.anchors.keySet()) {
							// for each anchor, count how many times the query term occurs in that anchor and multiply by the number of times that anchor occurs
							count += countNumOfOccurrencesInString(queryWord, anchor) * d.anchors.get(anchor);
						}
						
						currTermMap.put(queryWord, (double) count);
					}
				}
			}
		}
		
		// debug
		debugPrinttfResult(tfs, q);
		
		return tfs;
	}
	
	/*
	 * Counts the number of occurrences of term in str.
	 * 
	 * http://stackoverflow.com/questions/767759/occurrences-of-substring-in-a-string
	 */
	private double countNumOfOccurrencesInString(String term, String str) {
	    Pattern p = Pattern.compile(term.toLowerCase());
	    Matcher m = p.matcher(str.toLowerCase());
	    double count = 0;
	    while (m.find()){
	    	count +=1;
	    }
	    return count;
	}

	private void debugPrinttfResult(Map<String,Map<String, Double>> tf, Query q) {
		System.out.println("QUERY: " + q.queryString);
		for (String type : tf.keySet()) {
			System.out.print("TYPE: " + type + " ");
			
			for (String term : tf.get(type).keySet()) {
				System.out.print(term + " " +  tf.get(type).get(term));
			}
			
			System.out.println();
		}
	}
	
}
