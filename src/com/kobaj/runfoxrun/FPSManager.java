package com.kobaj.runfoxrun;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class FPSManager
{
	private int fps = 0;
	private long lastTime = 0;
	private long nowTime = 0;
	private long delta = 0;
	
	private long reset = 0;
	private final long wait = 1000;
	
	public FPSManager()
	{
		;// blank
	}
	
	public void onUpdate(long gameTime)
	{
		nowTime = gameTime;
		
		delta = nowTime - lastTime;
		
		if (reset < wait)
			reset += delta;
		else
		{
			fps = (int) ((1.0 / (delta)) * 1000.0);
			reset = 0;
		}
		
		lastTime = nowTime;
	}
	
	public int getFPS()
	{
		return fps;
	}
	
	// averages
	private int max = 4;
	private Queue<Float> averageList = new LinkedList<Float>();
	private float average = 0;
	
	private float calculateaverage(float newV)
	{
		averageList.offer(newV);
		
		if (averageList.size() > max)
			averageList.poll();
		
		for (Iterator<Float> it = averageList.iterator(); it.hasNext();)
		{
			average += it.next();
		}
		
		average = average / averageList.size();
		
		return average;
	}
	
	public float getDelta()
	{
		return Math.max(Math.min(calculateaverage(delta), 1000), 0);
	}
}
