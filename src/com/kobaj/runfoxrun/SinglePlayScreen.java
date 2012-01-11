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
	
	//top level
	private Bitmap progressBarIcon;
	private Paint linePaint;
	
	//for testing purposes, m
	//delete me later
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
		//player.setxPos(level.getPlayerStartX());
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
		
		progressBarIcon = LoadedResources.getIcon(resources);
		
		linePaint = new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setStrokeWidth(1);
		linePaint.setShadowLayer(1, 0, 0, Color.BLACK);
		
		start();
	}

	public void onUpdate()
	{
		if (initialized)
		{	
			//handle input
			for (int i = 0; i < im.fingerCount; i++)
			{
				if (im.getPressed(i))
				{
					pm.jump();
				}
			}
			
			//background logics
			Sprite back = this.level.getBackground1();
			if(back.getxPos() + back.getWidth() <= 0)
			{
				back.setxPos(0);
			}
			if(back.getxPos() >= width)
			{
				back.setxPos(0);
			}
			
			//handle death;
			if(pm.getDeath())
			{
				//pm.levelReset();
			}
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		if (initialized)
		{
			//background
			int backheight = height - this.level.getBackground1().getHeight();
			Sprite back = this.level.getBackground1();
			back.onDraw(canvas, (int) -back.getxPos(), backheight);
			float backPos = (back.getxPos() + back.getWidth());
			if(backPos <= width)
			{
				back.onDraw(canvas, -(int)backPos, backheight);
			}
			if(back.getxPos() >= 0)
			{
				back.onDraw(canvas, (int) -(-back.getWidth() + back.getxPos()), backheight);
			}
			
			//interaction layer
			for(int i = 0; i < hitList.size(); i++)
			{	
				if(hitList.get(i).getxPos() + hitList.get(i).getWidth() > 0) 
					hitList.get(i).onDraw(canvas);
				if(hitList.get(i).getxPos() > width + 20)
					break;
			}
			
			//character
			player.onDraw(canvas);
			
			//overlay (I should really not be doing math/logic here >.<
			int pad = (int)(width / 4.0f);
			canvas.drawLine(pad, 20, width - pad, 20, linePaint);
			canvas.drawLine(pad, 15, pad, 27, linePaint);
			canvas.drawLine(width - pad, 15, width - pad, 27, linePaint);
			canvas.drawBitmap(progressBarIcon, linInterp(0, level.getLevelLength() , pm.getScrollProgress(), pad, width - pad), 0, null);
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
			
		pm.addBackgroundPhys(this.level.getBackground1());
		
		initialized = true;
	}
	
	//really should make my own Math class...
	private float linInterp(float minX, float maxX, float value, float minY, float maxY)
	{
		if(minX == maxX)
			return minY;
		
		return  minY*(value - maxX)/(minX - maxX) + maxY*(value - minX)/(maxX - minX);
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
