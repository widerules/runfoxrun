package com.kobaj.runfoxrun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

//surface class that updates and draws everything.
public class SurfacePanel extends DrawablePanel
{	
	FPSManager fps;
	
	//will later be moved to its own class
	//TODO move this to its own class.
	private Bitmap img;
	
	private Sprite little;
	private Sprite BigAnimate;
	
	//construct our objects
	public SurfacePanel(Context context)
	{
		
		super(context);
		
		fps = new FPSManager();		
		little = new Sprite();
		BigAnimate = new XMLHandler().readSerialFile(getResources(), R.raw.haloperms, Sprite.class);
	}
	
	//load in our resources
	public void onInitalize()
	{
		img = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		
		little.onInitalize(getResources(), R.drawable.untitled, 30, 40, 26, 50);
		BigAnimate.onInitalize(getResources(), R.drawable.haloperms, 30, 190, 33, 49);
		BigAnimate.setAnimation(CharStates.Running);
	}
	
	private int x = 1;
	
	public void onUpdate(long gameTime)
	{
		fps.onUpdate(gameTime);
		
		little.onUpdate(fps.getDelta());
		x += 1;
		if(x > 300)
		{
			x = 0;
		}
		little.setxPos(x);
		
		BigAnimate.onUpdate(fps.getDelta());
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
		canvas.drawText(String.valueOf(fps.getFPS()), 10, 10, textPaint);
		
		
		canvas.drawBitmap(img, 20, 20, null);
		
		little.onDraw(canvas);
		BigAnimate.onDraw(canvas);
	}

}
