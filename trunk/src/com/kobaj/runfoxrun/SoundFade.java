package com.kobaj.runfoxrun;

public class SoundFade
{
	private int index;
	private float startVolume;
	private float endVolume;
	private float time;
	
	private float updateTime;
	
	//x is time
	//y is volume
	
	public float getEndVolume()
	{
		return endVolume;
	}
	
	public SoundFade(int index, float startVolume, float endVolume, float time)
	{
		updateTime = 0;
		
		startVolume = clampVolume(startVolume);
		endVolume = clampVolume(endVolume);
		
		if(startVolume == endVolume)
		{
			startVolume = 0;
			endVolume = 1;
		}
		
		if(time == 0)
		{
			time = 3;
		}
		
		this.index = index;
		this.startVolume = startVolume;
		this.endVolume = endVolume;
		this.time = time;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public void onUpdate(float delta)
	{
		updateTime += delta;
	}
	
	public float getVolume()
	{
		if (0 == updateTime)
			return startVolume;
		
		if (time == updateTime)
			return endVolume;
		
		return clampVolume(linInterp(updateTime));
	}
	
	public boolean getValid()
	{
		if(updateTime < time)
			return true;
		
		return false;
	}
	
	private float linInterp(float x)
	{
		return  startVolume*(x - time)/(0 - time) + endVolume*(x - 0)/(time - 0);
	}
	
	private float clampVolume(float value)
	{
		return Math.max(Math.min(value, .99f), 0.0001f);
	}
}
