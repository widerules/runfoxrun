//thanks to http://mobile.dzone.com/articles/beginning-android-game
// http://www.androidsnippets.com/stop-screen-from-dimming-by-enforcing-wake-lock
// http://sourceforge.net/projects/simple/
// http://stackoverflow.com/questions/1264709/convert-inputstream-to-byte-in-java
// http://www.zdnet.com/blog/burnette/how-to-use-multi-touch-in-android-2-part-3-understanding-touch-events/1775?tag=content;siu-container
// http://www.droidnova.com/creating-sound-effects-in-android-part-1,570.html
// http://blog.elsdoerfer.name/2010/04/15/android-check-if-sd-card-storage-is-available/
// http://stackoverflow.com/questions/6926644/android-color-to-int-conversion
// http://www.codeproject.com/KB/recipes/simple_interpolation.aspx
// http://www.droidbase.de/?p=103
// http://www.superflashbros.net/as3sfxr/
// http://bytes.com/topic/java/answers/645327-how-add-leading-zeros-string
// http://stackoverflow.com/questions/5196186/split-int-value-into-seperate-digits

package com.kobaj.runfoxrun;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.MotionEvent;

//initialize game and surface class
public class RunfoxrunActivity extends Activity
{
	/** Called when the activity is first created. */
	
	private PowerManager.WakeLock wl;
	private SurfacePanel game;
	
	//saving state
	public static SharedPreferences mPrefs;
	public static SharedPreferences.Editor ed;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mPrefs = getSharedPreferences("com.kobaj.runfoxrun_prefs", 0);
		ed = mPrefs.edit();
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		game = new SurfacePanel(this);
		game.onInitialize();
		
		// last
		setContentView(game);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		wl.release();
		
		game.onScreenPause(ed);
		ed.commit();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		wl.acquire();
		
		game.onScreenResume(mPrefs);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		game.onScreenQuit(ed);
		ed.commit();
	}
	
	@Override
	public boolean onKeyDown(int i, KeyEvent event)
	{
		if (i == KeyEvent.KEYCODE_BACK || i == KeyEvent.KEYCODE_HOME || i == KeyEvent.KEYCODE_SEARCH || i == KeyEvent.KEYCODE_MENU)
			game.onUserPause();
		
		if (i == KeyEvent.KEYCODE_VOLUME_DOWN || i == KeyEvent.KEYCODE_VOLUME_UP)
			return false;
		
		game.im.eventUpdateDown(i, event);
		return true;
	}
	
	@Override
	public boolean onKeyUp(int i, KeyEvent event)
	{
		game.im.eventUpdateUp(i, event);
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		game.im.eventUpdate(e);
		return true;
	}
}
