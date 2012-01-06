package com.kobaj.runfoxrun;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Animation
{
	@Element
	private int xStartPos;
	@Element
	private int yStartPos;
	@Element
	private int frameCount;
	@Element
	private int recFPS;
	
	private int recMS = -1;
	
	// these two technically go hand in hand.
	@Attribute
	private String name;
	@Attribute
	private int id;
	
	@SuppressWarnings("unused")
	private Animation(@Attribute(name = "id") int id, @Attribute(name = "name") String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public Animation(int id, String name, int xStartPos, int yStartPos, int frameCount, int recFPS)
	{
		this.id = id;
		this.name = name;
		
		this.xStartPos = xStartPos;
		this.yStartPos = yStartPos;
		this.frameCount = frameCount;
		this.recFPS = recFPS;
	}
	
	public int getxStartPos()
	{
		return xStartPos;
	}
	
	public int getyStartPos()
	{
		return yStartPos;
	}
	
	public int getFrameCount()
	{
		return frameCount;
	}
	
	public int getRecMS()
	{
		if(recMS == -1)
		{
			recMS = (int) ((1.0f / recFPS) * 1000);
		}
		
		return recMS;
	}
	
	public int getRecFPS()
	{
		return recFPS;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getId()
	{
		return id;
	}
	
}
