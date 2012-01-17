package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Canvas;

public class ContinousScreen
{
	private ArrayList<Sprite> hitList;
	private Sprite background1;
	private Sprite background2;
	
	private Resources mResources;
	
	private InputManager im;
	private PhysicsManager pm;
	
	Random random = new Random();
	int nextval = 0;
	
	private int width;
	private int height;
	
	private Sprite player;
	
	private boolean initialized = false;
	
	private boolean diedRecently = false;
	
	private final int startHeight = -100;
	private final int speedUp = 10;
	private float initialSpeed;
	private final int restartCount = 6;
	
	private int score = 0;
	private custString scoreString;
	private custString scoreWord;
	
	private SoundManager sm;
	
	public ContinousScreen(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	private void setPlayerPos()
	{
		player.setxPos((int) (width / 3.0f));
		player.setyPos(startHeight);
	}
	
	public void onInitialize(Resources resources, InputManager im, PhysicsManager pm, SoundManager sm, Sprite player)
	{
		float scale = resources.getDisplayMetrics().density;
		
		this.im = im;
		this.pm = pm;
		this.sm = sm;
		mResources = resources;
		
		this.player = player;
		
		setPlayerPos();
		
		pm.setPlayer(player);
		
		hitList = new ArrayList<Sprite>();
		
		startLevel();
		
		initialSpeed = -17.0f / 100.0f;
		pm.setScrollRate(initialSpeed);
		player.setyPos(startHeight);
		
		background1 = new Sprite();
		background1.onInitialize(LoadedResources.getBackground1(resources), 0 , height - LoadedResources.getBackground1(resources).getHeight(), LoadedResources.getBackground1(resources).getWidth(), LoadedResources.getBackground1(resources).getHeight());
		pm.addBackgroundPhys(background1);
		
		background2 = new Sprite();
		background2.onInitialize(LoadedResources.getBackground1(resources), background1.getWidth(), height - LoadedResources.getBackground1(resources).getHeight(), LoadedResources.getBackground1(resources).getWidth(), LoadedResources.getBackground1(resources).getHeight());
		pm.addBackgroundPhys(background2);
		
		scoreString = new custString(resources, "", width - (int)(120 * scale), (int)(16 * scale));
		scoreWord = new custString(resources, "Score: ", width - (int)(167 * scale), (int)(16 * scale));
		
		initialized = true;
	}
	
	public void onUpdate(float delta)
	{
		if (initialized)
		{
			// everything below this line
			
			// score
			score += 1.0f * delta / 10.0f;
			String s = "000000000000" + String.valueOf(score); // twelve zeros
																// prepended
			scoreString.setString(s.substring(s.length() - 13)); // keep the
																	// rightmost
																	// 13 chars
			
			// slowly get faster
			pm.setScrollRate(pm.getScrollRate() - delta / (1000000.0f - 5000.0f));
			
			// background logics
			if(background1.getxPos() + background1.getWidth() <= 0)
				background1.setxPos(background2.getxPos() + background2.getWidth());
			
			if(background2.getxPos() + background2.getWidth() <= 0)
				background2.setxPos(background1.getxPos() + background1.getWidth());
			
			// get rid of old blocks
			for (Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
			{
				Sprite temp = it.next();
				if (temp.getxPos() + temp.getWidth() < 0)
				{
					pm.removePhys(temp);
					it.remove();
				}
			}
			
			// handle death
			if (pm.getDeath())
			{
				pm.reset();
				
				this.setPlayerPos();
				
				restartLevel();
				
				pm.setScrollRate(initialSpeed * speedUp);
				
				diedRecently = true;
				
				sm.playSound(3);
				
				HighScores.addScore(score);
				
				score = 0;
			}
			
			// still handling death
			if (player.getyPos() > 0 && diedRecently)
			{
				diedRecently = false;
				pm.setScrollRate(initialSpeed);
			}
			
			// load in new blocks
			Sprite last = hitList.get(hitList.size() - 1);
			if (last.getxPos() + last.getWidth() < width)
			{
				int x;
				int y;
				
				// first we want to know how far in the future we can code
				double prevY = (height - last.getyPos()); // get this in real
															// world corridnates
				
				float newY1;
				float newY2;
				
				int newMaxX = (int) ((280 + prevY) / 1.0f);
				int newX = random.nextInt(newMaxX);
				
				// newY1 = (float) (-Math.abs(.777f * newX -140) + 140 + prevY);
				newY1 = (float) ((-1.0f * newX) + 280 + prevY);
				
				if (newY1 > 160)
					newY1 = 159;
				
				newY2 = (float) (-1.0f * newX + prevY);
				
				if (newY2 <= 9)
					newY2 = 10;
				
				if (newY1 - newY2 <= 0)
				{
					newY1 = 10;
					newY2 = 0;
				}
				
				int newFinalY = (int) (random.nextInt((int) (newY1 - newY2)) + newY2);
				
				y = height - newFinalY;
				x = newX + width;
				
				Sprite temp = new Sprite();
				
				int rand = random.nextInt(30);
				
				if (rand >= 0 && rand <= 12)
				{
					temp.onInitialize(LoadedResources.getGreen(mResources), x, y);
				}
				else if (rand > 12 && rand <= 25)
				{
					temp.onInitialize(LoadedResources.getRed(mResources), x, y);
				}
				else if (rand > 25)
				{
					temp.onInitialize(LoadedResources.getBlue(mResources), x, y);
				}
				
				hitList.add(temp);
				pm.addPhys(temp);
			}
			
			// input
			for (int i = 0; i < im.fingerCount; i++)
			{
				if (im.getPressed(i))
				{
					pm.jump();
				}
			}
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		if (initialized)
		{
			// background
			background1.onDraw(canvas);
			background2.onDraw(canvas);
			
			// objects
			for (int i = 0; i < hitList.size(); i++)
			{
				if (hitList.get(i).getxPos() + hitList.get(i).getWidth() > 0)
					hitList.get(i).onDraw(canvas);
			}
			
			// score
			scoreString.onDraw(canvas);
			scoreWord.onDraw(canvas);
		}
	}
	
	private void startLevel()
	{
		for (int i = 0; i < restartCount; i++)
			hitList.add(new Sprite());
		
		for (int i = 0; i < hitList.size(); i++)
		{
			hitList.get(i).onInitialize(mResources, R.drawable.green, i * 180, height - 10); // TODO
																								// fix
																								// this
																								// shit.
			this.pm.addPhys(hitList.get(i));
		}
	}
	
	// poor way of doing this.
	private void restartLevel()
	{
		// get rid of forward blocks
		for (Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
		{
			Sprite temp = it.next();
			if (temp.getxPos() > width)
			{
				pm.removePhys(temp);
				it.remove();
			}
		}
		
		int e = 0;
		for (; e < restartCount; e++)
			hitList.add(new Sprite());
		
		e++;
		
		for (int i = hitList.size() - 1; i >= 0; i--)
		{
			if (!hitList.get(i).getInitialized())
			{
				hitList.get(i).onInitialize(LoadedResources.getGreen(mResources), (e * 180) + width + 1, height - 10);
				this.pm.addPhys(hitList.get(i));
				
				e--;
			}
			else
				break;
		}
	}
}
