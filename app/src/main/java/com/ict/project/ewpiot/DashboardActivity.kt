package com.ict.project.ewpiot

import android.app.AlertDialog
import android.app.Dialog
import android.health.connect.datatypes.units.Temperature
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.R
import com.google.firebase.database.ValueEventListener
import com.ict.project.ewpiot.databinding.ActivityDashboardBinding
import com.ict.project.ewpiot.databinding.DialogAboutBinding
import com.ict.project.ewpiot.databinding.DialogHistoryBinding
import java.util.Timer
import java.util.TimerTask
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var firebaseDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://ewp-iot-default-rtdb.firebaseio.com/")
    private lateinit var Moisture:String
    private lateinit var Humidity:String
    private lateinit var Temperature:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponent()
    }

    private fun initComponent() {

        val timer = Timer()

        val task = object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                updateDatetime()
            }
        }

        // Schedule the task to run every second
        timer.scheduleAtFixedRate(task, 0, 1000)

        binding.btnHistory.setOnClickListener {
            btnHistoryOnCLickListener()
        }
        binding.btnAbout.setOnClickListener {
            btnAboutOnCLickListener()
        }
    }

    private fun btnAboutOnCLickListener() {
        val aboutBinding = DialogAboutBinding.inflate(this.layoutInflater)
        showDialog(aboutBinding.root)
    }

    private fun btnHistoryOnCLickListener() {
        val historyBinding = DialogHistoryBinding.inflate(this.layoutInflater)
        historyBinding.historyMoisture.text = Moisture
        historyBinding.historyHumidity.text = Humidity
        historyBinding.historyTemperature.text = Temperature
        showDialog(historyBinding.root)
    }


    private fun showDialog(view: View) {
        var promptDialog = Dialog(this)
        val promptBuilder = AlertDialog.Builder(this)
        promptBuilder.setView(view)
        promptBuilder.setNegativeButton("Okay") { _, _ ->
            promptDialog.dismiss()
        }
        promptDialog = promptBuilder.create()
        if (promptDialog.window!=null) {
            promptDialog.window!!.setBackgroundDrawableResource(com.ict.project.ewpiot.R.drawable.button_primary)
        }
        promptDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDatetime() {
        val currentDateTime = LocalDateTime.now()

        // Define a date time formatter
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Format the current date and time using the formatter
        val formattedDateTime = currentDateTime.format(formatter)
        binding.datetime.text = formattedDateTime

        getData();
    }

    private fun getData() {

        getHistoryMoisture()
        getHistoryHumidity()
        getHistoryTemperature()

        firebaseDatabaseReference.child("Log")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {


                    if (snapshot.hasChild("Moisture")) {
                        Log.d("ON_DATA_FETCH", "READING MOISTURE")
                        val moisture =
                            snapshot.child("Moisture").getValue(Double::class.java) ?: 0.0
                        binding.moisture.text = moisture.toString()
                    }
                    if (snapshot.hasChild("Humidity")) {
                        Log.d("ON_DATA_FETCH", "READING HUMIDITY")
                        val humidity =
                            snapshot.child("Humidity").getValue(Double::class.java) ?: 0.0
                        binding.humidity.text = humidity.toString()
                    }
                    if (snapshot.hasChild("Temperature")) {
                        Log.d("ON_DATA_FETCH", "READING TEMPERATURE")
                        val temperature =
                            snapshot.child("Temperature").getValue(Double::class.java) ?: 0.0
                        binding.temperature.text = temperature.toString()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("addValueEventListener", "loadPost:onCancelled", error.toException())
                    Toast.makeText(
                        this@DashboardActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    private fun getHistoryMoisture(): String {
        Moisture = ""
        firebaseDatabaseReference.child("History/Moisture")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (childSnapshot in snapshot.children) {
                        val timestamp = childSnapshot.key
                        val value = childSnapshot.value
                        Moisture += timestamp.toString() + ": " + value + "\n"
                    }
                    Log.d("ON_MOISTURE_FETCH", Moisture)

                }

                override fun onCancelled(error: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("addValueEventListener", "loadPost:onCancelled", error.toException())
                    Toast.makeText(
                        this@DashboardActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        return Moisture
    }

    private fun getHistoryHumidity(): String {
        Humidity = ""
        firebaseDatabaseReference.child("History/Humidity")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (childSnapshot in snapshot.children) {
                        val timestamp = childSnapshot.key
                        val value = childSnapshot.value
                        Humidity += timestamp.toString() + ": " + value + "\n"
                    }
                    Log.d("ON_HUMIDITY_FETCH", Humidity)

                }

                override fun onCancelled(error: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("addValueEventListener", "loadPost:onCancelled", error.toException())
                    Toast.makeText(
                        this@DashboardActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        return Humidity
    }

    private fun getHistoryTemperature(): String {
        Temperature = ""
        firebaseDatabaseReference.child("History/Temperature")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (childSnapshot in snapshot.children) {
                        val timestamp = childSnapshot.key
                        val value = childSnapshot.value
                        Temperature += timestamp.toString() + ": " + value + "\n"
                    }

                    Log.d("ON_TEMPERATURE_FETCH", Temperature)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("addValueEventListener", "loadPost:onCancelled", error.toException())
                    Toast.makeText(
                        this@DashboardActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        return Temperature
    }
}