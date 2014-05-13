package edu.stanford.cs276;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Query
{

	List<String> queryWords;
	public String queryString;
	
	public Query(String query)
	{
		queryWords = new ArrayList<String>(Arrays.asList(query.split(" ")));
		queryString = query;
	}
	
}
