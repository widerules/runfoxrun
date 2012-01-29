package com.kobaj.runfoxrun;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class LevelObject
{
	@Element
	private boolean top;
	
	@Attribute
	private String name;
	
	@Attribute
	private int xLoc;
	
	@Attribute
	private int yLoc;
	
	public LevelObject(@Attribute(name = "name") String name, @Attribute(name = "xLoc") int x, @Attribute(name = "yLoc") int y)
	{
		this.name = name;
		this.xLoc = (int) (x / 1.5f * SurfacePanel.scale);
		this.yLoc = (int) (y / 1.5f * SurfacePanel.scale);
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getxLoc()
	{
		return xLoc;
	}
	
	public int getyLoc()
	{
		return yLoc;
	}
	
	public void setyLoc(int y)
	{
		yLoc = y;
	}
	
	public boolean getOrientation()
	{
		return top;
	}
}
