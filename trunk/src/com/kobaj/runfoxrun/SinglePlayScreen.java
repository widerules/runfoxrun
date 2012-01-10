package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.res.Resources;
import android.graphics.Canvas;

public class SinglePlayScreen implements Runnable
{
	//thread stuff
	private Thread thread;
	
	private int width;
	private int height;
	
	private ArrayList<Sprite> hitList;
	
	private Level level;
	private int levelInt;
	
	private InputManager im;
	private PhysicsManager pm;
	private Resources resources;
	
	private Sprite player;
	
	private boolean initialized = false;
	
	//for testing purposes, m
	//delet me later
	public static void writelevel()
	{
		Level lev = new Level("test map");
		
		ArrayList<LevelObject> templist = new ArrayList<LevelObject>();
		templist.add(new LevelObject("green", 0, 470));
		templist.add(new LevelObject("green", 200, 460));
		templist.add(new LevelObject("green", 400, 470));
		
		lev.populatelevelObjects(templist);
		
		XMLHandler.writeSerialFile(lev, "level");
	}
	
	private void setPlayerPos()
	{
		player.setxPos(level.getPlayerStartX());
		player.setyPos(level.getPlayerStartY());
	}
	
	public SinglePlayScreen(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public boolean getInitialized()
	{
		return initialized;
	}
	
	public void onInitialize(Resources resources, InputManager im, PhysicsManager pm, int level, Sprite player)
	{
		this.im = im;
		this.pm = pm;
		
		this.player = player;
		
		this.resources = resources;
		
		this.levelInt = level;
		
		start();
	}

	public void onUpdate()
	{
		if (initialized)
		{
			//handle removing old stuffs
			/*for (Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
			{
				Sprite temp = it.next();
				
				if (temp.getxPos() + temp.getWidth() < 0)
				{
					pm.removePhys(temp);
					it.remove();
				}
			}*/
			
			//handle input
			for (int i = 0; i < im.fingerCount; i++)
			{
				if (im.getPressed(i))
				{
					pm.jump();
				}
			}
			
			//handle death;
			if(pm.getDeath())
			{
				pm.levelReset();
			}
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		if (initialized)
		{
			for(int i = 0; i < hitList.size(); i++)
			{	
				if(hitList.get(i).getxPos() + hitList.get(i).getWidth() > 0) 
					hitList.get(i).onDraw(canvas);
				if(hitList.get(i).getxPos() > width + 20)
					break;
			}
		}
	}
	
	private void start()
	{
		thread = new Thread(this);
		thread.start();
	}
	

	@Override
	public void run()
	{
		// load in the level
		this.level = XMLHandler.readSerialFile(resources, levelInt, Level.class);
		this.level.onInitialize(resources);
		
		// grab the hit list;
		hitList = new ArrayList<Sprite>();
		grabHitList();
		
		setPlayerPos();
		
		initialized = true;
	}
	
	private void grabHitList()
	{
		hitList.clear();
		for(Iterator<Sprite> it = this.level.getlevelSpriteList().iterator(); it.hasNext();)
		{
			Sprite temp = it.next();
			hitList.add(temp);
			pm.addPhys(temp);
		}
	}
}
