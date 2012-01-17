package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Collections;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class HighScores
{
	@ElementList
	private static ArrayList<Integer> scores;
	
	private static int maxscores = 5;
	
	@Element
	private static int obtLevel;
	
	public HighScores()
	{
		if (scores == null)
		{
			scores = new ArrayList<Integer>();
			
			for (int i = 0; i < maxscores; i++)
			{
				scores.add(0);
			}
		}
		
		obtLevel = 1;
	}

	public static void setLevel(int level)
	{
		obtLevel = level;
	}
	
	public static int getLevel()
	{
		return obtLevel;
	}
	
	public static void addScore(int score)
	{	
		scores.add(score);
		
		Collections.sort(scores);
		
		while(scores.size() > maxscores)
		{
			scores.remove(0);
		}
	}
	
	public static ArrayList<Integer> getScores()
	{
		return scores;
	}
}
