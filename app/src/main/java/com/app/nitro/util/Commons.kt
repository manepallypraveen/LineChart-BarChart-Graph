package com.app.nitro.util

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


fun Any?.toJson(): String {
    return Gson().toJson(this)
}

fun Any?.tag(): String {
    return this?.javaClass?.simpleName ?: ""
}

fun createPartFromString(param: String): RequestBody {
    return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), param)
}

fun OpenChrome(usrl:String,context: Context){
    val i = Intent(Intent.ACTION_VIEW, Uri.parse(usrl))
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    i.setPackage("com.android.chrome")
    try {
        context.startActivity(i)
    } catch (e: ActivityNotFoundException) {
        i.setPackage(null)
        context. startActivity(i)
    }
}



fun urlAddingForPicture(url: String): String {
    var str = ""
    if (url.contains("https://nitro.com")) {
        str = url
    } else {
        str = "https://nitro.com/" + url
    }
    return str
}

fun isValidEmail(target: CharSequence?): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun isValidPasswordFormat(password: String): Boolean {
    val passwordREGEX = Pattern.compile(
        "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$"
    );
    return passwordREGEX.matcher(password).matches()
}

fun String.isValidMobileNumber() =
    Pattern.compile(
//        "^[6-9]{10}\$"
//        "^[0-9]{10}\$"
        "^[6-9]\\d{9}\$"
    ).matcher(this).matches()

fun isValid(email: String?): Boolean {
    val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$"
    val pat = Pattern.compile(emailRegex)
    return if (email == null) false else pat.matcher(email).matches()
}


// Function to validate
// GST (Goods and Services Tax) number.
fun String.isValidGSTNo(str: String?): Boolean {
    val regex = ("^[0-9]{2}[A-Z]{5}[0-9]{4}"
            + "[A-Z]{1}[1-9A-Z]{1}"
            + "Z[0-9A-Z]{1}$")
    val p = Pattern.compile(regex)
    if (str == null) {
        return false
    }
    val m: Matcher = p.matcher(str)
    return m.matches()
}

fun Any?.logInfo(message: String?) {
    Log.i(tag(), message ?: "")
}

fun Any?.logInfo(message: String?, data: Any?) {
    Log.i(tag(), message ?: ("" + "\t-->\t" + this.toJson()))
}

fun Any?.onToast(string: String, context: Context) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
}

fun Any?.logWarning(message: String?) {
    Log.w(tag(), message ?: "")
}

fun Any?.logWarning(throwable: Throwable?) {
    Log.w(tag(), throwable?.message, throwable)
}

fun Any?.logWarning(message: String?, throwable: Throwable?) {
    Log.w(tag(), message ?: ("" + "\t-->\t" + this.toJson()), throwable)
}

fun Any?.logError(message: String?) {
    Log.e(tag(), message ?: "")
}

fun Any?.logError(throwable: Throwable?) {
    Log.e(tag(), throwable?.message, throwable)
}

fun Any?.logError(message: String?, throwable: Throwable?) {
    Log.e(tag(), message ?: ("" + "\t-->\t" + this.toJson()), throwable)
}

fun Fragment.navigate(directions: NavDirections) {
    try {
        NavHostFragment.findNavController(requireParentFragment()).navigate(directions)
    } catch (e: java.lang.Exception) {
        logError(e)
    }
}

fun Fragment.popBackStack() {
    try {
        NavHostFragment.findNavController(requireParentFragment()).popBackStack()
    } catch (e: java.lang.Exception) {
        logError(e)
    }
}

fun EditText.showKeyboard() {
    postDelayed(
        {
            if (requestFocusFromTouch()) {
                (this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                setSelection(text.toString().length)
            }
        },
        200
    )
}

fun isValidPassword(password: String?): Boolean {
    val regex = ("^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,20}$")

    val p: Pattern = Pattern.compile(regex)
    if (password == null) {
        return false
    }
    val m: Matcher = p.matcher(password)
    return m.matches()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun EditText.transformIntoDatePicker(
    context: Context,
    format: String,
    maxDate: Date? = null,
    from: String? = null
) {
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false

    val myCalendar = Calendar.getInstance()
    val datePickerOnDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(format, Locale.UK)
            setText(sdf.format(myCalendar.time))
        }

    setOnClickListener {
        DatePickerDialog(
            context, datePickerOnDataSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).run {
            maxDate?.time?.also { datePicker.maxDate = it }
            if (from.equals("ServiceRequest"))
                datePicker.setMinDate(System.currentTimeMillis());
            show()
        }
    }
}

fun checkDate(date: String, date1: String): String {
    var result = ""
    val sdformat = SimpleDateFormat("yyyy-MM-dd")
    val d1 = sdformat.parse(date)
    val d2 = sdformat.parse(date1)
    if (d1.compareTo(d2) > 0) {
        result = "Date 1 occurs after Date 2"
    } else if (d1.compareTo(d2) < 0) {
        result = "Date 1 occurs before Date 2"
    } else if (d1.compareTo(d2) === 0) {
        result = "Both dates are equal"
    }
    return result
}


fun showAlertDialog(message: String, context: Context) {
    val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
    alertDialog.setTitle("Disclaimer")
    alertDialog.setMessage("\n" + message)
    alertDialog.setPositiveButton("Ok") { dialog, which ->
        dialog.cancel()
    }
    val alert: AlertDialog = alertDialog.create()
    alert.setCanceledOnTouchOutside(false)
    alert.show()
}

fun showAlertDialogToActivity(message: String, context: Context) {
    val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
//    alertDialog.setTitle(message)
    alertDialog.setMessage("\n" + message)
    alertDialog.setPositiveButton("Ok") { dialog, which ->
//        context.startActivity(
//            Intent(context, DashboardActivity::class.java).setFlags(
//                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            )
//        )
        dialog.cancel()
    }
    val alert: AlertDialog = alertDialog.create()
    alert.setCanceledOnTouchOutside(false)
    alert.show()
}