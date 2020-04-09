package com.shaikjaleel.wsave.commoners


import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.jetbrains.anko.longToast

open class BaseFragment : Fragment() {

    open fun toast(message: String) {
        activity?.longToast(message)
    }

    // Check if user has granted storage permission
    fun storagePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

}
