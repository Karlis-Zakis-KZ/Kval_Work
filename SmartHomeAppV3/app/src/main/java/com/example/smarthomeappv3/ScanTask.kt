package com.example.smarthomeappv3

import android.os.AsyncTask
import java.net.InetAddress

class ScanTask : AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg params: String): String {
        val startIp = params[0]
        val endIp = params[1]
        val start = startIp.split(".")
        val end = endIp.split(".")
        for (i in start[0].toInt()..end[0].toInt()) {
            for (j in start[1].toInt()..end[1].toInt()) {
                for (k in start[2].toInt()..end[2].toInt()) {
                    for (l in start[3].toInt()..end[3].toInt()) {
                        val address = "$i.$j.$k.$l"
                        val inet = InetAddress.getByName(address)
                        if (inet.isReachable(5000)) {
                            println("$address is reachable")
                        }else{
                            println("$address unreachable")
                        }
                    }
                }
            }
        }
        return "Scan complete"
    }

    override fun onPostExecute(result: String) {
        // Update the UI with the result here
    }
}