package com.sundaymorning.coincharge;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.PowerManager;

public class PushWakeLock {

    private static PowerManager.WakeLock sCpuWakeLock = null;
    private static Timer wakeupTimer = null;
    private static final String TAG = "smorning";
    @SuppressWarnings("deprecation")
	public static void acquireCpuWakeLock(Context context) {    	
    	try {
        	releaseCpuLock();
        	PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            sCpuWakeLock = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, TAG);
            
            if(sCpuWakeLock != null)
            	sCpuWakeLock.acquire();

            wakeupTimer();
            
		} catch (Exception e) {
			e.printStackTrace();
			releaseCpuLock();
			wakeupTimerRelease();
		}    
    }
 
    private static void releaseCpuLock() { 
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }
    
    public static void wakeupTimer() {
    	wakeupTimerRelease();
    	wakeupTimer = new Timer();
		wakeupTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				releaseCpuLock();
				wakeupTimerRelease();
			}
		}, 10000);	
	   }
    
    public static void wakeupTimerRelease() {
		if (wakeupTimer != null) {
			wakeupTimer.cancel();
			wakeupTimer = null;
		}
    }
}
