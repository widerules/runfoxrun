package com.kobaj.runfoxrun;

import org.simpleframework.xml.Element;

public class HighScores
{
	@Element
	private  int scores = 0;
	
	@Element
	private  int obtLevel = 1;

	public  void setLevel(int level)
	{
		if(level > obtLevel)
			obtLevel = level;
	}
	
	public  int getLevel()
	{
		return obtLevel;
	}
	
	public  void addScore(int score)
	{	
		if(score > scores)
			scores = score;
	}
	
	public  int getHighScore()
	{
		return scores;
	}
}
