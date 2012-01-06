package com.kobaj.runfoxrun;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;

public class Sprite
{
	private Bitmap img;
	
	private int width;
	private int height;
	
	private int xPos = 0;
	private int yPos = 0;
	private Rect sRectangle;
	private Rect dest;
	
	// animation
	// NOT zero based index. 1 based.
	private int currentFrame;
	private long frameTimer;
	private boolean playing = true;
	
	@ElementList
	private ArrayList<Animation> animationList;
	private Animation currentSetAnimation;
	
	private boolean initalized = false;
	
	public void setxPos(int x)
	{
		xPos = x;
		updatesRect();
	}
	
	public void setyPos(int y)
	{
		yPos = y;
		updatesRect();
	}
	
	private void updatesRect()
	{
		dest.top = yPos;
		dest.bottom = yPos + height;
		dest.left = xPos;
		dest.right = xPos + width;
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
		
		if (animationList == null || animationList.isEmpty())
		{
			animationList = new ArrayList<Animation>();
			animationList.add(new Animation(0, "stopped", 0, 0, 1, 0));
		}
		
		setAnimation(CharStates.Stopped);
		
		dest = new Rect(xPos, yPos, xPos + width, yPos + height);
		sRectangle = new Rect(((currentFrame - 1) * width) + currentSetAnimation.getxStartPos(), currentSetAnimation.getyStartPos(), ((currentFrame - 1) * width) + currentSetAnimation.getxStartPos() + width, currentSetAnimation.getyStartPos() + height);
		
		initalized = true;
	}
	
	public void onInitalize(Resources resources, int identity, int xPos, int yPos)
	{
		onInitalize(resources, identity, xPos, yPos, 40, 40);
	}
	
	public void onInitalize(Resources resources, int identity)
	{
		onInitalize(resources, identity, 0, 0, 40, 40);
	}
	
	public void onUpdate(int deltaTime)
	{
		// tumbling ifs!
		if (initalized && playing && currentSetAnimation.getRecFPS() != 0)
		{
			if (frameTimer < currentSetAnimation.getRecMS())
			{
				frameTimer += deltaTime;
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
		
		Paint textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setStrokeWidth(2);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		
		//fps output
		canvas.drawText(String.valueOf(frameTimer), 100, 10, textPaint);
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
	
	public void onCleanup()
	{
		img.recycle();
		img = null;
		
		animationList.clear();
	}
}
