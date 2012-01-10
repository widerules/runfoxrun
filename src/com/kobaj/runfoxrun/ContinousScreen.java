package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Canvas;

public class ContinousScreen
{
	private ArrayList<Sprite> hitList;
	
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
		// THIS CLASS IS NOT COMPLETE: simply an example of how a Screen can be
		// used
		// will be completed later.
		
		this.im = im;
		this.pm = pm;
		mResources = resources;
		
		this.player = player;
		
		hitList = new ArrayList<Sprite>();
		
		startLevel();
		
		initialSpeed = pm.getScrollRate();
		player.setyPos(startHeight);
		
		initialized = true;
	}
	
	public void onUpdate(float delta)
	{
		if (initialized)
		{	
			// everything below this line
			
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
				
				int x = random.nextInt(width / 2);
				x += width;
				int y = random.nextInt(height / 2);
				y += height / 2;
				
				Sprite temp = new Sprite();
				
				if (rand == 0)
				{
					temp.onInitalize(mResources, R.drawable.green, x, y);
				}
				else if (rand == 1)
				{
					temp.onInitalize(mResources, R.drawable.red, x, y);
				}
				else if (rand == 2)
				{
					temp.onInitalize(mResources, R.drawable.blue, x, y);
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
			for (int i = 0; i < hitList.size(); i++)
			{
				if (hitList.get(i).getxPos() + hitList.get(i).getWidth() > 0)
					hitList.get(i).onDraw(canvas);
			}
		}
	}
	
	private void startLevel()
	{
		for (int i = 0; i < 4; i++)
			hitList.add(new Sprite());
		
		for (int i = 0; i < hitList.size(); i++)
		{
			hitList.get(i).onInitalize(mResources, R.drawable.green, i * 180, height - 10); // TODO
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
				hitList.get(i).onInitalize(mResources, R.drawable.green, (e * 180) + width + 1, height - 10); // TODO shit.
				this.pm.addPhys(hitList.get(i));
			
				if(e < 0)
				{
					e = 1000;	
				}
				
				e--;
			}
			else
				break;
		}
	}
}
