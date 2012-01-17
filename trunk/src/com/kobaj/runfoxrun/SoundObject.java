package com.kobaj.runfoxrun;

public class SoundObject
{
	private int soundPoolint;
	private int index;
	
	private LoadStates load = LoadStates.notStarted;
	
	public SoundObject(int index, int soundPool)
	{
		this.index = index;
		this.soundPoolint = soundPool;
		
		setLoad(LoadStates.loading);
	}
	
	public int getSoundPoolint()
	{
		return soundPoolint;
	}
	
	public void setSoundPoolint(int soundPoolint)
	{
		this.soundPoolint = soundPoolint;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}
	
	public LoadStates getLoad()
	{
		return load;
	}
	
	public void setLoad(LoadStates load)
	{
		this.load = load;
	}
}
