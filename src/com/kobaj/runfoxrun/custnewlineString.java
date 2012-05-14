package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;

//probably could have inherited something >.>
public class custnewlineString
{
	private custSingleString tempcust;
	
	private ArrayList<custSingleString> stringy;
	
	protected int x;
	
	private Rect bigRect;
	
	private int mainColor = Color.WHITE;
	private int outlineColor = Color.BLACK;
	
	private int size = 24;
	
	protected Paint textPaint = new Paint();
	private Paint strokePaint = new Paint();
	
	private Matrix rotationMatrix;
	
	public float measureit()
	{
		float max = 0;
		
		for (Iterator<custSingleString> it = stringy.iterator(); it.hasNext();)
		{
			tempcust = it.next();
			float mytemp = strokePaint.measureText(tempcust.string.toString());
			if (mytemp > max)
				max = mytemp;
		}
		
		return max;
	}
	
	public custnewlineString(Resources resources, String string, int x, int y, boolean reverse)
	{
		stringy = new ArrayList<custSingleString>();
		
		float scale = SurfacePanel.scale;
		
		size = (int) (16 * scale);
		
		// semi arbitrary
		textPaint.setColor(mainColor);
		// textPaint.setStrokeWidth(8);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(size);
		
		strokePaint.setColor(outlineColor);
		strokePaint.setTextAlign(Paint.Align.CENTER);
		strokePaint.setTextSize(size);
		strokePaint.setTypeface(Typeface.DEFAULT_BOLD);
		strokePaint.setStyle(Paint.Style.STROKE);
		strokePaint.setStrokeWidth(2);
		strokePaint.setAntiAlias(true);
		
		for (String line : string.split("\n"))
		{
			custSingleString temp = new custSingleString();
			temp.string.append(line);
			temp.y = y;
			y -= textPaint.ascent() - textPaint.descent() - 4;
			stringy.add(temp);
		}
		
		this.x = x;
		
		generateMembers();
		
		rotationMatrix = new Matrix();
	}
	
	public custnewlineString(Resources resources, String string, int x, int y)
	{
		this(resources, string, x, y, false);
		
		stringy = new ArrayList<custSingleString>();
		
		float scale = SurfacePanel.scale;
		
		size = (int) (16 * scale);
		
		// semi arbitrary
		textPaint.setColor(mainColor);
		// textPaint.setStrokeWidth(8);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(size);
		
		strokePaint.setColor(outlineColor);
		strokePaint.setTextAlign(Paint.Align.CENTER);
		strokePaint.setTextSize(size);
		strokePaint.setTypeface(Typeface.DEFAULT_BOLD);
		strokePaint.setStyle(Paint.Style.STROKE);
		strokePaint.setStrokeWidth(2);
		strokePaint.setAntiAlias(true);
		
		for (String line : string.split("\n"))
		{
			custSingleString temp = new custSingleString();
			temp.string.append(line);
			temp.y = y;
			y -= textPaint.ascent() - textPaint.descent() - 4;
			stringy.add(temp);
		}
		
		this.x = x;
		
		generateMembers();
		
		rotationMatrix = new Matrix();
	}
	
	private void generateMembers()
	{
		int maxwidth = 0;
		
		for (Iterator<custSingleString> it = stringy.iterator(); it.hasNext();)
		{
			tempcust = it.next();
			
			char[] temp = new char[tempcust.string.length()];
			tempcust.string.getChars(0, tempcust.string.length(), temp, 0);
			strokePaint.getTextBounds(temp, 0, temp.length, tempcust.rect);
			textPaint.getTextPath(temp, 0, temp.length, x, tempcust.y, tempcust.stringPath);
			
			int width = tempcust.rect.width();
			int height = tempcust.rect.height();
			
			tempcust.rect.left = x;
			tempcust.rect.bottom = tempcust.y + 4; // close enough
			tempcust.rect.top = tempcust.y - height - 4;
			tempcust.rect.right = x + width;
			
			if (tempcust.rect.right > maxwidth)
				maxwidth = tempcust.rect.right;
		}
		
		bigRect = new Rect(x, stringy.get(0).y - stringy.get(0).rect.height() + 4, maxwidth, stringy.get(stringy.size() - 1).y + 4);
	}
	
	public void onDraw(Canvas canvas)
	{
		for (Iterator<custSingleString> it = stringy.iterator(); it.hasNext();)
		{
			tempcust = it.next();
			canvas.drawPath(tempcust.stringPath, strokePaint);
			canvas.drawPath(tempcust.stringPath, textPaint);
		}
	}
	
	/*public void setPosition(int x, int y)
	{
		this.x = x;
		
		for (Iterator<custSingleString> it = stringy.iterator(); it.hasNext();)
		{
			tempcust = it.next();
			tempcust.y = y;
			y += textPaint.ascent() + textPaint.descent();
		}
		
		generateMembers();
	}*/
	
	public void setSize(int size)
	{
		this.size = size;
		textPaint.setTextSize(size);
		strokePaint.setTextSize(size);
		generateMembers();
	}
	
	public void setColor(int mainColor, int outlineColor)
	{
		this.mainColor = mainColor;
		this.outlineColor = outlineColor;
		
		textPaint.setColor(mainColor);
		strokePaint.setColor(outlineColor);
		
		generateMembers();
	}
	
	public boolean fingertap(int x, int y)
	{
		float[] src = { x, y };
		float[] dst = new float[2];
		
		rotationMatrix.mapPoints(dst, src);
		
		/*
		 * for(Iterator<custSingleString> it = stringy.iterator();
		 * it.hasNext();) { tempcust = it.next(); if
		 * (tempcust.rect.contains((int) dst[0], (int) dst[1])) return true; }
		 */
		
		if (bigRect.contains((int) dst[0], (int) dst[1]))
			return true;
		
		return false;
	}
	
	public void setRotation(float i, int xp, int yp)
	{
		rotationMatrix.setRotate(i, xp, yp);
	}
	
}
