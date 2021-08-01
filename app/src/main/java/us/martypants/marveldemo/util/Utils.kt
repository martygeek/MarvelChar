package us.martypants.marvel

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import us.martypants.marveldemo.R
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

fun closeKeyboard(activity: Activity) {
    val imm = activity
        .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


fun isConnectedToNetwork(ctx: Context): Boolean {
    val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}

fun errorDialog(context: Context, error: String) {
    val builder: AlertDialog.Builder = context.let {
        AlertDialog.Builder(it, R.style.MyAlertDialogTheme)
    }

    builder.setMessage(error)
        ?.setTitle("Error")
        ?.setPositiveButton(android.R.string.ok, { dialog, _ ->
            dialog.dismiss()
        })

    val dialog: AlertDialog = builder.create()
    dialog.show()
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

