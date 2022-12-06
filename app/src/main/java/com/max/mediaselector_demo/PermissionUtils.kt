package com.max.mediaselector_demo;

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


/**
 * A utility class to handle permissions
 */
class PermissionUtils {

    companion object {

        /**
         * 判断应用是否被永久拒绝了
         * (从未申请过权限该方法不适用)
         *
         * @param activity - the context
         * @param permission - the permission
         *
         * @return - true 如果权限被永久拒绝了
         *
         */
        fun isPermissionDeniedPermanently(activity: Activity, permission: String): Boolean {
            return !isPermissionGranted(activity, permission) &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
        }

        /**
         * Checks if a permission is already granted.
         *
         * @param activity - the context
         * @param permission - the permission
         *
         * @return - true if the permission is already granted
         */
        fun isPermissionGranted(activity: Activity, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(activity, permission) ==
                    PackageManager.PERMISSION_GRANTED
        }

    }

}
