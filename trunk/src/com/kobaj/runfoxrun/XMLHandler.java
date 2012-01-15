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
	public static <T> boolean writeSerialFile(T writeable, String fileName)
	{
		if (hasStorage(true))
		{
			
			// write
			Serializer serial = new Persister();
			
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/rfr");
			dir.mkdirs();
			
			File sdcardFile = new File(dir, fileName + ".xml");
			try
			{
				if (sdcardFile.exists())
					sdcardFile.delete();
				
				sdcardFile.createNewFile();
			}
			catch (IOException e)
			{
				Log.e("JAKOBERR", e.toString());
			}
			
			try
			{
				serial.write(writeable, sdcardFile);
				return true;
			}
			catch (Exception e)
			{
				// There is the possibility of error for a number of reasons.
				// Handle
				// this appropriately in your code
				Log.e("JAKOBERR", e.toString());
			}
		}
		
		return false;
	}
	
	private static boolean hasStorage(boolean requireWriteAccess)
	{
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			return true;
		}
		else if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
		{
			return true;
		}
		return false;
	}
	
	// TODO implement.
	public static <T> T readSerialFile(String fileName, Class<? extends T> type)
	{
		if (hasStorage(false))
		{
			
		}
		
		return null;
	}
	
	public static <T> T readSerialFile(Resources resources, int identity, Class<? extends T> type)
	{
		T finalReturn = null;
		Serializer serial = new Persister();
		
		try
		{
			byte[] temp = ioStremtoByteArray(resources.openRawResource(identity));
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
	
	public static byte[] ioStremtoByteArray(InputStream is)
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