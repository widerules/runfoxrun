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
	
	//top level
	private Bitmap progressBarIcon;
	private Sprite collectionScoreIcon;
	private Paint linePaint;
	private Paint bitmapPaint;
	
	int pad;
	
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
		player.setyPos(levelList.get(levelNumber - 1).getPlayerStartY());
	}
	
	public SinglePlayScreen(int width, int height)
	{
		this.width = width;
		this.height = height;
		pad = (int)(width / 4.0f);
		
		levelList = new ArrayList<Level>();
	}
	
	public boolean getInitialized()
	{
		return initialized;
	}
	
	public void onInitialize(Resources resources, InputManager im, PhysicsManager pm, SoundManager sm, MusicManager mm, int level, Sprite player)
	{
		this.im = im;
		this.pm = pm;
		this.sm = sm;
		this.mm = mm;
		
		back = new Sprite();
		back.onInitialize(LoadedResources.getBackground1(resources));
		
		this.player = player;
		
		this.resources = resources;
		
		progressBarIcon = LoadedResources.getIcon(resources);
		//load dat bad guy
		this.badGuy = XMLHandler.readSerialFile(resources, R.raw.smoke, Sprite.class);
		badGuy.onInitialize(LoadedResources.getBadGuy(resources), 0, height - 470 , 164, 470);
		
		collectionScore = 0;
		
		linePaint = new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setStrokeWidth(1);
		linePaint.setShadowLayer(1, 0, 0, Color.BLACK);
		
		bitmapPaint = new Paint();
		
		collectionText = new custString("", width - 55, 24);
		collectionScoreIcon = XMLHandler.readSerialFile(resources, R.raw.star, Sprite.class);
		collectionScoreIcon.onInitialize(LoadedResources.getStar(resources), width - 85, 5, 25, 24);
		
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
			
			//handle next level;
			if(pm.getScrollProgress() >= 16000)
			{
				hitList.clear();
				pm.nextLevel();
				
				levelNumber++;
				
				if(levelNumber == 2)
					mm.ChangeSongs(R.raw.quicken, new SoundFade(0, 1, 0, 3000), new SoundFade(0,0,1,3000));
				else if(levelNumber ==3)
					mm.ChangeSongs(R.raw.aegissprint, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
						
				pm.setScrollProgress(0);
				
				grabHitList(levelNumber);
			}
			
			//background logics
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
			collectionScoreIcon.onUpdate(delta);
			
			//gotta loop through and find the collected elements
			for(Iterator<Sprite> it = collectionList.iterator(); it.hasNext();)
			{
				Sprite temp = it.next();
				temp.onUpdate(delta);
				if(temp.getCollectable() == CollectableStates.collected)
				{
					collectionScore++;
					it.remove();
					hitList.remove(temp);
					pm.removePhys(temp);
				}	
			}
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		if (initialized)
		{
			//background
			int backheight = height - back.getHeight();
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
			
			//debugging
			/*Paint debugPaint = new Paint();
			debugPaint.setColor(Color.BLACK);
		    debugPaint.setStyle(Paint.Style.STROKE);
		    debugPaint.setStrokeWidth(2);
		    debugPaint.setAntiAlias(true);*/
			
			//interaction layer
			for(Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
			{
				Sprite temp = it.next();
				
				if(temp.getxPos() + temp.getWidth() > 0)
				{
					temp.onDraw(canvas);
					
					//item debuggin
					/*for(Iterator<physRect> ite = temp.getPhysRect().iterator(); ite.hasNext();)
					{
						physRect rect = ite.next();
						if(rect.getHurts())
							debugPaint.setColor(Color.RED);
						else
							debugPaint.setColor(Color.BLACK);
						canvas.drawRect(rect.getCollRect(), debugPaint);
					}*/
					
				}
				else if (temp.getxPos() > width + 10)
					break;
			}
			
			//character debugging
			/*for(Iterator<physRect> it = player.getPhysRect().iterator(); it.hasNext();)
			{
				physRect rect = it.next();
				canvas.drawRect(rect.getCollRect(), debugPaint);
			}*/
			
			//character
			player.onDraw(canvas);
			
			//bad guy
			badGuy.onDraw(canvas);
			
			//overlay (I should really not be doing math/logic here >.<
			canvas.drawLine(pad, 20, width - pad, 20, linePaint);
			canvas.drawLine(pad, 15, pad, 27, linePaint);
			canvas.drawLine(width - pad, 15, width - pad, 27, linePaint);
			canvas.drawBitmap(progressBarIcon, linInterp(0, 16400 , pm.getScrollProgress(), pad, width - pad), 0, bitmapPaint);
			
			//more overlay
			collectionScoreIcon.onDraw(canvas);
			collectionText.onDraw(canvas);
			
			//bit of debuggings
			canvas.drawText(String.valueOf(pm.getScrollProgress()), width - 300, 150, deleteme);
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
		// load in the level
		levelList.add(XMLHandler.readSerialFile(resources, R.raw.level, Level.class));
		levelList.add(XMLHandler.readSerialFile(resources, R.raw.level2, Level.class));
		levelList.add(XMLHandler.readSerialFile(resources, R.raw.level3, Level.class));
		levelList.add(XMLHandler.readSerialFile(resources, R.raw.level4, Level.class));
		
		for(int i = 0; i < 4; i++)
			levelList.get(i).onInitialize(resources, width, height, sm);
		
		// grab the hit list;
		hitList = new ArrayList<Sprite>();
		grabHitList(levelNumber);
		
		//grab collectionlist
		collectionList = new ArrayList<Sprite>();
		for(Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
		{
			Sprite temp = it.next();
			
			if(temp.getCollectable() == CollectableStates.collectable)
				collectionList.add(temp);
		}
		
		setPlayerPos();
			
		pm.addBackgroundPhys(back);
		
		mm.ChangeSongs(R.raw.pulse);
		mm.addFade(new SoundFade(0,0,1,3000));
		mm.play(0);
		
		initialized = true;
		
		pm.setScrollProgress(15000);
	}
	
	//really should make my own Math class...
	private float linInterp(float minX, float maxX, float value, float minY, float maxY)
	{
		if(minX == maxX)
			return minY;
		
		return  minY*(value - maxX)/(minX - maxX) + maxY*(value - minX)/(maxX - minX);
	}
	
	private void grabHitList(int levelNumber)
	{
		hitList.clear();
		for(Iterator<Sprite> it = levelList.get(levelNumber - 1).getlevelSpriteList().iterator(); it.hasNext();)
		{
			Sprite temp = it.next();
			hitList.add(temp);
			pm.addPhys(temp);
		}
	}
}
