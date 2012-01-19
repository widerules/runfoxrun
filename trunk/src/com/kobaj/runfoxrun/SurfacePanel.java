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
	
	public static final float scrollRate = -17.0f / 100.0f;
	
	// semi arbitrary
	private Paint textPaint = new Paint();
	private Sprite loadingStar;
	
	// might be changed to an image
	private custString pauseText;
	
	private float scale;
	
	private HighScores highScores;
	
	// stat holding
	@SuppressWarnings("unused")
	private int currentSong;
	@SuppressWarnings("unused")
	private Context context;
	
	@SuppressWarnings("unused")
	private boolean initialized = false;
	
	// construct our objects
	public SurfacePanel(Context context)
	{
		super(context);
		
		this.context = context;
		
		this.scale = getResources().getDisplayMetrics().density;
		
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
		mm.addFade(new SoundFade(0, 0, 1, 3000));
		mm.play(0);
		
		currentState = GameStates.Loading;
		oldState = GameStates.TitleScreen;
		
		ts = new TitleScreen();
		cous = new ContinousScreen(width, height);
		ps = new PauseScreen();
		sp = new SinglePlayScreen(width, height);
		
		pauseText = new custString(getResources(), "PAUSE", (int) (5 * scale), (int) (20 * scale));
		pauseText.setSize((int) (21 * scale));
		
		// semi arbitrary
		textPaint.setColor(Color.WHITE);
		textPaint.setStrokeWidth(8);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(16 * scale);
	}
	
	// load in our resources
	public void onInitialize()
	{
		// really silly way of doing this
		highScores = XMLHandler.readSerialFile("highscores", HighScores.class);
		if (highScores == null)
			highScores = new HighScores();
		
		// originally in constructor
		mainFox = XMLHandler.readSerialFile(getResources(), R.raw.foxmain, Sprite.class);
		loadingStar = XMLHandler.readSerialFile(getResources(), R.raw.star, Sprite.class);

		LoadedResources.load(getResources());

		loadingStar.onInitialize(LoadedResources.getStar(getResources()), width / 2 - 12, height / 2, 25, 24);
		
		ts.onInitialize(getResources(), R.drawable.titlescreen, mm);
		ps.onInitialize(getResources(), 0);
		
		pm.setPlayer(mainFox);
		mainFox.onInitialize(LoadedResources.getMainFox(), sm, (int) (width / 3.0f), -100, 82, 54);
		mainFox.setAnimation(CharStates.Running);
		
		smSetup();
		
		initialized = true;
	}
	
	private void smSetup()
	{
		sm.addSound(1, R.raw.footstep);
		sm.addSound(2, R.raw.pkup1);
		sm.addSound(3, R.raw.death);
		sm.addSound(4, R.raw.rumbling1);
		sm.addSound(5, R.raw.landing);
	}
	
	public void onUpdate(long gameTime)
	{
		fps.onUpdate(gameTime);
		float delta = fps.getDelta();
		sm.onUpdate(delta);
		mm.onUpdate(delta);
		
		if (currentState == GameStates.TitleScreen)
			onTitleScreen(delta);
		else if (currentState == GameStates.SinglePlay)
		{
			mainFox.onUpdate(fps.getDelta());
			pm.onUpdate(delta);
			if (!sp.onUpdate(delta))
			{
				currentState = GameStates.TitleScreen;
				oldState = GameStates.SinglePlay;
			}
			checkForUserPause();
		}
		else if (currentState == GameStates.Continous)
		{
			mainFox.onUpdate(delta);
			pm.onUpdate(delta);
			cous.onUpdate(delta);
			checkForUserPause();
		}
		else if (currentState == GameStates.Pause)
			onPauseScreen();
		else if (currentState == GameStates.Loading)
			onLoadingScreen(delta);
	}
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		if (currentState == GameStates.TitleScreen)
			ts.onDraw(canvas);
		else if (currentState == GameStates.SinglePlay)
		{
			// BigAnimate.onDraw(canvas); now handled inside single play
			sp.onDraw(canvas);
			pauseText.onDraw(canvas);
		}
		else if (currentState == GameStates.Continous)
		{
			cous.onDraw(canvas);
			pauseText.onDraw(canvas);
			mainFox.onDraw(canvas);
		}
		else if (currentState == GameStates.Pause && oldState == GameStates.SinglePlay)
		{
			sp.onDraw(canvas);
			ps.onDraw(canvas);
		}
		else if (currentState == GameStates.Pause && oldState == GameStates.Continous)
		{
			cous.onDraw(canvas);
			mainFox.onDraw(canvas);
			ps.onDraw(canvas);
		}
		else if (currentState == GameStates.Loading)
			onDrawLoadingScreen(canvas);
	}
	
	private void onLoadingScreen(float delta)
	{
		loadingStar.onUpdate(delta);
		
		if (oldState == GameStates.SinglePlay)
			if (sp.getInitialized())
			{
				oldState = GameStates.Loading;
				currentState = GameStates.SinglePlay;
			}
		
		if (oldState == GameStates.TitleScreen)
		{
			if (mm.isLoaded() && sm.isAllLoaded())
			// if(sm.isLoaded(0) == LoadStates.complete)
			{
				oldState = GameStates.Loading;
				currentState = GameStates.TitleScreen;
			}
		}
	}
	
	private void onDrawLoadingScreen(Canvas canvas)
	{
		loadingStar.onDraw(canvas);
		canvas.drawText("Loading...", width / 2 - textPaint.measureText("Loading...") / 2, height / 2 + 25 + 24, textPaint);
	}
	
	private void checkForUserPause()
	{
		for (int i = 0; i < im.fingerCount; i++)
		{
			if (im.getReleased(i))
			{
				if (pauseText.fingertap((int) im.getY(i), (int) im.getY(i)))
				{
					oldState = currentState;
					currentState = GameStates.Pause;
					mm.setVolume(.20f);
				}
			}
		}
	}
	
	// also special
	private void onPauseScreen()
	{
		GameStates newState = GameStates.Pause;
		
		for (int i = 0; i < im.fingerCount; i++)
		{
			if (im.getReleased(i))
			{
				newState = ps.onTouch((int) im.getX(i), (int) im.getY(i));
				
				if (newState != GameStates.Pause)
				{
					if(oldState == GameStates.Continous)
						HighScores.addScore(cous.getScore());
					
					break;
				}
			}
		}
		
		if (newState == GameStates.Quit)
		{
			onUserQuit();
		}
		else if (newState == GameStates.TitleScreen)
		{
			if(oldState == GameStates.SinglePlay)
				sp.Release();
			
			oldState = GameStates.Pause;
			currentState = GameStates.TitleScreen;
			//mm.addFade(new SoundFade(0, 1, 0, 3000));
			mm.ChangeSongs(R.raw.pulse, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
		}
		else if (newState == GameStates.Resume)
		{
			currentState = oldState;
			oldState = GameStates.Pause;
			mm.setVolume(1f);
		}
	}
	
	// special cause it handles a lot of stuff.
	// should really be inside of ts
	private void onTitleScreen(float delta)
	{
		// sounds
		ts.onUpdate(delta);
		
		GameStates newState = GameStates.TitleScreen;
		
		for (int i = 0; i < im.fingerCount; i++)
		{
			if (im.getReleased(i))
			{
				newState = ts.onTouch((int) im.getX(i), (int) im.getY(i));
				
				if (newState != GameStates.TitleScreen)
					break;
			}
		}
		
		if (newState == GameStates.Quit)
		{
			onUserQuit();
		}
		else if (newState == GameStates.SinglePlay)
		{
			purgeManagers();
			sp = new SinglePlayScreen(width, height);
			sp.onInitialize(getResources(), im, pm, sm, mm, R.raw.level, mainFox);
			oldState = GameStates.SinglePlay;
			currentState = GameStates.Loading;
			
			ts.titleScreenCurrentSong = 0;
			ts.titleScreenSoundTime = 3000000;
			mm.addFade(new SoundFade(0, 1, 0, 3000));
		}
		else if (newState == GameStates.Continous)
		{
			purgeManagers();
			cous = new ContinousScreen(width, height);
			cous.onInitialize(getResources(), im, pm, sm, mainFox);
			oldState = GameStates.TitleScreen;
			currentState = GameStates.Continous;
			
			mm.ChangeSongs(R.raw.catchinglightning, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
		}
		
		// ran out of time!
		/*
		 * else if (newState == GameStates.HighScore) { oldState =
		 * GameStates.TitleScreen; currentState = GameStates.HighScore; }
		 */
	}
	
	private void purgeManagers()
	{
		pm.purge();
		// sm.purge();
	}
	
	public void onScreenPause()
	{
		// when the game is paused by outside shit.
		this.oldState = this.currentState;
		this.currentState = GameStates.Pause;
		mm.stop();
		currentSong = mm.getCurrentSong();
		mm.release();
		sm.pauseAll();
		sm.release();
		XMLHandler.writeSerialFile(highScores, "highscores");
		System.exit(0);
	}
	
	public void onUserPause()
	{
		if(this.currentState != GameStates.TitleScreen)
		{
			this.oldState = this.currentState;
			this.currentState = GameStates.Pause;
		}
	}
	
	public void onScreenResume()
	{
		// should really have gotten this working >.<
	}
	
	public void onUserQuit()
	{
		// probably save stuff before actually quitting.
		sm.release();
		mm.stop();
		mm.release();
		stopThread();
		XMLHandler.writeSerialFile(highScores, "highscores");
		System.exit(0);
	}
	
}
