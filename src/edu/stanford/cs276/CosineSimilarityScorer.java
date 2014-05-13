package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CosineSimilarityScorer extends AScorer
{
	///////////////weights///////////////////////////
	
	private static double urlweight = -1;
	private static double titleweight  = -1;
	private static double bodyweight = -1;
	private static double headerweight = -1;
	private static double anchorweight = -1;
	private static double smoothingBodyLength = 500;

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

	public double getNetScore(Map<String, Map<String, Double>> tfs, Query q, Map<String,Double> tfQuery, Document d) throws Exception
	{
		double score = 0.0;

		for (String type : tfs.keySet()) {
			double currTotal = 0;
			for (String term : tfs.get(type).keySet()) {
				if (!tfQuery.containsKey(term)) {
					throw new Exception("Exception in getNetScore(): KEYS ARE NOT THE SAME!");
				}
				currTotal += tfs.get(type).get(term) * tfQuery.get(term);
			}
			
			score += weightParams.get(type) * currTotal;
		}

		return score;
	}


	public void normalizeTFs(Map<String, Map<String, Double>> tfs, Document d, Query q)
	{
		for (String type : tfs.keySet()) {			
			for (String term : tfs.get(type).keySet()) {
				double newVal = (1.0 + Math.log(tfs.get(type).get(term))) / (smoothingBodyLength + d.body_length);
				tfs.get(type).put(term, newVal);
			}
		}
	}


	@Override
	public double getSimScore(Document d, Query q) throws Exception 
	{
		Map<String,Map<String, Double>> tfs = this.getDocTermFreqs(d, q);
		this.normalizeTFs(tfs, d, q);

		Map<String,Double> tfQuery = getQueryFreqs(q);

		return getNetScore(tfs, q, tfQuery, d);
	}





}
