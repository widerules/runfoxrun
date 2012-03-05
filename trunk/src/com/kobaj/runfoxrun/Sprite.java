package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;

import org.simpleframework.xml.ElementList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sprite
{
	private boolean meSelected = false;
	
	private Bitmap img;
	
	private int width = -1;
	private int height = -1;
	
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
	
	private boolean initialized = false;
	
	private CollectableStates collectable;
	
	private Paint myPaint = new Paint();
	
	public String name;
	
	// wish I had extended my sprite classes correctly...too late now
	private SoundManager sm;
	
	public void setSelected(boolean value)
	{
		meSelected = value;
	}
	
	public boolean getSelected()
	{
		return meSelected;
	}
	
	public void setMMandSM(SoundManager SM)
	{
		this.sm = SM;
	}
	
	public void Release()
	{
		this.onCleanup();
	}
	
	public CollectableStates getCollectable()
	{
		return collectable;
	}
	
	public void setCollectable(CollectableStates collect)
	{
		if (collect == CollectableStates.collected)
		{
			if (sm != null)
				sm.playSound(2, 1, 0);
			
			collectable = CollectableStates.collected;
		}
		else
			this.collectable = CollectableStates.collectable;
	}
	
	public boolean getInitialized()
	{
		return initialized;
	}
	
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
		int deltaY = dest.top - (int) yPos;
		int deltaX = dest.left - (int) xPos;
		
		dest.top = (int) yPos;
		dest.bottom = (int) (yPos + height);
		dest.left = (int) xPos;
		dest.right = (int) (xPos + width);
		
		for (int i = 0; i < physRectList.size(); i++)
		{
			physRectList.get(i).shiftRect((int) deltaX, (int) deltaY);
		}
	}
	
	private void updateoRect()
	{
		sRectangle.top = currentSetAnimation.getyStartPos();
		sRectangle.bottom = sRectangle.top + height;
		sRectangle.left = ((currentFrame - 1) * width) + currentSetAnimation.getxStartPos();
		sRectangle.right = sRectangle.left + width;
	}
	
	public void reInitialize(Bitmap img, int xPos, int yPos)
	{
		if (!initialized)
		{
			onInitialize(img, xPos, yPos, -1, -1);
			return;
		}
		
		this.img = img;
		this.width = img.getWidth();
		this.height = img.getHeight();
		
		this.xPos = xPos;
		this.yPos = yPos;
		
		int deltaY = dest.top - (int) yPos;
		int deltaX = dest.left - (int) xPos;
		
		dest.top = (int) yPos;
		dest.bottom = (int) (yPos + height);
		dest.left = (int) xPos;
		dest.right = (int) (xPos + width);
		
		for (int i = 0; i < physRectList.size(); i++)
		{
			physRectList.get(i).shiftRect((int) deltaX, (int) deltaY);
			physRectList.get(i).setRect(physRectList.get(i).getCollRect().top, physRectList.get(i).getCollRect().left + width, physRectList.get(i).getCollRect().top + height,
					physRectList.get(i).getCollRect().left);
		}
		
		updateoRect();
	}
	
	public void onInitialize(Bitmap img, int xPos, int yPos, int width, int height)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		
		currentFrame = 1;
		frameTimer = 0;
		
		// load das image
		this.img = img;
		
		if (width == -1)
		{
			this.width = img.getWidth();
			this.height = img.getHeight();
		}
		
		if (animationList == null || animationList.isEmpty())
		{
			animationList = new ArrayList<Animation>();
			animationList.add(new Animation(0, "stopped"));
		}
		
		setAnimation(CharStates.Sitting);
		
		dest = new Rect(xPos, yPos, xPos + this.width, yPos + this.height);
		sRectangle = new Rect(((currentFrame - 1) * this.width) + currentSetAnimation.getxStartPos(), currentSetAnimation.getyStartPos(), ((currentFrame - 1) * this.width)
				+ currentSetAnimation.getxStartPos() + this.width, currentSetAnimation.getyStartPos() + this.height);
		
		if (physRectList == null || physRectList.isEmpty())
		{
			physRectList = new ArrayList<physRect>();
			physRectList.add(new physRect(new Rect(dest.left, dest.top, dest.right, dest.bottom), false));
		}
		else
			for (Iterator<physRect> it = physRectList.iterator(); it.hasNext();)
			{
				physRect rect = it.next();
				
				rect.shiftRect(-xPos, -yPos);
			}
		
		initialized = true;
	}
	
	public void onInitialize(Bitmap img, int xPos, int yPos)
	{
		onInitialize(img, xPos, yPos, -1, -1);
	}
	
	public void onInitialize(Bitmap img)
	{
		onInitialize(img, 0, 0, -1, -1);
	}
	
	public void onInitialize(Bitmap img, SoundManager sm, int xPos, int yPos, int width, int height)
	{
		this.sm = sm;
		onInitialize(img, xPos, yPos, width, height);
	}
	
	public void onInitialize(Resources resources, SoundManager sm, int identity, int xPos, int yPos, int width, int height)
	{
		this.sm = sm;
		Bitmap temp = BitmapFactory.decodeResource(resources, identity);
		onInitialize(temp, xPos, yPos, width, height);
	}
	
	public void onInitialize(Resources resources, int identity, int xPos, int yPos, int width, int height)
	{
		onInitialize(resources, null, identity, xPos, yPos, width, height);
	}
	
	public void onInitialize(Resources resources, int identity, int xPos, int yPos)
	{
		onInitialize(resources, identity, xPos, yPos, -1, -1);
	}
	
	public void onInitialize(Resources resources, int identity)
	{
		onInitialize(resources, identity, 0, 0, -1, -1);
	}
	
	public int getCurAnimation()
	{
		return this.currentSetAnimation.getId();
	}
	
	public void onUpdate(float delta)
	{
		// tumbling ifs!
		if (initialized && playing && currentSetAnimation.getRecFPS() != 0)
		{
			if (frameTimer < currentSetAnimation.getRecMS())
			{
				frameTimer += delta;
			}
			else
			{
				frameTimer = 0;
				
				currentFrame += 1;
				
				if (this.currentSetAnimation.getId() == CharStates.Running.ordinal())
				{
					if (currentFrame == 1 || currentFrame == 3)
					{
						if (sm != null)
							sm.playSound(1, .75f); // footsteps
					}
				}
				
				if (currentFrame > currentSetAnimation.getFrameCount())
				{
					if (currentSetAnimation.getId() == CharStates.Jump.ordinal())
					{
						this.setAnimation(CharStates.Fallingup);
					}
					else if (currentSetAnimation.getId() == CharStates.GoingDown.ordinal())
					{
						this.setAnimation(CharStates.Running);
						if (sm != null)
							sm.playSound(5, .75f); // landing
					}
					else if (currentSetAnimation.getId() == CharStates.Collapse.ordinal())
					{
						this.setAnimation(CharStates.Collapsed);
					}
					
					currentFrame = 1;
				}
				
				updateoRect();
			}
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		if (initialized)
			if (img != null)
				canvas.drawBitmap(img, sRectangle, dest, myPaint);
	}
	
	public void onDraw(Canvas canvas, int scrollingPositionX, int scrollingPositionY)
	{
		Rect rect1 = new Rect(scrollingPositionX, scrollingPositionY, scrollingPositionX + canvas.getWidth(), scrollingPositionY + canvas.getHeight());
		Rect rect2 = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		if (initialized)
			canvas.drawBitmap(img, rect1, rect2, myPaint);
	}
	
	public void setAnimation(CharStates state)
	{
		if (animationList.size() != 1)
		{
			for (int i = 0; i < animationList.size(); i++)
			{
				if (animationList.get(i).getId() == state.ordinal())
				{
					currentSetAnimation = animationList.get(i);
					
					frameTimer = 0;
					currentFrame = 1;
					
					// probably a poor way of handeling this
					if (initialized)
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
	
	// teeeeechnically not needed.
	public void onCleanup()
	{
		if (img != null)
			img.recycle();
		
		img = null;
		
		animationList.clear();
	}
	
	public void setPaintColorFilter(int alpha)
	{
		myPaint.setAlpha(alpha);
	}
}
