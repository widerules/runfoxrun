package com.kobaj.runfoxrun;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

public class custString
{
	private StringBuilder string;
	protected Path stringPath;
	
	protected int x;
	protected int y;
	
	private Rect rect;
	private int mainColor = Color.WHITE;
	private int outlineColor = Color.BLACK;
	
	private int size = 24;
	
	protected Paint textPaint = new Paint();
	private Paint strokePaint = new Paint();
	
	private Matrix rotationMatrix;
	
	public float measureit()
	{
		return strokePaint.measureText(string.toString());
	}
	
	public custString(Resources resources, String string, int x, int y)
	{
		float scale = SurfacePanel.scale;
		
		size = (int) (16 * scale);
		
		this.string = new StringBuilder();
		this.string.append(string);
		
		this.x = x;
		this.y = y;
		
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
		
		stringPath = new Path();
		rect = new Rect();
		
		generateMembers();
		
		rotationMatrix = new Matrix();
	}
	
	private void generateMembers()
	{
		char[] temp = new char[string.length()];
		string.getChars(0, string.length(), temp, 0);
		strokePaint.getTextBounds(temp, 0, temp.length, rect);
		textPaint.getTextPath(temp, 0, temp.length, x, y, stringPath);
		
		int width = rect.width();
		int height = rect.height();
		
		rect.left = x;
		rect.bottom = y + 4; // close enough
		rect.top = y - height + 4;
		rect.right = x + width;
	}
	
	public void onDraw(Canvas canvas)
	{
		canvas.drawPath(stringPath, strokePaint);
		canvas.drawPath(stringPath, textPaint);
	}
	
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
		generateMembers();
	}
	
	/*public void concat(String string)
	{
		this.string.append(string);
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
	
	/*public void setString(String string)
	{
		this.string.delete(0, this.string.length());
		this.string.append(string);
		generateMembers();
	}
	
	public String getString()
	{
		return string.toString();
	}*/
	
	public boolean fingertap(int x, int y)
	{
		float[] src = { x, y };
		float[] dst = new float[2];
		
		rotationMatrix.mapPoints(dst, src);
		
		if (rect.contains((int) dst[0], (int) dst[1]))
			return true;
		
		return false;
	}
	
	public void setRotation(float i, int xp, int yp)
	{
		rotationMatrix.setRotate(i, xp, yp);
	}
	
}
