package com.kobaj.runfoxrun;

import org.simpleframework.xml.Element;

public class HighScores
{
	@Element
	private static int scores = 0;
	
	@Element
	private static int obtLevel;

	public static void setLevel(int level)
	{
		if(level > obtLevel)
			obtLevel = level;
	}
	
	public static int getLevel()
	{
		return obtLevel;
	}
	
	public static void addScore(int score)
	{	
		if(score > scores)
			scores = score;
	}
	
	public static int getHighScore()
	{
		return scores;
	}
}
