package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Random;

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
	private GameStates currentState;
	
	private int width;
	private int height;
	
	private FPSManager fps;
	public InputManager im;
	private SoundManager sm;
	private PhysicsManager pm;
	
	private Sprite BigAnimate;
	
	Paint textPaint = new Paint();
	
	TitleScreen ts;
	
	ArrayList<Sprite> hitList;
	
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
		pm.setScrollRate(-10);
		
		currentState = GameStates.TitleScreen;
		
		//semi arbitrary
		textPaint.setColor(Color.WHITE);
		textPaint.setStrokeWidth(8);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(24);
		
		ts = new TitleScreen();
		
		hitList = new ArrayList<Sprite>();
		
		//arbitrary goes below here.
		BigAnimate = XMLHandler.readSerialFile(getResources(), R.raw.haloperms, Sprite.class);
	}
	
	//load in our resources
	public void onInitalize()
	{
		//semi arbitrary
		
		ts.onInitialize(getResources(), R.drawable.titlescreen);
		
		for(int i = 0; i < 4; i++)
			hitList.add(new Sprite());
		
		for(int i = 0; i < 4; i++)
			hitList.get(i).onInitalize(getResources(), R.drawable.green, i * 180, height - 10);
		
		//arbitrary
		
		sm.addSound(0, R.raw.collision);
		
		BigAnimate.onInitalize(getResources(), R.drawable.haloperms, (int)(width / 3.0f), 190, 33, 49);
		BigAnimate.setAnimation(CharStates.Running);
	}
	
	public void onUpdate(long gameTime)
	{
		fps.onUpdate(gameTime);
		im.onUpdate();
		
		if(currentState == GameStates.TitleScreen)
			onTitleScreen();
		else if(currentState == GameStates.SinglePlay)
			onSinglePlay();
	}
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		if(currentState == GameStates.TitleScreen)
			onDrawTitleScreen(canvas);
		else if (currentState == GameStates.SinglePlay)
			onDrawSinglePlay(canvas);
		
		//fps output
		canvas.drawText("FPS " + String.valueOf(fps.getFPS()), 10, 30, textPaint);
	}
	
	private void onTitleScreen()
	{
		GameStates newState = GameStates.TitleScreen;
		
		for(int i = 0; i < im.fingerCount; i++)
		{
			newState = ts.onTouch((int) im.getX(i), (int) im.getY(i));
			
			if(newState != GameStates.TitleScreen)
				break;
		}
		
		if(newState == GameStates.SinglePlay)
		{
			//normally go to loading, load in shit, and then go to single play
			currentState = GameStates.SinglePlay;
			
			//load arbitrary
			pm.setPlayer(BigAnimate);
			for(int i = 0; i < 4; i++)
				pm.addPhys(hitList.get(i));
		}
	}
	
	Random rand = new Random();
	int nextval = 0;;
	private void onSinglePlay()
	{
		pm.onUpdate(fps.getDelta());
		//everything below this line
		
		if(pm.getPhyObjCount() < 7)
		{
			int blah = rand.nextInt(4);
			
			if(blah == 1)
			{
				Sprite green = new Sprite();
				green.onInitalize(getResources(), R.drawable.green, rand.nextInt(800) + 800, rand.nextInt(370) + 30);
				hitList.add(green);
				pm.addPhys(green);
				//green
			}
			
			if(blah == 2)
			{
				Sprite green = new Sprite();
				green.onInitalize(getResources(), R.drawable.red, rand.nextInt(800) + 800, rand.nextInt(370) + 30);
				hitList.add(green);
				pm.addPhys(green);	
			}
			
			if(blah == 3)
			{
				Sprite green = new Sprite();
				green.onInitalize(getResources(), R.drawable.blue, rand.nextInt(800) + 800, rand.nextInt(370) + 30);
				hitList.add(green);
				pm.addPhys(green);
			}
			
			if(blah > 3)
			{
				//space
			}
		}
		
		for(int i = 0; i < im.fingerCount; i++)
		{
			if(im.getPressed(i))
			{
				pm.jump();
			}
		}
		
		BigAnimate.onUpdate(fps.getDelta());
	}
	
	private void onDrawTitleScreen(Canvas canvas)
	{
		ts.onDraw(canvas);
	}
	
	private void onDrawSinglePlay(Canvas canvas)
	{		
		BigAnimate.onDraw(canvas);
		
		int notdeleted = hitList.size() - pm.getPhyObjCount();
		
		if(notdeleted - 5 > 0)
			notdeleted = notdeleted - 5;
		
		//for(int i = 0; i < hitList.size(); i++)
		for(int i = hitList.size() - 1; i >= notdeleted; i = i-1)
		{
			hitList.get(i).onDraw(canvas);
		}
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
