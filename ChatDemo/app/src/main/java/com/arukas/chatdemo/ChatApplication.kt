package com.arukas.chatdemo

import android.content.Intent
import com.arukas.authen.login.LoginActivity
import com.arukas.base.core.BaseApplication
import com.arukas.chat.main.MainChatActivity
import com.arukas.network.cloud.FireStoreManager
import com.arukas.network.realm.RealmManager
import com.arukas.network.service.FireStoreObserverService
import io.paperdb.Paper
import io.realm.Realm

class ChatApplication : BaseApplication() {

    override fun createApp() {
        FireStoreManager.initial()
        Paper.init(this)
        Realm.init(this)
        RealmManager.initial(this)
    }

    override fun onLogout() {
        val serviceIntent=Intent(this,FireStoreObserverService::class.java)
        stopService(serviceIntent)

        val loginIntent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(loginIntent)
    }

    override fun onLogin() {
        startService(FireStoreObserverService.createIntent(this, "company_a"))

        val mainChatIntent = Intent(this, MainChatActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(mainChatIntent)
    }
}