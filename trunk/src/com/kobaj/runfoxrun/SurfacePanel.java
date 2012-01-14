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
	private MusicManager mm;
	
	private Sprite mainFox;
	
	private TitleScreen ts;
	private ContinousScreen cous;
	private PauseScreen ps;
	private SinglePlayScreen sp;
	
	public static final float scrollRate = -17.0f  / 100.0f;
	
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
		pm.setScrollRate(scrollRate);
		
		mm = new MusicManager(context, R.raw.pulse);
		mm.setLooping(true);
		
		currentState = GameStates.Loading;
		oldState = GameStates.TitleScreen;
		
		ts = new TitleScreen();
		cous = new ContinousScreen(width, height);
		ps = new PauseScreen();
		ps.onInitialize(getResources(), R.drawable.titlescreen);
		sp = new SinglePlayScreen(width, height);
		
		pauseText = new custString("PAUSE", 7, 30);
		pauseText.setSize(32);
		
		mainFox = XMLHandler.readSerialFile(getResources(), R.raw.foxmain, Sprite.class);
		
		//semi arbitrary
		textPaint.setColor(Color.WHITE);
		textPaint.setStrokeWidth(8);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(24);
	}
	
	//load in our resources
	public void onInitialize()
	{	
		ts.onInitialize(getResources(), R.drawable.titlescreen);
		
		pm.setPlayer(mainFox);
		mainFox.onInitialize(getResources(),sm, R.drawable.foxmain, (int)(width / 3.0f), -100, 82, 54);
		mainFox.setAnimation(CharStates.Running);
		
		sm.addSound(1, R.raw.footstep);
		sm.addSound(2, R.raw.pkup1);
		
		LoadedResources.load(getResources());
	}
	
	public void onUpdate(long gameTime)
	{
		fps.onUpdate(gameTime);
		sm.onUpdate(fps.getDelta());
		mm.onUpdate(fps.getDelta());
		
		if(currentState == GameStates.TitleScreen)
			onTitleScreen(fps.getDelta());
		else if(currentState == GameStates.SinglePlay)
		{
			mainFox.onUpdate(fps.getDelta());
			pm.onUpdate(fps.getDelta());
			sp.onUpdate(fps.getDelta());
			checkForUserPause();
		}
		else if(currentState == GameStates.Continous)
		{
			mainFox.onUpdate(fps.getDelta());
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
			cous.onDraw(canvas);
			pauseText.onDraw(canvas);
			mainFox.onDraw(canvas);
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
		
		if(oldState == GameStates.SinglePlay)
			if(sp.getInitialized())
			{
				oldState = GameStates.Loading;
				currentState = GameStates.SinglePlay;
				mm.ChangeSongs(R.raw.pulse);
				mm.addFade(new SoundFade(0,0,1,3000));
				mm.play(0);
			}
	
		if(oldState == GameStates.TitleScreen)
		{
			if(!mm.isLoaded() && sm.isAllLoaded())
			//if(sm.isLoaded(0) == LoadStates.complete)
			{
				oldState = GameStates.Loading;
				currentState = GameStates.TitleScreen;
			}
		}
	}
	
	private void onDrawLoadingScreen(Canvas canvas)
	{
		canvas.save();
		canvas.rotate(rotation, width / 2, height / 2);
		canvas.drawText("Loading...", width / 2 - textPaint.measureText("Loading") / 2, height / 2 - textPaint.getTextSize() * 2, textPaint);
		canvas.restore();		
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
					mm.setVolume(.20f);
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
			mm.setVolume(1f);
		}
	}
	
	//special cause it handles a lot of stuff.
	//should really be inside of ts
	private void onTitleScreen(float delta)
	{
		//sounds
		ts.onUpdate(delta, mm);
		
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
			sp.onInitialize(getResources(), im, pm, sm, R.raw.level, mainFox);
			oldState = GameStates.SinglePlay;
			currentState = GameStates.Loading;
			
			ts.titleScreenCurrentSong = 0;
			ts.titleScreenSoundTime = 3000000;
			
			mm.addFade(new SoundFade(0, 1, 0, 3000));
		}
		else if(newState == GameStates.Continous)
		{
			purgeManagers();
			cous = new ContinousScreen(width, height);
			cous.onInitialize(getResources(), im, pm, mainFox);
			oldState = GameStates.TitleScreen;
			currentState = GameStates.Continous;
		}
	}
	
	private void purgeManagers()
	{
		pm.purge();
		//sm.purge();
	}
	
	public void onScreenPause()
	{
		//when the game is paused by turning off.
		mm.release();
	}

	public void onScreenResume()
	{
		
	}
	
	private void onUserQuit()
	{
		//probably save stuff before actually quitting.
		sm.release();
		mm.release();
		System.exit(0); 
	}

}
