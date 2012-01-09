package com.kobaj.runfoxrun;

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
		;//blank
	}
	
	public void onUpdate(long gameTime)
	{
		nowTime = gameTime;
		
		delta = nowTime - lastTime;
		
		if(reset < wait)
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
	
	public float getDelta()
	{
		return Math.max(Math.min(delta, 10000), 0);
	}
}
