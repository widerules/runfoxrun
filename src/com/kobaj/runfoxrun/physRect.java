package com.kobaj.runfoxrun;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import android.graphics.Rect;

public class physRect
{
	@Element
	private boolean hurts;
	
	@SuppressWarnings("unused")
	@Attribute
	private int top = 0;
	@SuppressWarnings("unused")
	@Attribute
	private int right = 0;
	@SuppressWarnings("unused")
	@Attribute
	private int bottom = 0;
	@SuppressWarnings("unused")
	@Attribute
	private int left = 0;
	
	private Rect collRect;
	
	public physRect(@Attribute(name = "top") int top, @Attribute(name = "bottom") int bottom, @Attribute(name = "right") int right, @Attribute(name = "left") int left,
			@Element(name = "hurts") boolean hurts)
	{
		this.hurts = hurts;
		
		collRect = new Rect((int) (left / 1.5f * SurfacePanel.scale), (int) (top/ 1.5f * SurfacePanel.scale), (int) (right / 1.5f * SurfacePanel.scale), (int) (bottom / 1.5f * SurfacePanel.scale));
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
	
	public void shiftRect(int x, int y)
	{
		collRect.top -= y;
		collRect.bottom -= y;
		collRect.left -= x;
		collRect.right -= x;
	}
	
	// top, right, bottom, left
	public void setRect(float top, float right, float bottom, float left)
	{
		// left, top, right, bottom;
		collRect.set((int) left, (int) top, (int) right, (int) bottom);
	}
}
