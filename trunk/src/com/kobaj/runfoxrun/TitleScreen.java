package com.kobaj.runfoxrun;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class TitleScreen
{
	private Bitmap imgBackdrop;
	
	private custString newGame;
	private custString contGame;
	private custString continousGame;
	private custString highScores;
	
	public void onInitialize(Resources resources, int identity)
	{
		imgBackdrop = BitmapFactory.decodeResource(resources, identity);
		
		newGame = new custString("New Game", 100, 100);
		newGame.setSize(35);
		
		contGame = new custString("Continue Game", 100, 130);
		contGame.setColor(Color.GRAY, Color.BLACK);
		
		continousGame = new custString("Continous Game", 100, 160);
		continousGame.setColor(Color.GRAY, Color.BLACK);
		
		highScores = new custString("High Scores", 100, 190);
		highScores.setColor(Color.GRAY, Color.BLACK);
	}
	
	public GameStates onTouch(int x, int y)
	{
		GameStates state = GameStates.TitleScreen;
		
		if(newGame.fingertap(x, y))
			state = GameStates.SinglePlay;
		else if (contGame.fingertap(x, y))
			state = GameStates.ContSinglePlay;
		else if (continousGame.fingertap(x, y))
			state = GameStates.Continous;
		else if (highScores.fingertap(x, y))
			state = GameStates.HighScore;
		
		return state;
	}
	
	public void onDraw(Canvas canvas)
	{
		canvas.drawBitmap(imgBackdrop, 0, 0, null);
		
		newGame.onDraw(canvas);
		contGame.onDraw(canvas);
		continousGame.onDraw(canvas);
		highScores.onDraw(canvas);
	}
}
