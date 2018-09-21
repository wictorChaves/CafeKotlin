package com.arduino.wictor.cafekotlin

import android.os.AsyncTask
import android.widget.Button
import android.widget.TextView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException

class SocketClient internal constructor(internal var dstAddress: String, internal var dstPort: Int) : AsyncTask<String, String, Void>() {
    internal var response = ""
    internal var message = ""
    internal var old_label = ""

    internal var socket: Socket? = null

    /**
     * Components
     */
    lateinit internal var textResponse: TextView
    lateinit internal var btn_click: Button

    fun addComponents(textResponse: TextView, btn_click: Button) {
        this.textResponse = textResponse
        this.btn_click = btn_click
        this.old_label = this.btn_click.text.toString()
    }

    fun setSendMessage(message: String) {
        this.message = message
    }

    @Throws(IOException::class)
    private fun sendMessage(socket: Socket) {
        val outputStream = socket.getOutputStream()
        val bytes = message.toByteArray()
        outputStream.write(bytes)
        outputStream.flush()
    }

    @Throws(IOException::class)
    private fun getMessage(socket: Socket) {
        val byteArrayOutputStream = ByteArrayOutputStream(1024)
        val buffer = ByteArray(1024)
        val bytesRead: Int
        val inputStream = socket.getInputStream()
        bytesRead = inputStream.read(buffer)
        byteArrayOutputStream.write(buffer, 0, bytesRead)
        response += byteArrayOutputStream.toString("UTF-8")
    }

    override fun doInBackground(vararg arg0: String): Void? {

        try {
            socket = Socket(dstAddress, dstPort)
            this.sendMessage(socket!!)
            this.getMessage(socket!!)
        } catch (e: UnknownHostException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            response = "UnknownHostException: " + e.toString()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            response = "IOException: " + e.toString()
        } finally {
            if (socket != null) {
                try {
                    socket!!.close()
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

            }
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        try {
            textResponse.text = response
            btn_click.text = this.old_label
            this.socket!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
