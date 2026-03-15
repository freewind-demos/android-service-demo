package demos

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 后台任务
        return START_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
    }
}
