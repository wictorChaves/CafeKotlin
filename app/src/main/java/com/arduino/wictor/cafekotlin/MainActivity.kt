package com.arduino.wictor.cafekotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var ip = "0.0.0.0"
    var port = 80

    lateinit var btn_on: Button
    lateinit var btn_off: Button
    lateinit var btn_temperature: Button
    lateinit var btn_refresh_address: Button

    lateinit var txt_ip: EditText
    lateinit var txt_port: EditText
    lateinit var txt_response: TextView
    lateinit var txt_temperature: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.loadElements()
        this.getAddress()
    }

    fun loadElements() {
        this.btn_on = findViewById(R.id.btn_on)
        this.btn_off = findViewById(R.id.btn_off)
        this.btn_temperature = findViewById(R.id.btn_temperature)
        this.btn_refresh_address = findViewById(R.id.btn_refresh_address)

        this.txt_ip = findViewById(R.id.txt_ip)
        this.txt_port = findViewById(R.id.txt_port)
        this.txt_response = findViewById(R.id.txt_response)
        this.txt_temperature = findViewById(R.id.txt_temperature)
    }

    fun getAddress() {
        this.ip = this.txt_ip.text.toString()
        this.port = Integer.parseInt(this.txt_port.text.toString())
    }

    fun onClickRefreshAddress(v: View) {
        this.getAddress()
    }

    fun onClickShowTemperature(v: View) {
        val socketTemp = SocketTemp(this.ip, this.port)
        socketTemp.addComponents(this.txt_temperature, this.txt_response, this.btn_temperature)
        socketTemp.setSendMessage("[msg]:temp")
        socketTemp.execute()
        this.btn_temperature.text = "Loading... "
    }

    fun onClickBtnOn(v: View) {
        this.sendMessage("start", this.btn_on)
    }

    fun onClickBtnOff(v: View) {
        this.sendMessage("off", this.btn_off)
    }

    private fun sendMessage(message: String, btn: Button) {
        val socketClient = SocketClient(this.ip, this.port)
        socketClient.addComponents(this.txt_response, btn)
        socketClient.setSendMessage("[msg]:$message")
        socketClient.execute()
        btn.text = "Loading... "
    }
}
