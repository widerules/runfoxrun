package com.kobaj.runfoxrun;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import android.content.res.Resources;

public class Level
{
	@Element
	private int playerStartx = 0;
	@Element
	private int playerStarty = 0;
	
	@Attribute
	private String levelName;
	
	@ElementList 
	private ArrayList<LevelObject> levelObjectList;
	
	private ArrayList<Sprite> levelSpriteList;

	//delete me
	public void populatelevelObjects(ArrayList<LevelObject> list)
	{
		levelObjectList = list;
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
	
	public void onInitialize(Resources resources)
	{
		levelSpriteList = new ArrayList<Sprite>();
		
		for(int i = 0; i < levelObjectList.size(); i++)
		{
			//hard coded
			//its a little dumb, but thats how it goes
			if(levelObjectList.get(i).getName().equalsIgnoreCase("green"))
			{
				Sprite temp = new Sprite(); 
				//will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.green, Sprite.class);
				temp.onInitalize(resources, R.drawable.green, levelObjectList.get(i).getxLoc(), levelObjectList.get(i).getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(levelObjectList.get(i).getName().equalsIgnoreCase("red"))
			{
				Sprite temp = new Sprite(); 
				//will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.red, Sprite.class);
				temp.onInitalize(resources, R.drawable.red, levelObjectList.get(i).getxLoc(), levelObjectList.get(i).getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(levelObjectList.get(i).getName().equalsIgnoreCase("blue"))
			{
				Sprite temp = new Sprite(); 
				//will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.blue, Sprite.class);
				temp.onInitalize(resources, R.drawable.blue, levelObjectList.get(i).getxLoc(), levelObjectList.get(i).getyLoc());
				levelSpriteList.add(temp);
			}
		}
	}
}
