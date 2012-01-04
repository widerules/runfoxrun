package com.kobaj.runfoxrun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.SurfaceHolder;

//surface class that updates and draws everything.
public class SurfacePanel extends DrawablePanel
{
	
	//will later be moved to its own class
	//TODO move this to its own class.
	private Bitmap img;
	
	//TODO move this to its own class.
	private long fps = 0;
	private long lastTime = 0;
	private long nowTime = 0;
	

	public SurfacePanel(Context context)
	{
		super(context);
	}
	
	public void onInitalize()
	{
		img = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	}
	
	public void onUpdate(long gameTime)
	{
		nowTime = gameTime;
		
		fps = (long) ((1.0 / (nowTime - lastTime)) * 1000.0);
		
		lastTime = nowTime;
	}
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		Paint textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setStrokeWidth(2);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		
		//fps output
		canvas.drawText(String.valueOf(fps), 10, 10, textPaint);
		
		
		canvas.drawBitmap(img, 20, 20, null);
	}

}
