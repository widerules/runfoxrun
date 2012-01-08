package com.kobaj.runfoxrun;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import android.graphics.Rect;

public class physRect
{
	@Element
	private boolean hurts;
	
	@Attribute
	private int top = 0;
	@Attribute
	private int right = 0;
	@Attribute
	private int bottom = 0;
	@Attribute
	private int left = 0;
	
	private Rect collRect;
	
	@SuppressWarnings("unused")
	private physRect(@Attribute(name = "top") int top,
			@Attribute(name = "bottom") int bottom,
			@Attribute(name = "right") int right,
			@Attribute(name = "left") int left,
			@Element(name = "hurts") boolean hurts)
	{
		/*this.top = top;
		this.right = right;
		this.left = left;
		this.bottom = bottom;*/
		this.hurts = hurts;
		
		collRect = new Rect(top, right, bottom, left);
	}
	
	public physRect(Rect rect, boolean hurts)
	{
		collRect = rect;
		this.hurts = hurts;
	}
	
	public boolean getHurts()
	{
		return hurts;
	}
	
	public Rect getCollRect()
	{
		return collRect;
	}

	//top, right, bottom, left
	public void updatesRect(int yPos, int i, int j, int xPos)
	{
		//left, top, right, bottom;
		collRect.set(xPos, yPos, i, j);
	}
}
