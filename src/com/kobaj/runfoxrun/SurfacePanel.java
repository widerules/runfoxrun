package com.kobaj.runfoxrun;

import android.content.Context;
import android.content.SharedPreferences;
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
	private MapMakerScreen MapS;
	
	public static float scrollRate = -17.0f / 100.0f;
	
	// semi arbitrary
	private Paint textPaint = new Paint();
	private Sprite loadingStar;
	
	// might be changed to an image
	private custString pauseText;
	
	public static float scale;
	public static float startHeight;
	
	private HighScores highScores;
	
	public static Context context;

	// construct our objects
	@SuppressWarnings("deprecation")
	public SurfacePanel(Context context)
	{
		super(context);
		
		LoadedResources.preLoad();
		
		SurfacePanel.context = context;
		SurfacePanel.scale = getResources().getDisplayMetrics().density;
		
		//fix dat scale
		int tempwidth = LoadedResources.getBackgroundONE(getResources()).getWidth();
		if(tempwidth == 1072)
			SurfacePanel.scale = 1.0f;
		else if(tempwidth == 800)
			SurfacePanel.scale = .75f;
		else
			SurfacePanel.scale = 1.5f;
		
		scrollRate = (-17.0f / 100.0f) / 1.5f * scale;
		
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
		
		pauseText = new custString(getResources(), "PAUSE", (int) (6 * scale), (int) (22 * scale));
		pauseText.setSize((int) (20 * scale));
		
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

		loadingStar.onInitialize(LoadedResources.getStar(getResources()), (int) (width / 2 - (25.0f / 1.5f * scale) / 2.0f), height / 2, (int)(25.0f / 1.5f * scale), (int)(24.0f  / 1.5f * scale));
		
		ts.onInitialize(getResources(), R.drawable.titlescreen, mm);
		ps.onInitialize(getResources(), 0);
		
		pm.setPlayer(mainFox);
		mainFox.onInitialize(LoadedResources.getMainFox(), sm, (int) (width / 3.0f), -100, (int)(82.0f / 1.5f * scale), (int)(54.0f / 1.5f * scale));
		mainFox.setAnimation(CharStates.Running);
		
		startHeight = height - LoadedResources.getBackground1(getResources()).getHeight() - 100f / 1.5f * SurfacePanel.scale;
		
		smSetup();
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
			mainFox.onUpdate(delta);
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
		else if (currentState == GameStates.MapMaker)
		{
			if(MapS == null)
				currentState = GameStates.TitleScreen;
			else if (!MapS.onUpdate(delta))
			{
				currentState = GameStates.TitleScreen;
				oldState = GameStates.MapMaker;
				mm.ChangeSongs(R.raw.pulse, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
				mm.play();
			}
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		if (currentState == GameStates.TitleScreen)
			ts.onDraw(canvas, currentState);
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
			ps.onDraw(canvas, GameStates.SinglePlay);
		}
		else if (currentState == GameStates.Pause && oldState == GameStates.Continous)
		{
			cous.onDraw(canvas);
			ps.onDraw(canvas, GameStates.Continous);
		}
		else if (currentState == GameStates.Loading)
			onDrawLoadingScreen(canvas);
		else if (currentState == GameStates.MapMaker)
			MapS.onDraw(canvas);
		
		//canvas.drawText(String.valueOf(fps.getFPS()), 300, 100, textPaint);
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
		canvas.drawText("Loading...", width / 2 - textPaint.measureText("Loading...") / 2, height / 2 + (25.0f / 1.5f * scale) + (24.0f / 1.5f * scale), textPaint);
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
		else if (newState == GameStates.MapMaker)
		{
			purgeManagers();
			MapS = new MapMakerScreen(width, height);
			MapS.onInitialize(getResources(), im, pm, sm, mainFox);
			oldState = GameStates.TitleScreen;
			currentState = GameStates.MapMaker;
			
			mm.addFade(new SoundFade(0, 1, 0, 3000));
		}
	}
	
	private void purgeManagers()
	{
		pm.purge();
		// sm.purge();
	}
	
	public void onUserPause()
	{
		if(this.currentState != GameStates.TitleScreen && this.currentState != GameStates.Loading && this.currentState != GameStates.Pause && this.currentState != GameStates.MapMaker)
		{
			this.oldState = this.currentState;
			this.currentState = GameStates.Pause;
		}
		else if(this.currentState == GameStates.Pause)
		{
			this.currentState = this.oldState;
			this.oldState = GameStates.Pause;
		}
	}
	
	public void onScreenPause(SharedPreferences.Editor ed)
	{
		// when the game is paused by outside shit.
		if(currentState != GameStates.Pause && currentState != GameStates.Loading)
		{
			this.oldState = this.currentState;
			this.currentState = GameStates.Pause;
		}
		
		mm.stop();
		int currentSong = mm.getCurrentSong();
		XMLHandler.writeSerialFile(highScores, "highscores");
		
		ed.putInt("currentSong", currentSong);
		ed.putInt("oldState", this.oldState.ordinal());
		ed.putInt("SPLevel", sp.getCurrentLevel());
		
		this.stopThread();
	}
	
	public void onScreenResume(SharedPreferences ed)
	{
		//set the states first
		//hand loading and title and others
		//play through entire loaded game level 1
		//fix sm
		
		int lastscreen = ed.getInt("oldState", GameStates.TitleScreen.ordinal());

		this.smSetup();
		
		if(lastscreen == GameStates.SinglePlay.ordinal())
		{			
			if(!sp.getInitialized())
			{
				sp.setLevel(ed.getInt("SPLevel", 1));
				sp.onInitialize(getResources(), im, pm, sm, mm, R.raw.level, mainFox);
				
				oldState = GameStates.SinglePlay;
				currentState = GameStates.Loading;
				mm.stop();
			}		
			else
			{
				oldState = GameStates.SinglePlay;
				currentState = GameStates.Pause;
				mm.ChangeSongs(ed.getInt("currentSong",R.raw.pulse), null, new SoundFade(0, 0, 1, 3000));
				mm.play(0);
			}
		}
		else if( lastscreen == GameStates.Continous.ordinal())
		{
			if(!cous.getInitialized())
				cous.onInitialize(getResources(), im, pm, sm, mainFox);
			
			currentState = GameStates.Pause;
			oldState = GameStates.Continous;
			
			mm.ChangeSongs(R.raw.catchinglightning, null, new SoundFade(0, 0, 1, 3000));
			mm.play(0);
		}
		else if( lastscreen == GameStates.MapMaker.ordinal())
		{
			currentState = GameStates.MapMaker;
		}
		else 
		{
			currentState = GameStates.TitleScreen;
			mm.ChangeSongs(ed.getInt("currentSong",R.raw.pulse), null, new SoundFade(0, 0, 1, 3000));
			mm.play(0);
		}	
		
		try
		{
			this.restartThread();
		}
		catch (Exception e)
		{
			Log.e("JAKOBERR", e.toString());
		}
	}
	
	public void onScreenQuit(SharedPreferences.Editor ed)
	{
		//still gotta save stuff
		
		sm.release();
		mm.release();
	}
	
	public void onUserQuit()
	{
		RunfoxrunActivity.ed.clear();
		RunfoxrunActivity.ed.commit();
		
		mm.stop();
		stopThread();
		XMLHandler.writeSerialFile(highScores, "highscores");
		System.exit(0);//does NOT call onScreenQuit
	}
	
}
