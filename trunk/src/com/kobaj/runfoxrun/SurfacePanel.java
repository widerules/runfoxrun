package com.kobaj.runfoxrun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.Display;
import android.view.WindowManager;

//surface class that updates and draws everything.
public class SurfacePanel extends DrawablePanel
{	
	private GameStates currentState;
	private GameStates oldState;
	
	private int width;
	private int height;
	
	private FPSManager fps;
	public InputManager im;
	private SoundManager sm;
	private PhysicsManager pm;
	
	private Sprite BigAnimate;
	
	private TitleScreen ts;
	private ContinousScreen cous;
	private PauseScreen ps;
	private SinglePlayScreen sp;
	
	//semi arbitrary
	
	private Paint textPaint = new Paint();

	//might be changed to an image
	private custString pauseText;
	
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
		pm.setScrollRate(-15);
		
		currentState = GameStates.TitleScreen;
		oldState = GameStates.TitleScreen;
		
		ts = new TitleScreen();
		cous = new ContinousScreen(width, height);
		ps = new PauseScreen();
		ps.onInitialize(getResources(), R.drawable.titlescreen);
		sp = new SinglePlayScreen(width, height);
		
		pauseText = new custString("PAUSE", 7, 30);
		pauseText.setSize(32);
		
		//semi arbitrary
		textPaint.setColor(Color.WHITE);
		textPaint.setStrokeWidth(8);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(24);
		
		//arbitrary goes below here.
		BigAnimate = XMLHandler.readSerialFile(getResources(), R.raw.haloperms, Sprite.class);
	}
	
	//load in our resources
	public void onInitialize()
	{	
		ts.onInitialize(getResources(), R.drawable.titlescreen);
		
		//semi arbitrary
		
		pm.setPlayer(BigAnimate);
		
		LoadedResources.load(getResources());
		
		//arbitrary
		
		sm.addSound(0, R.raw.collision);
		
		BigAnimate.onInitialize(getResources(), R.drawable.haloperms, (int)(width / 3.0f), 190, 33, 49);
		BigAnimate.setAnimation(CharStates.Running);
	}
	
	public void onUpdate(long gameTime)
	{
		fps.onUpdate(gameTime);
		im.onUpdate();
		sm.onUpdate(fps.getDelta());
		
		if(currentState == GameStates.TitleScreen)
			onTitleScreen();
		else if(currentState == GameStates.SinglePlay)
		{
			//uncomment later
			BigAnimate.onUpdate(fps.getDelta());
			pm.onUpdate(fps.getDelta());
			sp.onUpdate();
			checkForUserPause();
		}
		else if(currentState == GameStates.Continous)
		{
			BigAnimate.onUpdate(fps.getDelta());
			pm.onUpdate(fps.getDelta());
			cous.onUpdate(fps.getDelta());
			checkForUserPause();
		}
		else if(currentState == GameStates.Pause)
			onPauseScreen();
		else if(currentState == GameStates.Loading)
			onLoadingScreen(fps.getDelta());
	}

	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		if(currentState == GameStates.TitleScreen)
			ts.onDraw(canvas);
		else if (currentState == GameStates.SinglePlay)
		{
			//BigAnimate.onDraw(canvas); now handled inside single play
			sp.onDraw(canvas);
			pauseText.onDraw(canvas);
		}
		else if(currentState == GameStates.Continous)
		{
			BigAnimate.onDraw(canvas);
			cous.onDraw(canvas);
			pauseText.onDraw(canvas);
		}
		else if(currentState == GameStates.Pause)
			ps.onDraw(canvas);
		else if(currentState == GameStates.Loading)
			onDrawLoadingScreen(canvas);
		
		//fps output
		canvas.drawText("FPS " + String.valueOf(fps.getFPS()), width - textPaint.measureText("FPS " + String.valueOf(fps.getFPS())), height, textPaint);
	}
	
	int rotation = 0;
	private void onLoadingScreen(float delta)
	{
		rotation += delta / 10.0f;
	}
	
	private void onDrawLoadingScreen(Canvas canvas)
	{
		canvas.save();
		canvas.rotate(rotation, width / 2, height / 2);
		canvas.drawText("Loading...", width / 2 - textPaint.measureText("Loading") / 2, height / 2 - textPaint.getTextSize() * 2, textPaint);
		canvas.restore();
		
		if(sp.getInitialized())
		{
			oldState = GameStates.Loading;
			currentState = GameStates.SinglePlay;
		}		
	}
	
	private void checkForUserPause()
	{
		for(int i = 0; i < im.fingerCount; i++)
		{
			if(im.getReleased(i))
			{
			if(pauseText.fingertap((int) im.getY(i), (int) im.getY(i)))
			{
				oldState = currentState;
				currentState = GameStates.Pause;
			}
			}
		}
	}
	
	//also special
	private void onPauseScreen()
	{
		GameStates newState = GameStates.Pause;
		
		for(int i = 0; i < im.fingerCount; i++)
		{
			if(im.getReleased(i))
			{
			newState = ps.onTouch((int) im.getX(i), (int) im.getY(i));
			
			if(newState != GameStates.Pause)
				break;
			}
		}
		
		if(newState == GameStates.Quit)
		{
			onUserQuit(); 
		}
		else if(newState == GameStates.TitleScreen)
		{
			oldState = GameStates.Pause;
			currentState = GameStates.TitleScreen;
		}
		else if(newState == GameStates.Resume)
		{
			currentState = oldState;
			oldState = GameStates.Pause;
		}
	}
	
	//special cause it handles a lot of stuff.
	private void onTitleScreen()
	{
		GameStates newState = GameStates.TitleScreen;
		
		for(int i = 0; i < im.fingerCount; i++)
		{
			if(im.getReleased(i))
			{
			newState = ts.onTouch((int) im.getX(i), (int) im.getY(i));
			
			if(newState != GameStates.TitleScreen)
				break;
			}
		}
		
		if(newState == GameStates.Quit)
		{
			onUserQuit(); 
		}
		else if(newState == GameStates.SinglePlay)
		{
			purgeManagers();
			sp = new SinglePlayScreen(width, height);
			sp.onInitialize(getResources(), im, pm, R.raw.level, BigAnimate);
			oldState = GameStates.SinglePlay;
			currentState = GameStates.Loading;
		}
		else if(newState == GameStates.Continous)
		{
			purgeManagers();
			cous = new ContinousScreen(width, height);
			cous.onInitialize(getResources(), im, pm, BigAnimate);
			oldState = GameStates.TitleScreen;
			currentState = GameStates.Continous;
		}
	}
	
	private void purgeManagers()
	{
		pm.purge();
		sm.purge();
	}
	
	public void onScreenPause()
	{
		//when the game is paused by turning off.
		
	}

	public void onScreenResume()
	{
		
	}
	
	private void onUserQuit()
	{
		//probably save stuff before actually quitting.
		System.exit(0); 
	}

}
