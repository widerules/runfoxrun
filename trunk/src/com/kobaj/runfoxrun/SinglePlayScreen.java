package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SinglePlayScreen implements Runnable
{
	// thread stuff
	private Thread thread;
	
	private int width;
	private int height;
	
	private int accessNumber = 0;
	private boolean drawloading = true;
	
	private ArrayList<Sprite> hitList;
	private ArrayList<Sprite> collectionList;
	
	private ArrayList<Level> levelList;
	
	private InputManager im;
	private PhysicsManager pm;
	private SoundManager sm;
	private MusicManager mm;
	private Resources resources;
	
	private int collectionScore = 3;
	private custInt collectionText;
	
	private Sprite player;
	private Sprite badGuy;
	private Sprite back;
	private Sprite back2;
	
	private Sprite loadingStar;
	
	private boolean initialized = false;
	
	private int levelNumber = 1;
	
	// top level
	private Bitmap progressBarIcon;
	private Sprite collectionScoreIcon;
	private Paint linePaint;
	private Paint bitmapPaint;
	private Paint blackPaint;

	private boolean sceneDead = false;
	
	private Creditables myCredits;
	
	private int pad;
	
	public boolean resetBad = false;
	
	private int black_to_level = 0;
	private int level_to_black = 0;
	
	private float credits = 0;
	private float gameOver = 0;
	private custString gameOverString;
	
	private custnewlineString stringfirst;
	private custnewlineString stringsecond;
	private custnewlineString stringthird;
	private custnewlineString stringfourth;
	private custString loading;
	private custString done;
	private custString clicktocontinue;
	
	private HighScores highscores;
	
	private float scale;
	
	private Sprite temp;
	
	public int getCurrentLevel()
	{
		return levelNumber;
	}
	
	public void setLevel(int level)
	{
		if(highscores == null)
		{
			levelNumber = level;
			return;
		}
		
		if(level <= highscores.getLevel())
			levelNumber = level;
	}
	
	public void force_load()
	{
		drawloading = true;
		start();	
	}
	
	private int getAccess(int levelnum)
	{
		if(levelnum % 2 == 0)
			return 1;
		
		return 0;
	}
	
	public void setMMandSM(SoundManager SM, MusicManager MM)
	{
		this.sm = SM;
		this.mm = MM;
		
		for(Sprite it: collectionList)
		{
			it.setMMandSM(sm);
		}
	}
	
	private void setPlayerPos()
	{
		player.setxPos((int) (width / 3.0f));
		//TODO set this one
		player.setyPos(SurfacePanel.startHeight);
	}
	
	public SinglePlayScreen(int width, int height)
	{
		this.width = width;
		this.height = height;
		pad = (int) (width / 4.0f);
		
		levelList = new ArrayList<Level>();
	}
	
	public boolean getInitialized()
	{
		return initialized;
	}
	
	public void onInitialize(Resources resources, InputManager im, PhysicsManager pm, SoundManager sm, MusicManager mm, int level, Sprite player, HighScores highScores)
	{
		this.scale = SurfacePanel.scale;
		
		this.highscores = highScores;
		
		this.im = im;
		this.pm = pm;
		this.sm = sm;
		this.mm = mm;
		
		black_to_level = 0;
		level_to_black = 0;
		
		credits = 0;
		gameOver = 0;
		
		this.player = player;
		pm.setPlayer(player);
		pm.setScrollRate(SurfacePanel.scrollRate);
		player.setPaintColorFilter(255);
		
		this.resources = resources;
		
		myCredits = new Creditables(resources, width, height);
	
		gameOverString = new custString(resources, "Game Over", 0, 0);
		gameOverString.setPosition((int)(width / 2.0f - gameOverString.measureit() / 2.0f), (int)(height / 2.0f));
		
		loadingStar = XMLHandler.readSerialFile(resources, R.raw.star, Sprite.class);
		loadingStar.onInitialize(LoadedResources.getStar(resources), (int)(450 / 1.5f * scale), (int)(400 / 1.5f * scale), (int)(25.0f / 1.5f * scale), (int)(24.0f  / 1.5f * scale));
		
		
		/*
		 * 
		 * [loading city/intro loading]

Man forgot this city long ago. The only soul keeping the decrepit buildings and lonely streets 

company is a creature surrounded by thick black smoke. And once awakened, the creature will 

not stop in chasing its prey.

[loading forrest]

The city is no place for a fox, but a dense forest filled with deadly vines is no better. Can't turning 

back now, the creature chases the fox into the thicket of branches.

[loading dessert]

The forest stands behind the fox, but an aired desert looms ahead. The sand is difficult to run 

on, but there is no choice for the fox. The creature seeks its prey.

[loading blackness]

What's ahead now? Remnants of buildings, forest, and desert can be seen, but where will they 

lead?
*/
		
		stringfirst = new custnewlineString(resources, "Man forgot this city long ago. The only soul keeping \nthe decrepit buildings and lonely streets company is \na creature surrounded by thick black smoke. And once \nawakened, the creature will not stop in chasing its prey.", (int)(100.0f * 1.5f / scale), (int)(100.0f * 1.5f / scale));
		stringfirst.setSize((int)(20.0f * 1.5f / scale));
		
		stringsecond = new custnewlineString(resources, "The city is no place for a fox, but a dense forest \nfilled with deadly vines is no better. Can't turn \nback now, the creature chases the fox into the \nthicket of branches.", (int)(100.0f * 1.5f / scale), (int)(100.0f * 1.5f / scale));
		stringsecond.setSize((int)(20.0f * 1.5f / scale));
		
		stringthird = new custnewlineString(resources, "The forest stands behind the fox, but an aired desert \nlooms ahead. The fox has no choice but to continue \nforward, the creature seeks its prey. The fox runs on.", (int)(100.0f * 1.5f / scale), (int)(100.0f * 1.5f / scale));
		stringthird.setSize((int)(20.0f * 1.5f / scale));
		
		stringfourth = new custnewlineString(resources, "Black. \nConsumed in an instant by the creature. \nWhat's ahead now? Remnants of buildings, forest, and \ndesert can be seen, but where will they lead? \nIs there hope?", (int)(100.0f * 1.5f / scale), (int)(100.0f * 1.5f / scale));
		stringfourth.setSize((int)(20.0f * 1.5f / scale));
		
		loading = new custString(resources, "Loading...", (int)(300.0f * 1.5f / scale), (int)(400.0f * 1.5f / scale));
		loading.setSize((int)(20.0f * 1.5f / scale));
		done = new custString(resources, "Done.", (int)(385.0f * 1.5f / scale), (int)(400.0f * 1.5f / scale));
		done.setSize((int)(20.0f * 1.5f / scale));
		clicktocontinue = new custString(resources, "Ready to dash?" , (int)(300.0f * 1.5f / scale), (int)(430.0f * 1.5f / scale));
		clicktocontinue.setColor(Color.RED, Color.BLACK);
		clicktocontinue.setSize((int)(20.0f * 1.5f / scale));
		
		start();
	}
	
	public boolean onUpdate(float delta)
	{
		/*
		 * Todo
		 * -allow user to select level
		 * -don't forget to give the users a new res
		 */
		
		if(drawloading)
			loadingStar.onUpdate(delta);
		
		if(waiting_to_click && drawloading)
		{	
			for (int i = 0; i < im.fingerCount; i++)
			{
				if (im.getPressed(i))
				{
					waiting_to_click = false;
					drawloading = false;
					black_to_level = 1;
					System.gc();
					break;
				}
			}
		}
		
		if (initialized && !drawloading)
		{	
			//always at the top!
			player.onUpdate(delta);
			pm.onUpdate(delta);
			
			// badguy movement
			if (resetBad)
				if (badGuy.getxPos() + badGuy.getWidth() > 0 - 20)
					badGuy.setxPos((badGuy.getxPos() - delta / 5.0f));
				else
					resetBad = false;
			
			if (pm.getScrollProgress() >= (800.0f / 1.5f * scale))
				if (badGuy.getxPos() < 0 && !resetBad)
				{
					badGuy.setxPos(badGuy.getxPos() + delta / 5f);
					
					if (badGuy.getxPos() > 0)
					{
						badGuy.setxPos(0); // done resettings
					}
				}
			
			// handle input
			for (int i = 0; i < im.fingerCount; i++)
			{
				if (im.getPressed(i))
				{
					if (!sceneDead && this.credits == 0)
						pm.jump();
				}
			}
			
			//mmm dat fade to black
			if (pm.getScrollProgress() >= (levelList.get(accessNumber).getLevelLength() - 300 / 1.5f * SurfacePanel.scale) / 1.5f * SurfacePanel.scale)
			if(levelNumber == 2 || levelNumber== 4 || levelNumber == 6)
			if(level_to_black == 0)
			{
				level_to_black = 1;
			}
			
			// handle next level;
			// probably could have done this a bit better.
			if (pm.getScrollProgress() >= (levelList.get(accessNumber).getLevelLength()) / 1.5f * SurfacePanel.scale)
			{
				hitList.clear();
				pm.nextLevel();
				
				levelNumber++;
				
				accessNumber = getAccess(levelNumber);
				
				highscores.setLevel(levelNumber);

				if (levelNumber == 3)
				{
					drawloading = true;
					//back.onCleanup();
					//back.onInitialize(LoadedResources.getBackgroundTHREE(resources), (int)((back2.getxPos() + back2.getWidth())), (int) (height - (480.0f / 1.5f * scale)), (int)(1600.0f / 1.5f * scale), (int)(480.0f / 1.5f * scale));
					//mm.ChangeSongs(R.raw.quicken, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
					start();
					return true;
				}
				else if (levelNumber == 5)
				{
					//back2.onCleanup();
					//back2.onInitialize(LoadedResources.getBackgroundFOUR(resources), (int)((back.getxPos() + back.getWidth())), (int) (height - (480.0f / 1.5f * scale)), (int)(1600.0f / 1.5f * scale), (int)(480.0f / 1.5f * scale));
					//mm.ChangeSongs(R.raw.aegissprint, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
					drawloading = true;
					start();
					return true;
				}
				else if (levelNumber == 7)
				{
					//back.onCleanup();
					//back.onInitialize(LoadedResources.getBackgroundFIVE(resources), (int)((back2.getxPos() + back2.getWidth())), (int) (height - (480.0f / 1.5f * scale)), (int)(1600.0f / 1.5f * scale), (int)(480.0f / 1.5f * scale));
					//mm.ChangeSongs(R.raw.blackdiamond, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
					pm.setScrollRate((float) (pm.getScrollRate() - (.025f / 1.5f * scale)));
					drawloading = true;
					start();
					return true;
					
					//pm.setBackDiv(((float)levelList.get(levelNumber - 1).getLevelLength()) / (1600.0f - (float)(width)));
				}
				
				pm.setScrollProgress(0, false);
				
				grabHitList(levelNumber);
			}
			else if (levelNumber == 6 && pm.getScrollProgress() >= 16000.0f / 1.5f * scale - width - 200.0f / 1.5f * scale && pm.getScrollProgress() < 16000.0f / 1.5f * scale - width - 200.0f / 1.5f * scale + pm.getScrollDelta() * delta)
				sceneDead = true;
			else if (levelNumber == 6 && pm.getScrollProgress() >= 16000.0f / 1.5f * scale - width + 100.0f / 1.5f * scale && pm.getScrollProgress() < 16000.0f / 1.5f * scale - width + 100.0f / 1.5f * scale + pm.getScrollDelta() * delta)
			{
				if (player.getCurAnimation() == (CharStates.Running.ordinal()))
				{
					pm.unsetPlayer();
					pm.addPhys(player);
					player.setAnimation(CharStates.Collapse);
				}
			}
			else if (levelNumber == 7 && sceneDead && pm.getScrollProgress() >= 200.0f / 1.5f * scale)
			{
				sceneDead = false;
				pm.setPlayer(player);
				player.setAnimation(CharStates.Running);
				this.setPlayerPos();
				badGuy.setyPos(-height - badGuy.getHeight() - 10);
			}
			else if (levelNumber == 7 && pm.getScrollProgress() >= 16000.0f / 1.5f * scale - width - 250.0f / 1.5f * scale && pm.getScrollProgress() < 16000.0f / 1.5f * scale - width - 200.0f / 1.5f * scale + pm.getScrollDelta() * delta && credits == 0)
			{
				credits += delta;
			}
			else if (levelNumber == 7 && pm.getScrollProgress() >= 16000.0f / 1.5f * scale - width - 25.0f / 1.5f * scale)
			{
				if (player.getCurAnimation() == (CharStates.Running.ordinal()))
				{
					pm.setScrollRate(0);
					player.setAnimation(CharStates.Standing);
					pm.unsetPlayer();
					
					pm.purge(); // may need to be removed
				}
			}
			
			if (credits > 0)
				credits += delta;
			
			if(gameOver > 0)
			{
				int multiplier = (int) this.linInterp(0, 1000, gameOver, 0, 255);
				blackPaint.setAlpha(multiplier);
				gameOver += delta;
				if(player.getCurAnimation() == (CharStates.Running.ordinal()))
				{
					pm.unsetPlayer();
					pm.levelReset();
				}
			}
			
			if(level_to_black > 0)
			{
				int multiplier = (int) this.linInterp(0, 1000, level_to_black, 0, 255);
				blackPaint.setAlpha(multiplier);
				level_to_black += delta;
			}
			else if(black_to_level > 0)
			{
				int multiplier = (int) this.linInterp(0, 1000, black_to_level, 255, 0);
				blackPaint.setAlpha(multiplier);
				black_to_level += delta;
			}
			
			if(level_to_black > 5150)
				level_to_black = -1;
			if(black_to_level > 1150)
				black_to_level = -1;
			
			if(gameOver > 10000)
			{
				return false;
			}
			
			// credits;
			if (credits > 10000 && credits < 15000)
			{
				int multiplier = (int) this.linInterp(10000, 15000, credits, 255, 0);
				for (Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
				{
					it.next().setPaintColorFilter(multiplier);
				}
				
				player.setPaintColorFilter(multiplier);
				badGuy.setPaintColorFilter(multiplier);
				linePaint.setShadowLayer(0, 0, 0, Color.BLACK);
				linePaint.setAlpha(multiplier);
				bitmapPaint.setAlpha(multiplier);
			}
			if (credits > 15000)
			{
				hitList.clear();
				collectionList.clear();
				if (!myCredits.getDone())
				{
					myCredits.onUpdate(delta);
				}
				else
				{
					mm.ChangeSongs(R.raw.pulse, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
					return false;
				}
				
				player.setyPos(-height - player.getHeight() - 25);
				player.setPaintColorFilter(0);
				badGuy.setPaintColorFilter(0);
				linePaint.setAlpha(0);
				bitmapPaint.setAlpha(0);
			}
			
			// bad guy
			badGuy.onUpdate(delta);
			
			// handle death;
			if (pm.getDeath())
			{
				pm.levelReset();
				resetBad = true;
				if (levelNumber != 4 + 1)
					sm.playSound(3, .25f);
				else
					sm.playSound(3, .10f);
				
				System.gc();
				
				collectionScore -= 1;
				
				if(collectionScore < 0)
					gameOver++;
			}
			
			// set me collections
			collectionText.setInt(collectionScore);
			collectionScoreIcon.onUpdate(delta);
			
			// gotta loop through and find the collected elements
			for (Iterator<Sprite> it = collectionList.iterator(); it.hasNext();)
			{
				temp = it.next();
				temp.onUpdate(delta);
				if (temp.getCollectable() == CollectableStates.collected)
				{
					collectionScore += 1;
					it.remove();
					hitList.remove(temp);
					pm.removePhys(temp);
				}
			}
		}
		
		return true;
	}
	
	public void onDraw(Canvas canvas)
	{
		
			if(!drawloading)
			{
				if (initialized)
				{
			// background
			if(back != null)
				back.onDraw(canvas);
			if(back2 != null)
				back2.onDraw(canvas);
			
			// interaction layer
			//integer iterators are faster?
			for (Sprite it: hitList)
			{
				int spritePosx = (int) it.getxPos();
				int spriteWidth = (int)it.getWidth();
				if (spritePosx < width && spritePosx + spriteWidth > 0)
				{
					it.onDraw(canvas);
				}
			}
			
			// bad guy
			if (badGuy.getyPos() > 0)
				badGuy.onDraw(canvas);
			
			if(gameOver > 0)
				canvas.drawRect(0, 0, width, height, blackPaint);
			
			// character
			player.onDraw(canvas);
			
			//black box
			if(height - LoadedResources.getBackground1(resources).getHeight() > 0)
				canvas.drawRect(0, 0, width, height - LoadedResources.getBackground1(resources).getHeight(), bitmapPaint);
			
			// overlay (I should really not be doing math/logic here >.<
			canvas.drawLine(pad, 20.0f / 1.5f * scale, width - pad, 20.0f / 1.5f * scale, linePaint);
			canvas.drawLine(pad, 15.0f / 1.5f * scale, pad, 27.0f / 1.5f * scale, linePaint);
			canvas.drawLine(width - pad, 15.0f / 1.5f * scale, width - pad, 27.0f / 1.5f * scale, linePaint);
			canvas.drawBitmap(progressBarIcon, linInterp(width / 1.5f * scale, 
					levelList.get(accessNumber).getLevelLength() / 1.5f * scale, 
					pm.getScrollProgress(), 
					pad, 
					width - pad - progressBarIcon.getWidth()), 0, bitmapPaint);
			
			// more overlay
			collectionScoreIcon.onDraw(canvas);
			collectionText.onDraw(canvas);
			
			if (credits > 15000)
				myCredits.onDraw(canvas);
			
			if(gameOver > 0)
				gameOverString.onDraw(canvas);
			
			if(level_to_black > 0 || black_to_level > 0)
				canvas.drawRect(0, 0, width, height, blackPaint);
			
				}
			}
			else
			{
				//fancy new loading screens
				
				loadingStar.onDraw(canvas);
				
				if(waiting_to_click)
				{
					done.onDraw(canvas);
					clicktocontinue.onDraw(canvas);
				}
				
				loading.onDraw(canvas);
				
				if(levelNumber < 3)
					stringfirst.onDraw(canvas);
				else if(levelNumber < 5)
					stringsecond.onDraw(canvas);
				else if(levelNumber < 7)
					stringthird.onDraw(canvas);
				else if(levelNumber == 7)
					stringfourth.onDraw(canvas);
			}
	}
	
	// really should make my own Math class...
	private float linInterp(float minX, float maxX, float value, float minY, float maxY)
	{
		if (minX == maxX)
			return minY;
		
		if(value < minX)
			return minY;
		
		if(value > maxX)
			return maxY;
		
		return minY * (value - maxX) / (minX - maxX) + maxY * (value - minX) / (maxX - minX);
	}
	
	private void start()
	{
		thread = new Thread(this);
		thread.start();
	}
	
	public void Release()
	{
		back.Release();
		back2.Release();
		loadingStar.Release();
	}
	
	@Override
	public void run()
	{
		//assuming we are rebootins
		levelList.clear();
		pm.reset();
		pm.purge();
		
		accessNumber = getAccess(levelNumber);
		
		back = new Sprite();
		back2 = new Sprite();
		if(levelNumber == 1 || levelNumber == 2)
		{
			back.onInitialize(LoadedResources.getBackgroundONE(resources), 0, height - LoadedResources.getBackgroundONE(resources).getHeight(), LoadedResources.getBackgroundONE(resources).getWidth(), LoadedResources.getBackgroundONE(resources).getHeight());
			back2.onInitialize(LoadedResources.getBackgroundTWO(resources), back.getWidth(), height - LoadedResources.getBackgroundTWO(resources).getHeight(), LoadedResources.getBackgroundTWO(resources).getWidth(), LoadedResources.getBackgroundTWO(resources).getHeight());
		}
		if(levelNumber == 3 || levelNumber == 4)
		{
			back2.onInitialize(LoadedResources.getBackgroundTWO(resources), 0, height - LoadedResources.getBackgroundTWO(resources).getHeight(), LoadedResources.getBackgroundTWO(resources).getWidth(), LoadedResources.getBackgroundTWO(resources).getHeight());
			back.onInitialize(LoadedResources.getBackgroundTHREE(resources), back2.getWidth(), height - LoadedResources.getBackgroundTHREE(resources).getHeight(), LoadedResources.getBackgroundTHREE(resources).getWidth(), LoadedResources.getBackgroundTHREE(resources).getHeight());
		}
		if(levelNumber == 5 || levelNumber == 6)
		{
			back.onInitialize(LoadedResources.getBackgroundTHREE(resources), 0, height - LoadedResources.getBackgroundTHREE(resources).getHeight(), LoadedResources.getBackgroundTHREE(resources).getWidth(), LoadedResources.getBackgroundTHREE(resources).getHeight());
			back2.onInitialize(LoadedResources.getBackgroundFOUR(resources), back.getWidth(), height - LoadedResources.getBackgroundFOUR(resources).getHeight(), LoadedResources.getBackgroundFOUR(resources).getWidth(), LoadedResources.getBackgroundFOUR(resources).getHeight());
		}
		if(levelNumber == 7)
		{
			back2.onInitialize(LoadedResources.getBackgroundFOUR(resources), 0, height - LoadedResources.getBackgroundFOUR(resources).getHeight(), LoadedResources.getBackgroundFOUR(resources).getWidth(), LoadedResources.getBackgroundFOUR(resources).getHeight());
			back.onInitialize(LoadedResources.getBackgroundFIVE(resources), back2.getWidth(), height - LoadedResources.getBackgroundFIVE(resources).getHeight(), LoadedResources.getBackgroundFIVE(resources).getWidth(), LoadedResources.getBackgroundFIVE(resources).getHeight());
		}
		
		progressBarIcon = LoadedResources.getIcon(resources);
		// load dat bad guy
		this.badGuy = XMLHandler.readSerialFile(resources, R.raw.smoke, Sprite.class);
		badGuy.onInitialize(LoadedResources.getBadGuy(resources), (int)(-165.0f / 1.5f * scale), (int)(height - (470.0f / 1.5f * scale)), (int)(164.0f / 1.5f * scale), (int)(470.0f / 1.5f * scale));
		
		linePaint = new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setStrokeWidth(1);
		linePaint.setShadowLayer(1, 0, 0, Color.BLACK);
		
		bitmapPaint = new Paint();
		
		blackPaint = new Paint();
		blackPaint.setARGB(0, 0, 0, 0);
		
		collectionText = new custInt(resources, 0, 2, width - (int) (37 * scale), (int) (16 * scale));
		collectionScoreIcon = XMLHandler.readSerialFile(resources, R.raw.star, Sprite.class);
		collectionScoreIcon.onInitialize(LoadedResources.getStar(resources), width - (int) (57 * scale), (int) (3 * scale), (int)(25.0f / 1.5f * scale), (int)(24.0f / 1.5f * scale));
		
		// load in the level
		if(levelNumber == 1 || levelNumber == 2)
		{
			levelList.add(XMLHandler.readSerialFile(resources, R.raw.level, Level.class));
			levelList.add(XMLHandler.readSerialFile(resources, R.raw.level_new_2_map_, Level.class));
		}
		if(levelNumber == 3 || levelNumber == 4)
		{
			levelList.add(XMLHandler.readSerialFile(resources, R.raw.level2, Level.class));
			levelList.add(XMLHandler.readSerialFile(resources, R.raw.level_new_3_star_map_, Level.class));
		}
		if(levelNumber == 5 || levelNumber == 6)
		{
			levelList.add(XMLHandler.readSerialFile(resources, R.raw.level_new_4_66_star_map_, Level.class));
			levelList.add(XMLHandler.readSerialFile(resources, R.raw.level3, Level.class));
		}
		if(levelNumber == 7)
		{
			levelList.add(XMLHandler.readSerialFile(resources, R.raw.level4, Level.class));
		}
		
		for (Iterator<Level> it = levelList.iterator(); it.hasNext();)
			it.next().onInitialize(resources, width, height, sm);
		
		// grab the hit list;
		hitList = new ArrayList<Sprite>();
		grabHitList(levelNumber);
		
		// grab collectionlist
		collectionList = new ArrayList<Sprite>();
		for (Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
		{
			Sprite temp = it.next();
			
			if (temp.getCollectable() == CollectableStates.collectable)
				collectionList.add(temp);
		}
		
		setPlayerPos();
		
		pm.addBackgroundPhys(back);
		pm.addBackgroundPhys(back2);
		
		//should not be doing this
		if(levelNumber == 1 || levelNumber == 2)
			mm.ChangeSongs(R.raw.pulse);
		if(levelNumber == 3 || levelNumber == 4)
			mm.ChangeSongs(R.raw.quicken);
		if(levelNumber == 5 || levelNumber == 6)
			mm.ChangeSongs(R.raw.aegissprint);
		if(levelNumber == 7)
			mm.ChangeSongs(R.raw.blackdiamond);
		mm.addFade(new SoundFade(0, 0, 1, 3000));
		mm.play(0);
		
		if (levelNumber != 7)
			pm.setBackDiv(((((float)levelList.get(accessNumber).getLevelLength() * 2) / (1600.0f))));
		else
			pm.setBackDiv(((((float)levelList.get(accessNumber).getLevelLength()) / (1600.0f))));
		
		pm.setScrollRate(SurfacePanel.scrollRate);
		
		//and a very specialized
		if(levelNumber == 7)
		{
			pm.setScrollRate((float) (pm.getScrollRate() - (.025f / 1.5f * scale)));
			badGuy.setyPos(-height - badGuy.getHeight() - 10);
		}
		
		XMLHandler.writeSerialFile(highscores, "highscores");
		
		//pm.setScrollProgress(14500 / 1.5f * SurfacePanel.scale, true);
		
		System.gc();
		
		black_to_level = 0;
		level_to_black = 0;
		
		credits = 0;
		gameOver = 0;
		
		initialized = true;
		waiting_to_click = true;
	}
	
	boolean waiting_to_click = false;
	
	private void grabHitList(int levelNumber)
	{
		hitList.clear();
		for (Iterator<Sprite> it = levelList.get(accessNumber).getlevelSpriteList().iterator(); it.hasNext();)
		{
			Sprite temp = it.next();
			hitList.add(temp);
			pm.addPhys(temp);
		}
		
		// grab collectionlist
		collectionList = new ArrayList<Sprite>();
		collectionList.clear();
		for (Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
		{
			Sprite temp = it.next();
			
			if (temp.getCollectable() == CollectableStates.collectable)
				collectionList.add(temp);
		}
		
	}
}
