package com.kobaj.runfoxrun;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite
{
	private Bitmap img;
	
	private int width;
	private int height;
	
	private float xPos = 0;
	private float yPos = 0;
	private Rect sRectangle;
	private Rect dest;
	
	@ElementList
	private ArrayList<physRect> physRectList;
	
	// animation
	// NOT zero based index. 1 based.
	private int currentFrame;
	private long frameTimer;
	private boolean playing = true;
	
	@ElementList
	private ArrayList<Animation> animationList;
	private Animation currentSetAnimation;
	
	private boolean initalized = false;
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public ArrayList<physRect> getPhysRect()
	{	
		return physRectList;
	}
	
	public float getxPos()
	{
		return xPos;
	}
	
	public void setxPos(float x)
	{
		xPos = x;
		updatesRect();
	}
	
	public float getyPos()
	{
		return yPos;
	}
	
	public void setyPos(float y)
	{
		yPos = y;
		updatesRect();
	}
	
	private void updatesRect()
	{
		dest.top = (int)yPos;
		dest.bottom = (int)(yPos + height);
		dest.left = (int)xPos;
		dest.right = (int)(xPos + width);
		
		for(int i = 0; i < physRectList.size(); i++)
		{
			physRectList.get(i).updatesRect(yPos, xPos + width, yPos + height, xPos);
		}
	}
	
	private void updateoRect()
	{
		sRectangle.top = currentSetAnimation.getyStartPos();
		sRectangle.bottom = sRectangle.top + height;
		sRectangle.left = ((currentFrame - 1) * width) + currentSetAnimation.getxStartPos();
		sRectangle.right = sRectangle.left + width;	
	}
	
	public void onInitalize(Resources resources, int identity, int xPos, int yPos, int width, int height)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		
		currentFrame = 1;
		frameTimer = 0;
		
		// load das image
		img = BitmapFactory.decodeResource(resources, identity);
		
		if(this.width == -1)
		{
			this.width = img.getWidth();
			this.height = img.getHeight();
		}
		
		if (animationList == null || animationList.isEmpty())
		{
			animationList = new ArrayList<Animation>();
			animationList.add(new Animation(0, "stopped", 0, 0, 1, 0));
		}
		
		setAnimation(CharStates.Stopped);
		
		dest = new Rect(xPos, yPos, xPos + this.width, yPos + this.height);
		sRectangle = new Rect(((currentFrame - 1) * this.width) + currentSetAnimation.getxStartPos(), currentSetAnimation.getyStartPos(), ((currentFrame - 1) * this.width) + currentSetAnimation.getxStartPos() + this.width, currentSetAnimation.getyStartPos() + this.height);
		
		if(physRectList == null || physRectList.isEmpty())
		{
			physRectList = new ArrayList<physRect>();
			physRectList.add(new physRect(new Rect(dest.top, dest.right, dest.bottom, dest.left), false));
		}
		
		initalized = true;
	}
	
	public void onInitalize(Resources resources, int identity, int xPos, int yPos)
	{
		onInitalize(resources, identity, xPos, yPos, -1, -1);
	}
	
	public void onInitalize(Resources resources, int identity)
	{
		onInitalize(resources, identity, 0, 0, -1, -1);
	}
	
	public void onUpdate(float f)
	{
		// tumbling ifs!
		if (initalized && playing && currentSetAnimation.getRecFPS() != 0)
		{
			if (frameTimer < currentSetAnimation.getRecMS())
			{
				frameTimer += f;
			}
			else
			{
				frameTimer = 0;
				
				currentFrame += 1;
				
				if (currentFrame > currentSetAnimation.getFrameCount())
					currentFrame = 1;
				
				updateoRect();
			}
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		if (initalized)
			canvas.drawBitmap(img, sRectangle, dest, null);
		
		return;
	}
	
	public void setAnimation(CharStates state)
	{
		if (animationList.size() != 1)
		{	
			for (int i = 0; i < animationList.size(); i++)
			{
				if (animationList.get(i).getName().equalsIgnoreCase(state.name()))
				{
					currentSetAnimation = animationList.get(i);
					
					frameTimer = 0;
					currentFrame = 1;
					
					//probably a poor way of handeling this
					if(initalized)
						updateoRect();
				}
			}
		}
		else
			currentSetAnimation = animationList.get(0);
	}
	
	public void pauseAnimation()
	{
		playing = false;
	}
	
	public void startAnimation()
	{
		playing = true;
	}
	
	//teeeeechnically not needed.
	public void onCleanup()
	{
		img.recycle();
		img = null;
		
		animationList.clear();
	}
}
