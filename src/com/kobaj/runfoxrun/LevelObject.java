package com.kobaj.runfoxrun;

import org.simpleframework.xml.Attribute;

public class LevelObject
{
	@Element
	private boolean collectable;
	
	@Element
	private boolean top;
	
	@Attribute
	private String name;
	
	@Attribute
	private int xLoc;
	
	@Attribute
	private int yLoc;
	
	public LevelObject(@Attribute (name = "name") String name, 
					   @Attribute (name = "xLoc") int x,
					   @Attribute (name = "yLoc") int y)
	{
		this.name = name;
		this.xLoc = x;
		this.yLoc = y;
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
	
	public boolean getOrientation()
	{
		return top;
	}
	
	public boolean getCollectable()
	{
		return collectable;	
	}
}
