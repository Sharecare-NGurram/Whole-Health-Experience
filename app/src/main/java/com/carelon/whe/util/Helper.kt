package com.carelon.whe.util

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.os.SystemClock
import android.text.TextUtils
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.carelon.whe.R
import com.carelon.whe.databinding.LayoutToastBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Validate input entered in text field is valid email format or not
 */
fun EditText.isEmailValid() : Boolean {
    return (!TextUtils.isEmpty(this.text.toString().trim()) &&
            android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString().trim()).matches())
}

/**
 * Hide keyboard from window
 */
fun Activity.hideKeyBoard() {
    val view = this.currentFocus
    view?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

/**
 * To check internet is connected to the device or not
 */
fun Context.isInternetConnected() : Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNW = cm.activeNetwork ?: return false
    val activeNWLists = cm.getNetworkCapabilities(activeNW) ?: return false
    return when {
        activeNWLists.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNWLists.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNWLists.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        activeNWLists.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}

/**
* Use common toast to show quick info in success
*/
fun Activity.showSuccessToast(msg:String){
    val binding = LayoutToastBinding.inflate(LayoutInflater.from(applicationContext))
    val toastMsg = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG)
    toastMsg.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL,0,0)
    binding.txtMessage.text = msg
    toastMsg.view = binding.root
    toastMsg.show()
}

/**
 * Use common toast to show quick info in failure or error
 */
fun Activity.showFailureToast(msg:String){
    val binding = LayoutToastBinding.inflate(LayoutInflater.from(applicationContext))
    val toastMsg = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG)
    toastMsg.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL,0,0)
    binding.txtMessage.text = msg
    binding.txtMessage.background = applicationContext.getDrawable(R.drawable.drawable_toast_error_bg)
    toastMsg.view = binding.root
    toastMsg.show()
}

/**
* Make activity full screen stretch to status bar
*/
@Suppress("DEPRECATION")
fun Activity.stretchToFullScreen() {
    if (SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
    } else {
        with(window) {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility =
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}

/**
 * Throttle
 */
fun View.setSafeOnClickListener(debounceTime: Long = 500L, action: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return
            } else {
                action()
            }
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

internal inline fun <reified T : Activity> Activity.launchAndFinish(
    block: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.apply(block)
    startActivity(intent)
    finish()
    /*when (navigationDirection) {
        NavigationDirection.BACKWARD -> {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        NavigationDirection.UPWARD -> {
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
        }
        else -> {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }*/
}

internal inline fun <reified T : Activity> Activity.launch(
    block: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.apply(block)
    startActivity(intent)
    /*when (navigationDirection) {
        NavigationDirection.BACKWARD -> {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        NavigationDirection.UPWARD -> {
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
        }
        else -> {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }*/
}

internal fun Activity.closeActivity(navigationDirection: NavigationDirection) {
    this.finish()
    when (navigationDirection) {
        NavigationDirection.BACKWARD -> {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        else -> {
            overridePendingTransition(R.anim.slide_down_reverse, R.anim.slide_up_reverse)
        }
    }
}

internal fun Activity.closeAllActivity(navigationDirection: NavigationDirection) {
    this.finishAffinity()
    when (navigationDirection) {
        NavigationDirection.BACKWARD -> {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        else -> {
            overridePendingTransition(R.anim.slide_down_reverse, R.anim.slide_up_reverse)
        }
    }
}

/**
* this function is used for status bar icon text color change to dark or light
* */
fun Activity.setLightStatusBar(b: Boolean) {
    if (SDK_INT < Build.VERSION_CODES.R) {
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = b
    }
}

/**
 * Extension function to show or hide the view
 */
fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun DialogFragment.safeShow(fragmentManager: FragmentManager?, tag: String) {
    if (this.isAdded) {
        return
    }

    fragmentManager?.beginTransaction()?.add(this, tag)?.commitAllowingStateLoss()
}

fun DialogFragment.safeDismiss(fragmentManager: FragmentManager) {
    fragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
}

inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
    SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}

inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
    SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

fun AlarmManager.canScheduleAlarms(): Boolean {
    return if (SDK_INT >= Build.VERSION_CODES.S) {
        canScheduleExactAlarms()
    } else {
        true
    }
}

fun BottomNavigationView.showBadge(
    context: Context?,
    @IdRes itemId: Int,
    value: String?
) {
    removeBadge(itemId)
    val itemView = this.findViewById<BottomNavigationItemView>(itemId)
    val badge: View = LayoutInflater.from(context)
        .inflate(R.layout.view_bottom_batch, this, false)
    val text = badge.findViewById<TextView>(R.id.badge_text_view)
    text.text = value
    itemView.addView(badge)
}

fun BottomNavigationView.batchRemove( @IdRes itemId: Int) {
    val itemView = this.findViewById<BottomNavigationItemView>(itemId)
    if (itemView.childCount == 3) {
        itemView.removeViewAt(2)
    }
}

fun EditText.getContent():String{
    return this.text.toString().trim()
}

/**
 * Extension function to read the data from asset file
 */
fun AssetManager.readAssetsFile(fileName: String) =  open(fileName).bufferedReader().use { it.readText() }

fun Uri.findParameterValue(parameterName: String): String? {
    return query?.split('&')?.map {
        val parts = it.split('=')
        val name = parts.firstOrNull() ?: ""
        val value = parts.drop(1).firstOrNull() ?: ""
        Pair(name, value)
    }?.firstOrNull { it.first == parameterName }?.second
}