package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;

import org.simpleframework.xml.Attribute;

import android.util.Log;

public class custLevel extends Level
{	
	public void setObjectList(ArrayList<Sprite> levelList, int reverse)
	{	
		for(Iterator<Sprite> it = levelList.iterator(); it.hasNext();)
		{
			Sprite temp = it.next();
			
			if(temp.name != null)
			{
			
				int y = (int)temp.getyPos();
				y = reverse - y;
			
				if(temp.name.equalsIgnoreCase("tree") || temp.name.equalsIgnoreCase("smalltree"))
				{
					y =  temp.getPhysRect().get(0).getCollRect().top;
					y = reverse - y;
				}
				else if( temp.name.equalsIgnoreCase("littlevine") || temp.name.equalsIgnoreCase("bigvine"))
				{
					y = y - temp.getHeight();
				}
				else if(temp.name.equalsIgnoreCase("weed"))
				{
					y = y - temp.getHeight();
				}
			
			
				LevelObject lotemp = new LevelObject(temp.name, (int)temp.getxPos(), y);
			
				levelObjectList.add(lotemp);
			
			}
			else
				Log.e("WRITING FILE ERROR", "Could not save Item at location (" + temp.getxPos() + ", " + temp.getyPos() + ") Width: " + temp.getWidth() + " Height: " + temp.getHeight());
		}
		
		levelLength = 16000;
		playerStartx = 100;
		playerStarty = -100;
	}
	
	public custLevel(@Attribute(name = "levelName") String levelName)
	{
		super(levelName);
	}
}
