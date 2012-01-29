package com.kobaj.runfoxrun;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Animation
{
	@Element
	private int frameCount;
	@Element
	private int recFPS;
	
	private int recMS = -1;

	private int mxStartPos;
	private int myStartPos;
	
	
	// these two technically go hand in hand.
	@Attribute
	private String name;
	@Attribute
	private int id;
	@SuppressWarnings("unused")
	@Attribute
	private int xstartpos;
	@SuppressWarnings("unused")
	@Attribute
	private int ystartpos;
	
	@SuppressWarnings("unused")
	private Animation(@Attribute(name = "id") int id,
			@Attribute(name = "name") String name,
			@Attribute(name = "xstartpos") int xstartpos,
			@Attribute(name = "ystartpos") int ystartpos)
	{
		this.id = id;
		this.name = name;
		
		this.mxStartPos = (int) (xstartpos / 1.5f * SurfacePanel.scale);
		this.myStartPos = (int) (ystartpos / 1.5f * SurfacePanel.scale);
		
		onInitialize();
	}
	
	public Animation(int id, String name)
	{
		this.id = id;
		this.name = name;
		
		this.mxStartPos = 0;
		this.myStartPos = 0;
		this.frameCount = 1;
		this.recFPS = 0;
		
		onInitialize();
	}
	
	private void onInitialize()
	{
		if(name.equalsIgnoreCase(CharStates.Running.name()))
			id = CharStates.Running.ordinal();
		if(name.equalsIgnoreCase(CharStates.Collapse.name()))
			id = CharStates.Collapse.ordinal();
		if(name.equalsIgnoreCase(CharStates.Collapsed.name()))
			id = CharStates.Collapsed.ordinal();
		if(name.equalsIgnoreCase(CharStates.Fallingup.name()))
			id = CharStates.Fallingup.ordinal();
		if(name.equalsIgnoreCase(CharStates.GoingDown.name()))
			id = CharStates.GoingDown.ordinal();
		if(name.equalsIgnoreCase(CharStates.Jump.name()))
			id = CharStates.Jump.ordinal();
		if(name.equalsIgnoreCase(CharStates.LevelOut.name()))
			id = CharStates.LevelOut.ordinal();
		if(name.equalsIgnoreCase(CharStates.Sitting.name()))
			id = CharStates.Sitting.ordinal();
		if(name.equalsIgnoreCase(CharStates.Standing.name()))
			id = CharStates.Standing.ordinal();
	}
	
	public int getxStartPos()
	{
		return mxStartPos;
	}
	
	public int getyStartPos()
	{
		return myStartPos;
	}
	
	public int getFrameCount()
	{
		return frameCount;
	}
	
	public int getRecMS()
	{
		if (recMS == -1)
		{
			recMS = (int) ((1.0f / recFPS) * 1000);
		}
		
		return recMS;
	}
	
	public int getRecFPS()
	{
		return recFPS;
	}
	
	/*public String getName()
	{
		return name;
	}*/
	
	public int getId()
	{
		return id;
	}
	
}
