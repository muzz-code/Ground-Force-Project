package com.trapezoidlimited.groundforce.utils

import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.checkItem(actionId: Int) {
    for (i in 0 until menu.size()) {
        val item: MenuItem = menu.getItem(i)
        item.isChecked = false
    }

    menu.findItem(actionId)?.isChecked = true
}