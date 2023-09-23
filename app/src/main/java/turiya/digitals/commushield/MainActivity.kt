package turiya.digitals.commushield

import android.Manifest.permission.READ_SMS
import android.Manifest.permission.READ_CALL_LOG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity





class MainActivity : AppCompatActivity() {
    private val permissions =  ArrayList<String>()
    private var permissionsToRequest = ArrayList<String>();
    private val ALL_PERMISSIONS_RESULT = 107;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("error_one ","Test");
        permissions.add(READ_SMS)
        permissions.add(READ_CALL_LOG)
        permissionsToRequest = findUnAskedPermissions(permissions)
        requestPermissions(
            (permissionsToRequest.toTypedArray<String?>())!!,
            ALL_PERMISSIONS_RESULT
        )
    }

    private fun findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()
        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result
    }
    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }
    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }
}