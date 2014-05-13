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
		//map from tf type -> queryWord -> score
		Map<String,Map<String, Double>> tfs = new HashMap<String,Map<String, Double>>();
		
		////////////////////Initialization/////////////////////
		
		/*
		 * @//TODO : Your code here
		 */
		
		
	    ////////////////////////////////////////////////////////
		
		//////////handle counts//////
		
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
				// is this a term we haven't seen before?
				if (!currTermMap.containsKey(queryWord)) {
					// url
					if (type.equals("url")) {
						currTermMap.put(queryWord, countNumOfOccurrencesInString(queryWord, d.url));
					}
					
					// title
					if (type.equals("title")) {
						currTermMap.put(queryWord, countNumOfOccurrencesInString(queryWord, d.title));
					}
					
					// headers
					if (type.equals("header")) {
						for (String header : d.headers) {
							// TODO
						}
					}
				}
			}
		}
		
		
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

}
