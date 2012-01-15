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
	private final int speedup = 10;
	private float initialSpeed;
	private final int restartCount = 6;
	
	public ContinousScreen(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public void onInitialize(Resources resources, InputManager im, PhysicsManager pm, Sprite player)
	{
		this.im = im;
		this.pm = pm;
		mResources = resources;
		
		this.player = player;
		
		hitList = new ArrayList<Sprite>();
		
		startLevel();
		
		initialSpeed = pm.getScrollRate();
		player.setyPos(startHeight);
		
		background1 = new Sprite();
		background1.onInitialize(LoadedResources.getBackground1(resources)); //should be different
		pm.addBackgroundPhys(background1);
		
		initialized = true;
	}
	
	public void onUpdate(float delta)
	{
		if (initialized)
		{	
			// everything below this line
			
			//background logics
			Sprite back = background1;
			if(back.getxPos() + back.getWidth() <= 0)
			{
				back.setxPos(0);
			}
			if(back.getxPos() >= width)
			{
				back.setxPos(0);
			}
			
			
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
				
				pm.setScrollProgress(1);
				
				player.setyPos(startHeight);
				
				restartLevel();
				
				pm.setScrollRate(initialSpeed * speedup);
				
				diedRecently = true;
			}
			
			//still handling death
			if(player.getyPos() > 0 && diedRecently)
			{
				diedRecently = false;
				pm.setScrollRate(initialSpeed);
			}
			
			// load in new blocks
			Sprite last = hitList.get(hitList.size() - 1);
			if (last.getxPos() + last.getWidth() < width)
			{
				int rand = random.nextInt(3);
				
				int x;
				int y;
				
				//first we want to know how far in the future we can code
				double prevY = (height - last.getyPos()); // get this in real world corridnates
				
				float newY1;
				float newY2;
				
				int newMaxX = (int) ((280 + prevY) / 1.0f);		
				int newX = random.nextInt(newMaxX);
				
				//newY1 = (float) (-Math.abs(.777f * newX -140) + 140 + prevY);
				newY1 = (float)((-1.0f * newX) + 280 + prevY);
				
				if(newY1 > 160)
					newY1 = 159;

				newY2 = (float) (-1.0f * newX + prevY);
				
				if(newY2 <= 9)
					newY2 = 10;
				
				if(newY1 - newY2 <= 0)
				{
					newY1 = 10;
					newY2 = 0;
				}
				
				int newFinalY = (int) (random.nextInt((int) (newY1 - newY2)) + newY2);
				
				y = height - newFinalY;
				x = newX + width;
				
				
				Sprite temp = new Sprite();
				
				if (rand == 0)
				{
					temp.onInitialize(LoadedResources.getGreen(mResources), x, y);
				}
				else if (rand == 1)
				{
					temp.onInitialize(LoadedResources.getRed(mResources), x, y);
				}
				else if (rand == 2)
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
			//background
			int backheight = height - background1.getHeight();
			background1.onDraw(canvas, (int) -background1.getxPos(), backheight);
			float backPos = (background1.getxPos() + background1.getWidth());
			if(backPos <= width)
			{
				background1.onDraw(canvas, -(int)backPos, backheight);
			}
			if(background1.getxPos() >= 0)
			{
				background1.onDraw(canvas, (int) -(-background1.getWidth() + background1.getxPos()), backheight);
			}
			
			//objects
			for (int i = 0; i < hitList.size(); i++)
			{
				if (hitList.get(i).getxPos() + hitList.get(i).getWidth() > 0)
					hitList.get(i).onDraw(canvas);
			}
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
			if(!hitList.get(i).getInitialized())
			{
				hitList.get(i).onInitialize(mResources, R.drawable.green, (e * 180) + width + 1, height - 10); // TODO shit.
				this.pm.addPhys(hitList.get(i));
			
				e--;
			}
			else
				break;
		}
	}
}
