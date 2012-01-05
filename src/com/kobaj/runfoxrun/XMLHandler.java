package com.kobaj.runfoxrun;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Environment;
import android.util.Log;

public class XMLHandler
{
	private final static String TAG = Sprite.class.getCanonicalName();
	
	// TODO actually implement this.
	// DO NOT USE YET
	public void writeSave(Animation animate)
	{
		// write
		Serializer serial = new Persister();
		
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + "/rfr");
		if (dir.mkdirs())
		{
			
		}
		else
		{
			
		}
		
		File sdcardFile = new File(dir, "levelout.xml");
		try
		{
			if (sdcardFile.exists())
				sdcardFile.delete();
			
			sdcardFile.createNewFile();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			Log.i("JAKOBERR", e.toString());
		}
		
		try
		{
			serial.write(animate, sdcardFile);
		}
		catch (Exception e)
		{
			// There is the possibility of error for a number of reasons. Handle
			// this appropriately in your code
			Log.i("JAKOBERR", e.toString());
		}
		Log.i(TAG, "XML Written to File: " + sdcardFile.getAbsolutePath());
	}
	
	public <T> T readSerialFile(Resources resources, int identity, Class<? extends T> type)
	{
		T finalReturn = null;
		Serializer serial = new Persister();
		
		try
		{
			byte[] temp = new XMLHandler().ioStremtoByteArray(resources.openRawResource(identity));
			String temp2 = new String(temp, "UTF-8");
			finalReturn = serial.read(type, temp2);
		}
		catch (NotFoundException e)
		{
			Log.e("JAKOBERR", e.toString());
		}
		catch (Exception e)
		{
			Log.e("JAKOBERR", e.toString());
		}
		
		return finalReturn;
	}
	
	public byte[] ioStremtoByteArray(InputStream is)
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		int nRead;
		byte[] data = new byte[16384];
		
		try
		{
			while ((nRead = is.read(data, 0, data.length)) != -1)
			{
				buffer.write(data, 0, nRead);
			}
		}
		catch (IOException e)
		{
			Log.e("JAKOBERR", e.toString());
		}
		
		try
		{
			buffer.flush();
		}
		catch (IOException e)
		{
			Log.e("JAKOBERR", e.toString());
		}
		
		return buffer.toByteArray();
	}	
}
