package com.kobaj.runfoxrun;

import android.graphics.Path;
import android.graphics.Rect;

public class custSingleString
{
	public StringBuilder string;
	public Path stringPath;
	
	public int y;
	
	public Rect rect;
	
	public custSingleString()
	{
		string = new StringBuilder();
		stringPath = new Path();
		rect = new Rect();
	};
}
