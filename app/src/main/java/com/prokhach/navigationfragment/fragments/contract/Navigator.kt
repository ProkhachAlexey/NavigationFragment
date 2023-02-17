package com.prokhach.navigationfragment.fragments.contract

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.prokhach.navigationfragment.model.Options

typealias ResultListener<T> = (T) -> Unit

// для обеспечения доступа любого из фрагментов к интерфесу Navigator
fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun showBoxSelectionScreen(options: Options)

    fun showOptionsScreen(options: Options)

    fun showAboutScreen()

    fun showConfigurationsScreen()

    fun goBack()

    fun goToMenu()

    fun <T : Parcelable> publishResult(result: T)

    fun <T : Parcelable> listenResult(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    )
}