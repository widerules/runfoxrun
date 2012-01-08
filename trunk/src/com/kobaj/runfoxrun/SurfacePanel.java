package com.kobaj.runfoxrun;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

//surface class that updates and draws everything.
public class SurfacePanel extends DrawablePanel
{	
	private int width;
	private int height;
	
	private FPSManager fps;
	public InputManager im;
	private SoundManager sm;
	private PhysicsManager pm;
	
	private Sprite little;
	private Sprite BigAnimate;
	
	//construct our objects
	public SurfacePanel(Context context)
	{	
		super(context);
		
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		
		fps = new FPSManager();
		im = new InputManager();
		
		sm = new SoundManager(context);
		
		pm = new PhysicsManager(width, height);
		
		little = new Sprite();
		BigAnimate = XMLHandler.readSerialFile(getResources(), R.raw.haloperms, Sprite.class);
	}
	
	//load in our resources
	public void onInitalize()
	{
		sm.addSound(0, R.raw.collision);
		
		pm.setScrollRate(-1);
		
		little.onInitalize(getResources(), R.drawable.untitled, 30, 40, 26, 50);
		BigAnimate.onInitalize(getResources(), R.drawable.haloperms, (int)(width / 3.0f), 190, 33, 49);
		BigAnimate.setAnimation(CharStates.Running);
		
		pm.setPlayer(BigAnimate);
		pm.addPhys(little);
	}
	
	private int x = 1;
	
	public void onUpdate(long gameTime)
	{
		fps.onUpdate(gameTime);
		im.onUpdate();
		pm.onUpdate(fps.getDelta());
		//everything below this line
		
		for(int i = 0; i < im.fingerCount; i++)
		{
			if(im.getPressed(i))
			{
				little.setxPos((int) im.getX(i));
				little.setyPos((int) im.getY(i));
				sm.playSound(0);
			}
		}
		
		BigAnimate.onUpdate(fps.getDelta());
	}
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		Paint textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setStrokeWidth(8);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(24);
		
		//fps output
		canvas.drawText("FPS " + String.valueOf(fps.getFPS()), 10, 30, textPaint);
		
		//velocity
		canvas.drawText("Velocity " + String.valueOf(pm.getVle()), 100, 30, textPaint);
		
		little.onDraw(canvas);
		BigAnimate.onDraw(canvas);
	}
	
	public void onScreenPause()
	{
		//when the game is paused by turning off.
		
	}
	
	private void onPlayerPause()
	{
		//TODO implement when the player pressses pause button
	}
	
	public void onScreenResume()
	{
		
	}
	
	public void onPlayerResume()
	{
		
	}

}
