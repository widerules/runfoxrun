package com.kobaj.runfoxrun;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class ListScreens
{
	protected Bitmap imgBackdrop;
	protected custString[] stringList;
	
	protected int count;
	
	protected boolean initialized = false;
	
	public abstract void onInitialize(Resources rescoures, int identity);
	
	public void onDraw(Canvas canvas)
	{
		if(initialized)
		{
		canvas.drawBitmap(imgBackdrop, 0, 0, null);
		
		//array yay :D
		for(int i = 0; i < count; i++)
			stringList[i].onDraw(canvas);
		
		}
	}
}
