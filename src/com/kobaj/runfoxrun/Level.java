package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;

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
				
		levelSpriteList = new ArrayList<Sprite>();
		
		for(Iterator<LevelObject> it = levelObjectList.iterator(); it.hasNext();)
		{	
			LevelObject tempLevelObject = it.next();
			
			if(!tempLevelObject.getOrientation())
			{
				tempLevelObject.setyLoc(height - tempLevelObject.getyLoc());
			}
			
			//hard coded
			//its a little dumb, but thats how it goes
			if(tempLevelObject.getName().equalsIgnoreCase("green"))
			{
				Sprite temp = new Sprite(); 
				//will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.green, Sprite.class);
				temp.onInitialize(LoadedResources.getGreen(resources), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("red"))
			{
				Sprite temp = new Sprite(); 
				//will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.red, Sprite.class);
				temp.onInitialize(LoadedResources.getRed(resources), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("blue"))
			{
				Sprite temp = new Sprite(); 
				//will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.blue, Sprite.class);
				temp.onInitialize(LoadedResources.getBlue(resources), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("star"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.star, Sprite.class);
				temp.onInitialize(LoadedResources.getStar(resources), sm, tempLevelObject.getxLoc(), tempLevelObject.getyLoc(), 25, 24);
				temp.setCollectable(CollectableStates.collectable);
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("bigbuilding"))
			{
				Sprite temp = new Sprite(); //XMLHandler.readSerialFile(resources, R.raw., Sprite.class);
				temp.onInitialize(LoadedResources.getBigBuilding(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("smallbuilding"))
			{
				Sprite temp = new Sprite(); //XMLHandler.readSerialFile(resources, R.raw., Sprite.class);
				temp.onInitialize(LoadedResources.getSmallBuilding(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("sandflat"))
			{
				Sprite temp = new Sprite(); //XMLHandler.readSerialFile(resources, R.raw., Sprite.class);
				temp.onInitialize(LoadedResources.getSandFlat(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("bigvine"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.bigvine, Sprite.class);
				temp.onInitialize(LoadedResources.getBigVine(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - LoadedResources.getBigVine().getHeight());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("grass"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.grass, Sprite.class);
				temp.onInitialize(LoadedResources.getGrass(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("littlevine"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.littlevine, Sprite.class);
				temp.onInitialize(LoadedResources.getLittleVine(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - LoadedResources.getLittleVine().getHeight());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("sandhole"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.sandhole, Sprite.class);
				temp.onInitialize(LoadedResources.getSandHole(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("sandholeleft"))
			{
				Sprite temp = new Sprite();//XMLHandler.readSerialFile(resources, R.raw.sandholeleft, Sprite.class);
				temp.onInitialize(LoadedResources.getSandHoleLeft(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("sandholeright"))
			{
				Sprite temp = new Sprite(); //XMLHandler.readSerialFile(resources, R.raw.sandholeright, Sprite.class);
				temp.onInitialize(LoadedResources.getSandHoleRight(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("smalltree"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.smalltree, Sprite.class);
				temp.onInitialize(LoadedResources.getSmallTree(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - temp.getPhysRect().get(0).getCollRect().top);
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("tree"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.tree, Sprite.class);
				temp.onInitialize(LoadedResources.getTree(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - temp.getPhysRect().get(0).getCollRect().top);
				levelSpriteList.add(temp);
			}
			
			else if(tempLevelObject.getName().equalsIgnoreCase("weed"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.weed, Sprite.class);
				temp.onInitialize(LoadedResources.getWeed(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - LoadedResources.getWeed().getHeight());
				levelSpriteList.add(temp);
			}
		}
		
		levelLength = 16000;//(int) (levelSpriteList.get(levelSpriteList.size() - 1).getxPos() + levelSpriteList.get(levelSpriteList.size() - 1).getWidth());
	}
}
