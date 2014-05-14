package edu.stanford.cs276;

import java.util.HashMap;
import java.util.Map;

public class CosineSimilarityScorer extends AScorer
{
	///////////////weights///////////////////////////
	
	private double urlweight = 1.5;
	private double titleweight = 1.3;
	private double bodyweight = 0.3;
	private double headerweight = 1.5;
	private double anchorweight = 1.5;
	private double smoothingBodyLength = 30000;

	Map<String, Double> weightParams = new HashMap<String, Double>();

	//////////////////////////////////////////


	public CosineSimilarityScorer(Map<String,Double> idfs)
	{
		super(idfs);
		
		weightParams.put("url", urlweight);
		weightParams.put("title", titleweight);
		weightParams.put("body", bodyweight);
		weightParams.put("header", headerweight);
		weightParams.put("anchor", anchorweight);
	}

	public double getNetScore(Map<String, Map<String, Double>> tfs, Query q, 
			Map<String, Double> tfQuery, Document d, Map<String, Double> idfs, int numDocs, double smallestWindowConstant) throws Exception
	{
		double score = 0.0;

		for (String type : tfs.keySet()) {
			double currTotal = 0;
			for (String term : tfs.get(type).keySet()) {
				if (!tfQuery.containsKey(term)) {
					throw new Exception("Exception in getNetScore(): KEYS ARE NOT THE SAME!");
				}		
				
				double idfComponent = -1;
				if (idfs.containsKey(term)) {
					idfComponent = idfs.get(term);
				} else {
					idfComponent = Math.log10(idfs.size() + numDocs);
				}
				
				currTotal += tfs.get(type).get(term) * tfQuery.get(term) * idfComponent;
			}
			
			score += weightParams.get(type) * currTotal * smallestWindowConstant;
		}

		return score;
	}


	public void normalizeTFs(Map<String, Map<String, Double>> tfs, Document d, Query q)
	{
		for (String type : tfs.keySet()) {			
			for (String term : tfs.get(type).keySet()) {
				double tf = tfs.get(type).get(term);
				double newVal = 0;
				if (tf != 0) {
					newVal = (1.0 + Math.log10(tf)) / (smoothingBodyLength + d.body_length);
				}
				tfs.get(type).put(term, newVal);
			}
		}
	}


	@Override
	public double getSimScore(Document d, Query q, Map<String,Double> idfs, int numDocs) throws Exception 
	{
		Map<String,Map<String, Double>> tfs = this.getDocTermFreqs(d, q);
		this.normalizeTFs(tfs, d, q);

		Map<String,Double> tfQuery = getQueryFreqs(q);
		
		return getNetScore(tfs, q, tfQuery, d, idfs, numDocs, 1.0);
	}

}
