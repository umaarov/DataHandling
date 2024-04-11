package uz.umarov.kotlinflowsexample

import android.widget.TextView
import java.lang.ref.WeakReference

object Logger {
    private var logTextView: WeakReference<TextView>? = null

    fun setLogTextView(textView: TextView) {
        logTextView = WeakReference(textView)
    }

    fun printToTextView(message: String) {
        logTextView?.get()?.append("$message\n")
    }
}