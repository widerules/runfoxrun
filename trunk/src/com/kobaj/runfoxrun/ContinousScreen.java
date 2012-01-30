package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ContinousScreen
{
	private ArrayList<Sprite> hitList;
	private Sprite background1;
	private Sprite background2;
	
	private Resources resources;
	
	private InputManager im;
	private PhysicsManager pm;
	
	Random random = new Random();
	int nextval = 0;
	
	private int width;
	private int height;
	
	private Sprite player;
	
	private boolean initialized = false;
	
	private boolean diedRecently = false;
	
	private float speedUp = 10;
	private float initialSpeed;
	private final int restartCount = 6;
	
	private int score = 0;
	private custInt scoreString;
	private custString scoreWord;
	private custInt highScore;
	private custString highScoreWords;
	
	private SoundManager sm;
	
	private Sprite temp;
	
	private Paint bitmapPaint;
	
	float scale;
	
	public ContinousScreen(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	private void setPlayerPos()
	{
		player.setxPos((int) (width / 3.0f));
		player.setyPos(SurfacePanel.startHeight);
	}
	
	public void onInitialize(Resources resources, InputManager im, PhysicsManager pm, SoundManager sm, Sprite player)
	{
		scale = SurfacePanel.scale;
		
		this.im = im;
		this.pm = pm;
		this.sm = sm;
		this.resources = resources;
		
		bitmapPaint = new Paint();
		
		this.player = player;
		setPlayerPos();
		pm.setPlayer(player);
		player.setPaintColorFilter(255);
		
		hitList = new ArrayList<Sprite>();
		
		startLevel();
		
		initialSpeed = SurfacePanel.scrollRate;
		pm.setScrollRate(initialSpeed);
		setPlayerPos();
		
		background1 = new Sprite();
		background1.onInitialize(LoadedResources.getBackground1(resources), 0, height - LoadedResources.getBackground1(resources).getHeight(), LoadedResources.getBackground1(resources).getWidth(),
				LoadedResources.getBackground1(resources).getHeight());
		pm.addBackgroundPhys(background1);
		
		background2 = new Sprite();
		background2.onInitialize(LoadedResources.getBackground1(resources), background1.getWidth(), height - LoadedResources.getBackground1(resources).getHeight(),
				LoadedResources.getBackground1(resources).getWidth(), LoadedResources.getBackground1(resources).getHeight());
		pm.addBackgroundPhys(background2);
		
		scoreString = new custInt(resources, 0, width - (int) (120 * scale), (int) (16 * scale));
		scoreWord = new custString(resources, "Score: ", width - (int) (167 * scale), (int) (16 * scale));
		
		highScoreWords = new custString(resources, "High:", width - (int) (167 * scale), (int) (33 * scale));
		highScore = new custInt(resources, HighScores.getHighScore(), width - (int) (120 * scale), (int) (33 * scale));
		
		initialized = true;
	}
	
	public boolean getInitialized()
	{
		return initialized;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void onUpdate(float delta)
	{
		if (initialized)
		{
			// everything below this line
			
			// score
			score += (1.0f * delta / 10.0f) / 1.5f * scale;
			scoreString.setInt(score);
			
			// slowly get faster
			pm.setScrollRate(pm.getScrollRate() - delta / (1000000.0f - 5000.0f));
			
			// background logics
			if (background1.getxPos() + background1.getWidth() <= 0)
				background1.setxPos(background2.getxPos() + background2.getWidth());
			
			if (background2.getxPos() + background2.getWidth() <= 0)
				background2.setxPos(background1.getxPos() + background1.getWidth());
			
			// get rid of old blocks
			
			for (Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
			{
				temp = it.next();
				if (temp.getxPos() + temp.getWidth() < 0)
				{
					pm.removePhys(temp);
					//it.remove();
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
				
				highScore.setInt(HighScores.getHighScore());
				
				score = 0;
			}
			
			// still handling death
			if (player.getyPos() > height - this.background1.getHeight() && diedRecently)
			{
				diedRecently = false;
				pm.setScrollRate(initialSpeed);
			}
			
			// load in new blocks
			if (hitList.get(hitList.size() - 1).getxPos() + hitList.get(hitList.size() - 1).getWidth() < width)
			{
				int x;
				int y;
				
				// first we want to know how far in the future we can code
				double prevY = (height - hitList.get(hitList.size() - 1).getyPos()); // get
																						// this
																						// in
																						// real
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
				
				y = (int) (height - newFinalY / 1.5f * scale);
				x = (int) (newX / 1.5f * scale + width);
				
				temp = new Sprite();
				
				int rand = random.nextInt(30);
				
				if (rand >= 0 && rand <= 12)
				{
					temp.onInitialize(LoadedResources.getGreen(resources), x, y);
				}
				else if (rand > 12 && rand <= 25)
				{
					temp.onInitialize(LoadedResources.getRed(resources), x, y);
				}
				else if (rand > 25)
				{
					temp.onInitialize(LoadedResources.getBlue(resources), x, y);
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
			
			//little fox
			player.onDraw(canvas);
			
			//black box
			if(height - LoadedResources.getBackground1(resources).getHeight() > 0)
				canvas.drawRect(0, 0, width, height - LoadedResources.getBackground1(resources).getHeight(), bitmapPaint);
			
			// score
			scoreString.onDraw(canvas);
			scoreWord.onDraw(canvas);
			highScoreWords.onDraw(canvas);
			highScore.onDraw(canvas);
		}
	}
	
	private void startLevel()
	{
		for (int i = 0; i < restartCount; i++)
			hitList.add(new Sprite());
		
		for (int i = 0; i < hitList.size(); i++)
		{
			hitList.get(i).onInitialize(resources, R.drawable.green, (int) (i * 180.0f / 1.5f * scale), height - 10); // TODO
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
			temp = it.next();
			if (temp.getxPos() > width || temp.getxPos() + temp.getWidth() < 0)
			{
				pm.removePhys(temp);
				// pm.removePhys(temp);
				it.remove();
			}
		}
		
		System.gc();
		
		int e = 0;
		for (; e < restartCount; e++)
			hitList.add(new Sprite());
		
		e++;
		
		for (int i = hitList.size() - 1; i >= 0; i--)
		{
			if (!hitList.get(i).getInitialized())
			{
				hitList.get(i).onInitialize(LoadedResources.getGreen(resources), (int) ((e * 180.0f / 1.5f * scale) + width + 1), height - 10);
				this.pm.addPhys(hitList.get(i));
				
				e--;
			}
			else
				break;
		}
	}
}
