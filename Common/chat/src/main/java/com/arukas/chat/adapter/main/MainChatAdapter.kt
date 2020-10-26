package com.arukas.chat.adapter.main

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.arukas.base.BasePagerAdapter

class MainChatAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle,
    fragments: SparseArray<Fragment>
) : BasePagerAdapter(fragmentManager, lifecycle, fragments) {
}