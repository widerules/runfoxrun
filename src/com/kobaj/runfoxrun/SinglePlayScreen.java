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
	
	private ArrayList<Sprite> hitList;
	private ArrayList<Sprite> collectionList;
	
	private ArrayList<Level> levelList;
	
	private InputManager im;
	private PhysicsManager pm;
	private SoundManager sm;
	private MusicManager mm;
	private Resources resources;
	
	private int collectionScore = 0;
	private custInt collectionText;
	
	private Sprite player;
	private Sprite badGuy;
	private Sprite back;
	private Sprite back2;
	
	private boolean initialized = false;
	
	private int levelNumber = 4;
	
	// top level
	private Bitmap progressBarIcon;
	private Sprite collectionScoreIcon;
	private Paint linePaint;
	private Paint bitmapPaint;
	
	private boolean sceneDead = false;
	
	private Creditables myCredits;
	
	int pad;
	
	public boolean resetBad = false;
	
	private float credits = 0;
	
	private float scale;
	
	private Sprite temp;
	
	public int getCurrentLevel()
	{
		return levelNumber;
	}
	
	public void setLevel(int level)
	{
		if(level < levelNumber)
			return;
		
		if(level != 1)
		{
			this.levelNumber = level;
		}
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
		player.setyPos(levelList.get(levelNumber - 1).getPlayerStartY());
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
	
	public void onInitialize(Resources resources, InputManager im, PhysicsManager pm, SoundManager sm, MusicManager mm, int level, Sprite player)
	{
		this.scale = resources.getDisplayMetrics().density;
		
		this.im = im;
		this.pm = pm;
		this.sm = sm;
		this.mm = mm;
		
		this.player = player;
		pm.setPlayer(player);
		pm.setScrollRate(SurfacePanel.scrollRate);
		player.setPaintColorFilter(255);
		
		this.resources = resources;
		
		myCredits = new Creditables(resources, width, height);
		
		start();
	}
	
	public boolean onUpdate(float delta)
	{
		if (initialized)
		{
			
			// badguy movement
			if (resetBad)
				if (badGuy.getxPos() + badGuy.getWidth() > 0)
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
			
			// handle next level;
			// probably could have done this a bit better.
			if (pm.getScrollProgress() >= (levelList.get(levelNumber - 1).getLevelLength()) / 1.5f * SurfacePanel.scale)
			{
				hitList.clear();
				pm.nextLevel();
				
				levelNumber++;
				
				HighScores.setLevel(levelNumber);
				
				pm.setBackDiv(((((float)levelList.get(levelNumber - 1).getLevelLength()) / (1600.0f))));
				//back.setxPos(-1600 * (levelNumber - 1));
				
				if (levelNumber == 2)
				{
					back.onCleanup();
					back.onInitialize(LoadedResources.getBackgroundTHREE(resources), (int)((back2.getxPos() + back2.getWidth())), (int) (height - (480.0f / 1.5f * scale)), (int)(1600.0f / 1.5f * scale), (int)(480.0f / 1.5f * scale));
					mm.ChangeSongs(R.raw.quicken, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
				}
				else if (levelNumber == 3)
				{
					back2.onCleanup();
					back2.onInitialize(LoadedResources.getBackgroundFOUR(resources), (int)((back.getxPos() + back.getWidth())), (int) (height - (480.0f / 1.5f * scale)), (int)(1600.0f / 1.5f * scale), (int)(480.0f / 1.5f * scale));
					mm.ChangeSongs(R.raw.aegissprint, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
				}
				else if (levelNumber == 4)
				{
					back.onCleanup();
					back.onInitialize(LoadedResources.getBackgroundFIVE(resources), (int)((back2.getxPos() + back2.getWidth())), (int) (height - (480.0f / 1.5f * scale)), (int)(1600.0f / 1.5f * scale), (int)(480.0f / 1.5f * scale));
					mm.ChangeSongs(R.raw.blackdiamond, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
					pm.setScrollRate((float) (pm.getScrollRate() - (.025f / 1.5f * scale)));
					
					//pm.setBackDiv(((float)levelList.get(levelNumber - 1).getLevelLength()) / (1600.0f - (float)(width)));
				}
				
				pm.setScrollProgress(0, false);
				
				grabHitList(levelNumber);
			}
			else if (levelNumber == 3 && pm.getScrollProgress() >= 16000.0f / 1.5f * scale - width - 200.0f / 1.5f * scale && pm.getScrollProgress() < 16000.0f / 1.5f * scale - width - 200.0f / 1.5f * scale + pm.getScrollDelta() * delta)
				sceneDead = true;
			else if (levelNumber == 3 && pm.getScrollProgress() >= 16000.0f / 1.5f * scale - width + 100.0f / 1.5f * scale && pm.getScrollProgress() < 16000.0f / 1.5f * scale - width + 100.0f / 1.5f * scale + pm.getScrollDelta() * delta)
			{
				if (player.getCurAnimation() == (CharStates.Running.ordinal()))
				{
					pm.unsetPlayer();
					pm.addPhys(player);
					player.setAnimation(CharStates.Collapse);
				}
			}
			else if (levelNumber == 4 && sceneDead && pm.getScrollProgress() >= 200.0f / 1.5f * scale)
			{
				sceneDead = false;
				pm.setPlayer(player);
				player.setAnimation(CharStates.Running);
				this.setPlayerPos();
				badGuy.setyPos(-height - badGuy.getHeight() - 10);
			}
			else if (levelNumber == 4 && pm.getScrollProgress() >= 16000.0f / 1.5f * scale - width - 250.0f / 1.5f * scale && pm.getScrollProgress() < 16000.0f / 1.5f * scale - width - 200.0f / 1.5f * scale + pm.getScrollDelta() * delta && credits == 0)
			{
				credits += delta;
			}
			else if (levelNumber == 4 && pm.getScrollProgress() >= 16000.0f / 1.5f * scale - width - 25.0f / 1.5f * scale)
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
				if (levelNumber != 4)
					sm.playSound(3, .25f);
				else
					sm.playSound(3, .10f);
				
				System.gc();
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
					collectionScore++;
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
		if (initialized)
		{
			// background
			if(back != null)
				back.onDraw(canvas);
			if(back2 != null)
				back2.onDraw(canvas);
			
			// interaction layer
			for (Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
			{
				temp = it.next();
				
				int spritePosx = (int) temp.getxPos();
				int spriteWidth = (int) temp.getWidth();
				if (spritePosx < width && spritePosx + spriteWidth > 0)
				{
					temp.onDraw(canvas);
				}
			}
			
			// character
			player.onDraw(canvas);
			
			// bad guy
			if (badGuy.getyPos() > 0)
				badGuy.onDraw(canvas);
			
			// overlay (I should really not be doing math/logic here >.<
			canvas.drawLine(pad, 20.0f / 1.5f * scale, width - pad, 20.0f / 1.5f * scale, linePaint);
			canvas.drawLine(pad, 15.0f / 1.5f * scale, pad, 27.0f / 1.5f * scale, linePaint);
			canvas.drawLine(width - pad, 15.0f / 1.5f * scale, width - pad, 27.0f / 1.5f * scale, linePaint);
			canvas.drawBitmap(progressBarIcon, linInterp(width / 1.5f * scale, 
					levelList.get(levelNumber - 1).getLevelLength() / 1.5f * scale, 
					pm.getScrollProgress(), 
					pad, 
					width - pad), 0, bitmapPaint);
			
			// more overlay
			collectionScoreIcon.onDraw(canvas);
			collectionText.onDraw(canvas);
			
			if (credits > 15000)
				myCredits.onDraw(canvas);
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
	}
	
	@Override
	public void run()
	{
		back = new Sprite();
		back2 = new Sprite();
		if(levelNumber == 1)
		{
			back.onInitialize(LoadedResources.getBackgroundONE(resources), 0, height - LoadedResources.getBackgroundONE(resources).getHeight(), LoadedResources.getBackgroundONE(resources).getWidth(), LoadedResources.getBackgroundONE(resources).getHeight());
			back2.onInitialize(LoadedResources.getBackgroundTWO(resources), back.getWidth(), height - LoadedResources.getBackgroundTWO(resources).getHeight(), LoadedResources.getBackgroundTWO(resources).getWidth(), LoadedResources.getBackgroundTWO(resources).getHeight());
		}
		if(levelNumber == 2)
		{
			back2.onInitialize(LoadedResources.getBackgroundTWO(resources), 0, height - LoadedResources.getBackgroundTWO(resources).getHeight(), LoadedResources.getBackgroundTWO(resources).getWidth(), LoadedResources.getBackgroundTWO(resources).getHeight());
			back.onInitialize(LoadedResources.getBackgroundTHREE(resources), back2.getWidth(), height - LoadedResources.getBackgroundTHREE(resources).getHeight(), LoadedResources.getBackgroundTHREE(resources).getWidth(), LoadedResources.getBackgroundTHREE(resources).getHeight());
		}
		if(levelNumber == 3)
		{
			back.onInitialize(LoadedResources.getBackgroundTHREE(resources), 0, height - LoadedResources.getBackgroundTHREE(resources).getHeight(), LoadedResources.getBackgroundTHREE(resources).getWidth(), LoadedResources.getBackgroundTHREE(resources).getHeight());
			back2.onInitialize(LoadedResources.getBackgroundFOUR(resources), back.getWidth(), height - LoadedResources.getBackgroundFOUR(resources).getHeight(), LoadedResources.getBackgroundFOUR(resources).getWidth(), LoadedResources.getBackgroundFOUR(resources).getHeight());
		}
		if(levelNumber == 4)
		{
			back2.onInitialize(LoadedResources.getBackgroundFOUR(resources), 0, height - LoadedResources.getBackgroundFOUR(resources).getHeight(), LoadedResources.getBackgroundFOUR(resources).getWidth(), LoadedResources.getBackgroundFOUR(resources).getHeight());
			back.onInitialize(LoadedResources.getBackgroundFIVE(resources), back2.getWidth(), height - LoadedResources.getBackgroundFIVE(resources).getHeight(), LoadedResources.getBackgroundFIVE(resources).getWidth(), LoadedResources.getBackgroundFIVE(resources).getHeight());
		}
		
		progressBarIcon = LoadedResources.getIcon(resources);
		// load dat bad guy
		this.badGuy = XMLHandler.readSerialFile(resources, R.raw.smoke, Sprite.class);
		badGuy.onInitialize(LoadedResources.getBadGuy(resources), (int)(-165.0f / 1.5f * scale), (int)(height - (470.0f / 1.5f * scale)), (int)(164.0f / 1.5f * scale), (int)(470.0f / 1.5f * scale));
		
		collectionScore = 0;
		
		linePaint = new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setStrokeWidth(1);
		linePaint.setShadowLayer(1, 0, 0, Color.BLACK);
		
		bitmapPaint = new Paint();
		
		collectionText = new custInt(resources, 0, 2, width - (int) (37 * scale), (int) (16 * scale));
		collectionScoreIcon = XMLHandler.readSerialFile(resources, R.raw.star, Sprite.class);
		collectionScoreIcon.onInitialize(LoadedResources.getStar(resources), width - (int) (57 * scale), (int) (3 * scale), (int)(25.0f / 1.5f * scale), (int)(24.0f / 1.5f * scale));
		
		// load in the level
		levelList.add(XMLHandler.readSerialFile(resources, R.raw.level, Level.class));
		levelList.add(XMLHandler.readSerialFile(resources, R.raw.level2, Level.class));
		levelList.add(XMLHandler.readSerialFile(resources, R.raw.level3, Level.class));
		levelList.add(XMLHandler.readSerialFile(resources, R.raw.level4, Level.class));
		
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
		if(levelNumber == 1)
			mm.ChangeSongs(R.raw.pulse);
		if(levelNumber == 2)
			mm.ChangeSongs(R.raw.quicken);
		if(levelNumber == 3)
			mm.ChangeSongs(R.raw.aegissprint);
		if(levelNumber == 4)
			mm.ChangeSongs(R.raw.blackdiamond);
		mm.addFade(new SoundFade(0, 0, 1, 3000));
		mm.play(0);
		
		pm.setBackDiv(((float)levelList.get(levelNumber - 1).getLevelLength()) / (1600.0f));
		
		//and a very specialized
		if(levelNumber == 4)
			badGuy.setyPos(-height - badGuy.getHeight() - 10);
		
		pm.setScrollProgress(14500 / 1.5f * SurfacePanel.scale, true);
		
		initialized = true;
	}
	
	private void grabHitList(int levelNumber)
	{
		hitList.clear();
		for (Iterator<Sprite> it = levelList.get(levelNumber - 1).getlevelSpriteList().iterator(); it.hasNext();)
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
