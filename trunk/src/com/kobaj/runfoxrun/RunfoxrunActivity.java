//thanks to http://mobile.dzone.com/articles/beginning-android-game
// http://www.androidsnippets.com/stop-screen-from-dimming-by-enforcing-wake-lock

package com.kobaj.runfoxrun;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;

//initialize game and surface class
public class RunfoxrunActivity extends Activity {
    /** Called when the activity is first created. */
	
	private PowerManager.WakeLock wl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

        setContentView(new SurfacePanel(this));
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
}