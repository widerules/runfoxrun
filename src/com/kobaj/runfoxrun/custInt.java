package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.res.Resources;

public class custInt extends custString
{
	private int zeroCount;
	
	private List<Integer> digits = new ArrayList<Integer>();
	private char[] result;
	
	public custInt(Resources resources, int integer, int zeroCount, int x, int y)
	{
		super(resources, "0000000000000", x, y);
		
		this.zeroCount = zeroCount;
		
		for(int i = 0; i < zeroCount; i++)
			digits.add(0);
		
		result = new char[zeroCount];
		
		setInt(integer);
	}
	
	public custInt(Resources resources, int integer, int x, int y)
	{
		this(resources, integer, 13, x, y);
	}
	
	public void setInt(int i)
	{
		textPaint.getTextPath(listtochar(digits(i), zeroCount), 0, zeroCount, x, y, stringPath);
	}
	

	private List<Integer> digits(int i) 
	{
			for(int e = digits.size() - 1; e >=0; e--)
			{
				digits.set(e, 0);
			}
	    
		
		int e = 0;
	    while(i > 0) 
	    {
	    	digits.set(e,i % 10);
	        i /= 10;
	        e++;
	    }
	    
	    Collections.reverse(digits);
	    
	    return digits;
	}
	
	private char[] listtochar(List<Integer> i, int maximum)
	{	
		int max = i.size();
		if(maximum - max < 0)
			return null;

		int j = 0;
		for(int e: i)
		{
			if (e >= 0 && e <= 9) 
			{
				result[j] = Character.forDigit(e, 10);
			}
			j++;
		}
		
		while(j < maximum)
		{
			result[j] = 0;
			j++;
		}
		
		return result;
	}	
}
