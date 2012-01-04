package com.kobaj.runfoxrun;

import android.graphics.Canvas;

public interface ISurface
{
	public void onInitalize();
	public void onDraw(Canvas canvas);
	public void onUpdate(long gameTime);
}
