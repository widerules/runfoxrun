package com.kobaj.runfoxrun;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

//threading!
public class CustomThread implements Runnable
{
	private Thread thread;
	
	private SurfaceHolder surfaceHolder;
	private ISurface panel;
	private boolean run = false;
	
	public CustomThread(SurfaceHolder surfaceHolder, ISurface panel)
	{
		this.surfaceHolder = surfaceHolder;
		this.panel = panel;
		
		this.panel.onInitialize();
	}
	
	public void start()
	{
		run = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void restart()
	{
		if(thread != null)
		{
			run = true;
			thread.start();
		}
		else
			throw new Exception("Cannot restart, thread not specified");
	}
	
	public void stop()
	{
		run = false;
	}
	
	@Override
	public void run()
	{
		Canvas c;
		while (run)
		{
			c = null;
			panel.onUpdate(System.currentTimeMillis());
			
			try
			{
				c = surfaceHolder.lockCanvas(null);
				synchronized (surfaceHolder)
				{
					panel.onDraw(c);
				}
			}
			catch (Exception e)
			{
				// do nothing?
			}
			finally
			{
				if (c != null)
				{
					surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}
}
