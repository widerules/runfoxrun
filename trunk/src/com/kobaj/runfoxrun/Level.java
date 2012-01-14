package com.kobaj.runfoxrun;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import android.content.res.Resources;

public class Level
{
	private Sprite background1;	
	
	@Element
	private int playerStartx = 0;
	@Element
	private int playerStarty = 0;
	
	@Attribute
	private String levelName;
	
	@ElementList 
	private ArrayList<LevelObject> levelObjectList;
	
	private ArrayList<Sprite> levelSpriteList;
	private int levelLength;
	
	@SuppressWarnings("unused")
	private int height;
	@SuppressWarnings("unused")
	private int width;

	//delete me
	public void populatelevelObjects(ArrayList<LevelObject> list)
	{
		levelObjectList = list;
	}
	
	public int getLevelLength()
	{
		return levelLength;
	}
	
	public int getPlayerStartX()
	{
		return playerStartx;
	}	
	
	public int getPlayerStartY()
	{
		return playerStarty;
	}
	
	public ArrayList<Sprite> getlevelSpriteList()
	{
		return levelSpriteList;
	}
	
	public String getLevelName()
	{
		return levelName;
	}
	
	public Level(@Attribute (name = "levelName") String levelName)
	{	
		this.levelName = levelName;
		
		if(levelObjectList == null)
			levelObjectList = new ArrayList<LevelObject>();
	}
	
	public void onInitialize(Resources resources, int width, int height, SoundManager sm)
	{
		this.height = height;
		this.width = width;
		
		background1 = new Sprite();
		background1.onInitialize(LoadedResources.getBackground1(resources));
				
		levelSpriteList = new ArrayList<Sprite>();
		
		//should be an iterator
		for(int i = 0; i < levelObjectList.size(); i++)
		{	
			if(!levelObjectList.get(i).getOrientation())
			{
				levelObjectList.get(i).setyLoc(height - levelObjectList.get(i).getyLoc());
			}
			
			//hard coded
			//its a little dumb, but thats how it goes
			if(levelObjectList.get(i).getName().equalsIgnoreCase("green"))
			{
				Sprite temp = new Sprite(); 
				//will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.green, Sprite.class);
				temp.onInitialize(LoadedResources.getGreen(resources), levelObjectList.get(i).getxLoc(), levelObjectList.get(i).getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(levelObjectList.get(i).getName().equalsIgnoreCase("red"))
			{
				Sprite temp = new Sprite(); 
				//will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.red, Sprite.class);
				temp.onInitialize(LoadedResources.getRed(resources), levelObjectList.get(i).getxLoc(), levelObjectList.get(i).getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(levelObjectList.get(i).getName().equalsIgnoreCase("blue"))
			{
				Sprite temp = new Sprite(); 
				//will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.blue, Sprite.class);
				temp.onInitialize(LoadedResources.getBlue(resources), levelObjectList.get(i).getxLoc(), levelObjectList.get(i).getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(levelObjectList.get(i).getName().equalsIgnoreCase("star"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.star, Sprite.class);
				temp.onInitialize(LoadedResources.getStar(resources), sm, levelObjectList.get(i).getxLoc(), levelObjectList.get(i).getyLoc(), 25, 24);
				temp.setCollectable(CollectableStates.collectable);
				levelSpriteList.add(temp);
			}
		}
		
		levelLength = (int) (levelSpriteList.get(levelSpriteList.size() - 1).getxPos() + levelSpriteList.get(levelSpriteList.size() - 1).getWidth());
	}

	
	//stupid stupid stupid stupid
	
	public Sprite getBackground1()
	{
		return background1;
	}

	public void setBackground1(Sprite background1)
	{
		this.background1 = background1;
	}
}
