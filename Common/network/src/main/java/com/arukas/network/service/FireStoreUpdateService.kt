package com.arukas.network.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class FireStoreUpdateService: Service() {
    private val timer=Timer()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }
}