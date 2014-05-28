package edu.stanford.cs276;

import java.util.Map;

public class ExtraCreditScorer extends AScorer
{

	public ExtraCreditScorer(Map<String,Double> idfs)
	{
		super(idfs);
	}
	
	@Override
	public double getSimScore(Document d, Query q, Map<String,Double> idfs, int numDocs) throws Exception {
		
		return 0;
	}

	/*
	 * @//TODO : Your code here
	 */
}
