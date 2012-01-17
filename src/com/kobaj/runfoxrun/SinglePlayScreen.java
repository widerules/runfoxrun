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
	private custString collectionText;
	
	private Sprite player;
	private Sprite badGuy;
	private Sprite back;
	
	private boolean initialized = false;
	
	private int levelNumber = 1;
	
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
		pm.setScrollRate(-17.0f / 100.0f);
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
			
			if (pm.getScrollProgress() >= 800)
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
			if (pm.getScrollProgress() >= levelList.get(levelNumber - 1).getLevelLength())
			{
				hitList.clear();
				pm.nextLevel();
				
				levelNumber++;
				
				HighScores.setLevel(levelNumber);
				
				pm.setBackDiv(((float)levelList.get(levelNumber - 1).getLevelLength()) / (1600.0f));
				//back.setxPos(-1600 * (levelNumber - 1));
				
				if (levelNumber == 2)
				{
					mm.ChangeSongs(R.raw.quicken, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
				}
				else if (levelNumber == 3)
				{
					mm.ChangeSongs(R.raw.aegissprint, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
				}
				else if (levelNumber == 4)
				{
					mm.ChangeSongs(R.raw.blackdiamond, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
					pm.setScrollRate((float) (pm.getScrollRate() - .025));
					
					pm.setBackDiv(((float)levelList.get(levelNumber - 1).getLevelLength()) / (1600.0f - (float)(width)));
				}
				
				pm.setScrollProgress(0, false);
				
				grabHitList(levelNumber);
			}
			else if (levelNumber == 3 && pm.getScrollProgress() >= 16000 - width - 200 && pm.getScrollProgress() < 16000 - width - 200 + pm.getScrollDelta() * delta)
				sceneDead = true;
			else if (levelNumber == 3 && pm.getScrollProgress() >= 16000 - width + 100 && pm.getScrollProgress() < 16000 - width + 100 + pm.getScrollDelta() * delta)
			{
				if (player.getCurAnimation().equalsIgnoreCase(CharStates.Running.name()))
				{
					pm.unsetPlayer();
					pm.addPhys(player);
					player.setAnimation(CharStates.Collapse);
				}
			}
			else if (levelNumber == 4 && sceneDead && pm.getScrollProgress() >= 200)
			{
				sceneDead = false;
				pm.setPlayer(player);
				player.setAnimation(CharStates.Running);
				this.setPlayerPos();
				badGuy.setyPos(-height - badGuy.getHeight() - 10);
			}
			else if (levelNumber == 4 && pm.getScrollProgress() >= 16000 - width - 250 && pm.getScrollProgress() < 16000 - width - 200 + pm.getScrollDelta() * delta && credits == 0)
			{
				credits += delta;
			}
			else if (levelNumber == 4 && pm.getScrollProgress() >= 16000 - width - 25)
			{
				if (player.getCurAnimation().equalsIgnoreCase(CharStates.Running.name()))
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
					return false;
				
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
			}
			
			// set me collections
			collectionText.setString("x " + String.valueOf(collectionScore));
			collectionScoreIcon.onUpdate(delta);
			
			// gotta loop through and find the collected elements
			for (Iterator<Sprite> it = collectionList.iterator(); it.hasNext();)
			{
				Sprite temp = it.next();
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
			back.onDraw(canvas);
			
			// interaction layer
			for (Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
			{
				Sprite temp = it.next();
				
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
			canvas.drawLine(pad, 20, width - pad, 20, linePaint);
			canvas.drawLine(pad, 15, pad, 27, linePaint);
			canvas.drawLine(width - pad, 15, width - pad, 27, linePaint);
			canvas.drawBitmap(progressBarIcon, linInterp(800, levelList.get(levelNumber - 1).getLevelLength() + 400, pm.getScrollProgress(), pad, width - pad), 0, bitmapPaint);
			
			// more overlay
			collectionScoreIcon.onDraw(canvas);
			collectionText.onDraw(canvas);
			
			if (credits > 15000)
				myCredits.onDraw(canvas);
		}
	}
	
	Paint deleteme = new Paint();
	
	private void start()
	{
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run()
	{
		back = new Sprite();
		back.onInitialize(LoadedResources.getBigBackGround(), 0, height - 480, 6400, 480);
		
		progressBarIcon = LoadedResources.getIcon(resources);
		// load dat bad guy
		this.badGuy = XMLHandler.readSerialFile(resources, R.raw.smoke, Sprite.class);
		badGuy.onInitialize(LoadedResources.getBadGuy(resources), -165, height - 470, 164, 470);
		
		collectionScore = 0;
		
		linePaint = new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setStrokeWidth(1);
		linePaint.setShadowLayer(1, 0, 0, Color.BLACK);
		
		bitmapPaint = new Paint();
		
		collectionText = new custString(resources, "", width - (int) (37 * scale), (int) (16 * scale));
		collectionScoreIcon = XMLHandler.readSerialFile(resources, R.raw.star, Sprite.class);
		collectionScoreIcon.onInitialize(LoadedResources.getStar(resources), width - (int) (57 * scale), (int) (3 * scale), 25, 24);
		
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
		
		mm.ChangeSongs(R.raw.pulse);
		mm.addFade(new SoundFade(0, 0, 1, 3000));
		mm.play(0);
		
		pm.setBackDiv(((float)levelList.get(levelNumber - 1).getLevelLength()) / (1600.0f));
		
		initialized = true;
		
		// pm.setScrollProgress(14800);
	}
	
	// really should make my own Math class...
	private float linInterp(float minX, float maxX, float value, float minY, float maxY)
	{
		if (minX == maxX)
			return minY;
		
		return minY * (value - maxX) / (minX - maxX) + maxY * (value - minX) / (maxX - minX);
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
