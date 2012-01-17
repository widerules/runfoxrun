package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import android.content.res.Resources;
import android.graphics.Rect;

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
	
	@Element
	private int levelLength;
	
	private int height;
	@SuppressWarnings("unused")
	private int width;
	
	private Resources resources;
	private SoundManager sm;
	
	private boolean initialized = false;
	
	public boolean getInitialized()
	{
		return initialized;
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
	
	public Level(@Attribute(name = "levelName") String levelName)
	{
		this.levelName = levelName;
		
		if (levelObjectList == null)
			levelObjectList = new ArrayList<LevelObject>();
	}
	
	public void onInitialize(Resources resources, int width, int height, SoundManager sm)
	{
		this.height = height;
		this.width = width;
		this.resources = resources;
		this.sm = sm;
		
		start();
	}
	
	private void start()
	{
		// thread = new Thread(this);
		// thread.start();
		run();
	}
	
	public void run()
	{
		levelSpriteList = new ArrayList<Sprite>();
		
		// in an attempt to make loading faster
		Sprite BIGVine = LoadedResources.getBIGVine();
		Sprite LITtleVine = LoadedResources.getLITtleVine();
		Sprite SANdHole = LoadedResources.getSANdHole();
		Sprite SMAllTree = LoadedResources.getSMAllTree();
		Sprite TREe = LoadedResources.getTREe();
		Sprite WEEd = LoadedResources.getWEEd();
		Sprite DEAdTree = LoadedResources.getDEAdTree();
		
		for (Iterator<LevelObject> it = levelObjectList.iterator(); it.hasNext();)
		{
			LevelObject tempLevelObject = it.next();
			
			if (!tempLevelObject.getOrientation())
			{
				tempLevelObject.setyLoc(height - tempLevelObject.getyLoc());
			}
			
			// hard coded
			// its a little dumb, but thats how it goes
			if (tempLevelObject.getName().equalsIgnoreCase("green"))
			{
				Sprite temp = new Sprite();
				// will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.green,
				// Sprite.class);
				temp.onInitialize(LoadedResources.getGreen(resources), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("red"))
			{
				Sprite temp = new Sprite();
				// will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.red,
				// Sprite.class);
				temp.onInitialize(LoadedResources.getRed(resources), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("blue"))
			{
				Sprite temp = new Sprite();
				// will be changed to
				// XMLHandler.readSerialFile(getResources(), R.raw.blue,
				// Sprite.class);
				temp.onInitialize(LoadedResources.getBlue(resources), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("star"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.star, Sprite.class);
				temp.onInitialize(LoadedResources.getStar(resources), sm, tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - 24, 25, 24);
				temp.setCollectable(CollectableStates.collectable);
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("bigbuilding"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getBigBuilding(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("smallbuilding"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getSmallBuilding(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("sandflat"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getSandFlat(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("bigvine"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getBigVine(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - LoadedResources.getBigVine().getHeight());
				temp.getPhysRect().clear();
				for (Iterator<physRect> iter = BIGVine.getPhysRect().iterator(); iter.hasNext();)
				{
					physRect physRectTemp = iter.next();
					Rect collRect = physRectTemp.getCollRect();
					temp.getPhysRect().add(
							new physRect(collRect.top + (int) temp.getyPos(), collRect.bottom + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.left + (int) temp.getxPos(),
									physRectTemp.getHurts()));
				}
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("grass"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.grass, Sprite.class);
				temp.onInitialize(LoadedResources.getGrass(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("littlevine"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getLittleVine(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - LoadedResources.getLittleVine().getHeight());
				temp.getPhysRect().clear();
				for (Iterator<physRect> iter = LITtleVine.getPhysRect().iterator(); iter.hasNext();)
				{
					physRect physRectTemp = iter.next();
					Rect collRect = physRectTemp.getCollRect();
					temp.getPhysRect().add(
							new physRect(collRect.top + (int) temp.getyPos(), collRect.bottom + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.left + (int) temp.getxPos(),
									physRectTemp.getHurts()));
				}
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("sandhole"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getSandHole(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				temp.getPhysRect().clear();
				for (Iterator<physRect> iter = SANdHole.getPhysRect().iterator(); iter.hasNext();)
				{
					physRect physRectTemp = iter.next();
					Rect collRect = physRectTemp.getCollRect();
					temp.getPhysRect().add(
							new physRect(collRect.top + (int) temp.getyPos(), collRect.bottom + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.left + (int) temp.getxPos(),
									physRectTemp.getHurts()));
				}
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("sandholeleft"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getSandHoleLeft(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("sandholeright"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getSandHoleRight(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("smalltree"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getSmallTree(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - SMAllTree.getPhysRect().get(0).getCollRect().top);
				temp.getPhysRect().clear();
				for (Iterator<physRect> iter = SMAllTree.getPhysRect().iterator(); iter.hasNext();)
				{
					physRect physRectTemp = iter.next();
					Rect collRect = physRectTemp.getCollRect();
					temp.getPhysRect().add(
							new physRect(collRect.top + (int) temp.getyPos(), collRect.bottom + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.left + (int) temp.getxPos(),
									physRectTemp.getHurts()));
				}
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("tree"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getTree(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - TREe.getPhysRect().get(0).getCollRect().top);
				temp.getPhysRect().clear();
				for (Iterator<physRect> iter = TREe.getPhysRect().iterator(); iter.hasNext();)
				{
					physRect physRectTemp = iter.next();
					Rect collRect = physRectTemp.getCollRect();
					temp.getPhysRect().add(
							new physRect(collRect.top + (int) temp.getyPos(), collRect.bottom + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.left + (int) temp.getxPos(),
									physRectTemp.getHurts()));
				}
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("weed"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getWeed(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - LoadedResources.getWeed().getHeight());
				temp.getPhysRect().clear();
				for (Iterator<physRect> iter = WEEd.getPhysRect().iterator(); iter.hasNext();)
				{
					physRect physRectTemp = iter.next();
					Rect collRect = physRectTemp.getCollRect();
					temp.getPhysRect().add(
							new physRect(collRect.top + (int) temp.getyPos(), collRect.bottom + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.left + (int) temp.getxPos(),
									physRectTemp.getHurts()));
				}
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("level3ground"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.level3ground, Sprite.class);
				temp.onInitialize(LoadedResources.getLevel3Ground(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("deadtree"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getDeadTree(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				temp.getPhysRect().clear();
				for (Iterator<physRect> iter = DEAdTree.getPhysRect().iterator(); iter.hasNext();)
				{
					physRect physRectTemp = iter.next();
					Rect collRect = physRectTemp.getCollRect();
					temp.getPhysRect().add(
							new physRect(collRect.top + (int) temp.getyPos(), collRect.bottom + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.left + (int) temp.getxPos(),
									physRectTemp.getHurts()));
				}
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("black"))
			{
				Sprite temp = new Sprite();
				temp.onInitialize(LoadedResources.getBlack(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc());
				levelSpriteList.add(temp);
			}
			
			else if (tempLevelObject.getName().equalsIgnoreCase("foxtwo"))
			{
				Sprite temp = XMLHandler.readSerialFile(resources, R.raw.foxmain, Sprite.class);
				temp.onInitialize(LoadedResources.getFoxTwo(), tempLevelObject.getxLoc(), tempLevelObject.getyLoc() - 54, 82, 54);
				temp.setAnimation(CharStates.Sitting);
				temp.setCollectable(CollectableStates.collectable);
				levelSpriteList.add(temp);
			}
		}
		
		initialized = true;
	}
}
