//thanks to http://mobile.dzone.com/articles/beginning-android-game
// http://www.androidsnippets.com/stop-screen-from-dimming-by-enforcing-wake-lock
// http://sourceforge.net/projects/simple/
// http://stackoverflow.com/questions/1264709/convert-inputstream-to-byte-in-java
// http://www.zdnet.com/blog/burnette/how-to-use-multi-touch-in-android-2-part-3-understanding-touch-events/1775?tag=content;siu-container

package com.kobaj.runfoxrun;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.MotionEvent;

//initialize game and surface class
public class RunfoxrunActivity extends Activity {
    /** Called when the activity is first created. */
	
	private PowerManager.WakeLock wl;
	private SurfacePanel game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        
        game = new SurfacePanel(this);
        setContentView(game);
    }
    
    @Override
    protected void onPause() {
            super.onPause();
            wl.release();
            
            //TODO implement pause
    }

    @Override
    protected void onResume() {
            super.onResume();
            wl.acquire();
            
            //TODO implement resume
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
    	game.im.eventUpdate(e);
    	return true;
    }
}