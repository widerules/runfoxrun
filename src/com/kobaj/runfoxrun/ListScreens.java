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
	
	public void onDraw(Canvas canvas, GameStates current)
	{
		if (initialized)
		{
			if (imgBackdrop != null)
				canvas.drawBitmap(imgBackdrop, -1, 1, null);
			
			// array yay :D
			if (imgBackdrop != null)
			{
				canvas.save();
				canvas.rotate(-27, imgBackdrop.getWidth() / 2, imgBackdrop.getHeight() / 2);
			}
			
			for (int i = 0; i < count; i++)
				if(stringList[i] != null)
					if(current == GameStates.Continous && (i == 2 || i > 3))
					{
						//do nothing.
					}
					else
						stringList[i].onDraw(canvas);
			
			if (imgBackdrop != null)
				canvas.restore();
			
		}
	}
}
