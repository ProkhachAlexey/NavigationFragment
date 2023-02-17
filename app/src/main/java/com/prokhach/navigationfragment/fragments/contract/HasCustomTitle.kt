package com.prokhach.navigationfragment.fragments.contract

import androidx.annotation.StringRes

interface HasCustomTitle {

    @StringRes
    fun getTitleRes(): Int
}
