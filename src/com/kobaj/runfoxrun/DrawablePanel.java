package com.kobaj.runfoxrun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//lower level panel that handles ugly surface changes that we don't want to see in our surface panel
public abstract class DrawablePanel extends SurfaceView implements SurfaceHolder.Callback, ISurface
{
	private CustomThread thread;
	
	public DrawablePanel(Context context)
	{
		super(context);
		getHolder().addCallback(this);
	}
	
	public void surfaceCreated(SurfaceHolder holder)
	{
		if(thread == null)
		{
			thread = new CustomThread(getHolder(), this);
			thread.start();
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		// do nothing for now.
	}
	
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		thread.stop();
	}
	
	public void stopThread()
	{
		thread.stop();
	}
	
	public void startThread()
	{
		thread.start();
	}
	
	public void restartThread()
	{
		if(thread != null)
		{
			thread.restart();
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		// reset everything.
		canvas.drawColor(Color.BLACK);
	}
}
