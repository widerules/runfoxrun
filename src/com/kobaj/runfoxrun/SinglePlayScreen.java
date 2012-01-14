package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class SinglePlayScreen implements Runnable
{
	//thread stuff
	private Thread thread;
	
	private int width;
	private int height;
	
	private ArrayList<Sprite> hitList;
	private ArrayList<Sprite> collectionList;
	
	private Level level;
	private int levelInt;
	
	private InputManager im;
	private PhysicsManager pm;
	private SoundManager sm;
	private Resources resources;
	
	private int collectionScore = 0;
	private custString collectionText;
	
	private Sprite player;
	private Sprite badGuy;
	
	private boolean initialized = false;
	
	//top level
	private Bitmap progressBarIcon;
	private Paint linePaint;
	
	//for testing purposes, m
	//delete me later
	/*public static void writelevel()
	{
		Level lev = new Level("test map", width, height);
		
		ArrayList<LevelObject> templist = new ArrayList<LevelObject>();
		templist.add(new LevelObject("green", 0, 470));
		templist.add(new LevelObject("green", 200, 460));
		templist.add(new LevelObject("green", 400, 470));
		
		lev.populatelevelObjects(templist);
		
		XMLHandler.writeSerialFile(lev, "level");
	}*/
	
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
	
	public void onInitialize(Resources resources, InputManager im, PhysicsManager pm, SoundManager sm, int level, Sprite player)
	{
		this.im = im;
		this.pm = pm;
		this.sm = sm;
		
		this.player = player;
		
		this.resources = resources;
		
		this.levelInt = level;
		
		progressBarIcon = LoadedResources.getIcon(resources);
		//load dat bad guy
		this.badGuy = XMLHandler.readSerialFile(resources, R.raw.smoke, Sprite.class);
		badGuy.onInitialize(LoadedResources.getBadGuy(resources), 0, height - 470 , 164, 470);
		
		collectionScore = 0;
		
		linePaint = new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setStrokeWidth(1);
		linePaint.setShadowLayer(1, 0, 0, Color.BLACK);
		
		collectionText = new custString("", width - 10, 0);
		
		start();
	}

	public void onUpdate(float delta)
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
			
			//bad guy
			badGuy.onUpdate(delta);
			
			//handle death;
			if(pm.getDeath())
			{
				pm.levelReset();
			}
			
			//set me collections
			collectionText.setString("x " + String.valueOf(collectionScore));
			
			//gotta loop through and find the collected elements
			/*for(Iterator<Sprite> it = collectionList.iterator(); it.hasNext();)
			{
				Sprite temp = it.next();
				if(temp.getCollectable() == CollectableStates.collected)
				{
					collectionScore++;
					it.remove();
					hitList.remove(temp);
					pm.removePhys(temp);
				}	
			}*/
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
			for(Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
			{
				Sprite temp = it.next();
				
				//this logic should not be here, but we're starting to lose fps, and I only want to iterate once through this.
				if(temp.getCollectable() == CollectableStates.collected)
				{
					it.remove();
					pm.removePhys(temp);
					collectionScore++;
				}
				
				if(temp.getxPos() + temp.getWidth() > 0)
					temp.onDraw(canvas);
				else if (temp.getxPos() > width + 10)
					break;
			}
			
			//character debugging
			for(Iterator<physRect> it = player.getPhysRect().iterator(); it.hasNext();)
			{
				physRect rect = it.next();
				canvas.drawRect(rect.getCollRect(), new Paint());
			}
			
			//character
			player.onDraw(canvas);
			
			//bad guy
			badGuy.onDraw(canvas);
			
			//overlay (I should really not be doing math/logic here >.<
			int pad = (int)(width / 4.0f);
			canvas.drawLine(pad, 20, width - pad, 20, linePaint);
			canvas.drawLine(pad, 15, pad, 27, linePaint);
			canvas.drawLine(width - pad, 15, width - pad, 27, linePaint);
			canvas.drawBitmap(progressBarIcon, linInterp(0, level.getLevelLength() , pm.getScrollProgress(), pad, width - pad), 0, null);
			
			//more overlay
			canvas.drawBitmap(LoadedResources.getStar(resources), width - 10, 0, null);
			collectionText.onDraw(canvas);
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
		this.level.onInitialize(resources, width, height, sm);
		
		// grab the hit list;
		hitList = new ArrayList<Sprite>();
		grabHitList();
		
		//grab collectionlist
		collectionList = new ArrayList<Sprite>();
		for(Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
		{
			Sprite temp = it.next();
			
			if(temp.getCollectable() == CollectableStates.collectable)
				collectionList.add(temp);
		}
		
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
