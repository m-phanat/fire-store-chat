package com.arukas.chatdemo.view.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arukas.base.BaseViewModel
import com.arukas.network.model.Person
import com.arukas.network.utils.UserManager

class MainActivityViewModel(application: Application) : BaseViewModel(application) {
    private val user=MutableLiveData<Person>()

    fun getUser():LiveData<Person>{
        return user
    }

    fun loadUser(){
        UserManager.getInstance().getUser()?.let {
            user.value=it
        }
    }

    fun logout() {
        UserManager.getInstance().logout()
    }
}