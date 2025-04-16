package com.lkl.opengl

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

open class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    companion object {
        private const val TAG = "BaseActivity"
        private const val RC_INTERNET_PERM = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        methodRequiresCameraPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(RC_INTERNET_PERM)
    private fun methodRequiresCameraPermission() {
        val perms = arrayOf(Manifest.permission.INTERNET)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // Already have permission, do the thing
            // ...
            Log.d(TAG, "Internet Permission have open")
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this,
                "允许联网权限",
                RC_INTERNET_PERM,
                *perms
            )
            Log.d(TAG, "Internet Permission start request")
        }
    }

    override fun onPermissionsGranted(requestCode: Int, @NonNull perms: List<String>) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size)
        restartApplication()
    }


    override fun onPermissionsDenied(requestCode: Int, @NonNull perms: List<String>) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size)

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                .setTitle("权限申请")
                .setRationale("没有请求的权限，此应用可能无法正常工作。打开应用程序设置界面以修改应用程序权限。")
                .build()
                .show()
        } else {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            methodRequiresCameraPermission()
        }
    }

    /**
     * 重启应用程序
     */
    private fun restartApplication() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}