package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;

import org.simpleframework.xml.Attribute;

public class custLevel extends Level
{	
	public void setObjectList(ArrayList<Sprite> levelList, int reverse)
	{	
		for(Iterator<Sprite> it = levelList.iterator(); it.hasNext();)
		{
			Sprite temp = it.next();
			
			int y = (int)temp.getyPos();
			y = reverse - y;
			
			if(temp.name != null)
			{
				if(temp.name.equalsIgnoreCase("tree") || temp.name.equalsIgnoreCase("smalltree"))
				{
					y =  temp.getPhysRect().get(0).getCollRect().top;
					y = reverse - y;
				}
				else if( temp.name.equalsIgnoreCase("littlevine") || temp.name.equalsIgnoreCase("bigvine"))
				{
					y = temp.getPhysRect().get(0).getCollRect().top;
				}
				else if(temp.name.equalsIgnoreCase("weed"))
				{
					y = y - temp.getHeight();
				}
			}
			
			LevelObject lotemp = new LevelObject(temp.name, (int)temp.getxPos(), y);
			
			levelObjectList.add(lotemp);
		}
	}
	
	public custLevel(@Attribute(name = "levelName") String levelName)
	{
		super(levelName);
	}
}
