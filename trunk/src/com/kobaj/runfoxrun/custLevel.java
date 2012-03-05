package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;

import org.simpleframework.xml.Attribute;

public class custLevel extends Level
{	
	public void setObjectList(ArrayList<Sprite> levelList)
	{	
		for(Iterator<Sprite> it = levelList.iterator(); it.hasNext();)
		{
			Sprite temp = it.next();
			
			LevelObject lotemp = new LevelObject(temp.name, (int)temp.getxPos(), (int)temp.getyPos());
			
			levelObjectList.add(lotemp);
		}
	}
	
	public custLevel(@Attribute(name = "levelName") String levelName)
	{
		super(levelName);
	}
}
