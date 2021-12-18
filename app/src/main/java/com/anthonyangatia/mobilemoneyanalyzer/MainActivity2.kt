package com.anthonyangatia.mobilemoneyanalyzer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.anthonyangatia.mobilemoneyanalyzer.databinding.ActivityMain2Binding
import com.anthonyangatia.mobilemoneyanalyzer.serviceandreceiver.SmsService
import timber.log.Timber

class MainActivity2 : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 1

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())//Using timber for debuging
        checkForSmsPermission()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_SMS),
                PackageManager.PERMISSION_GRANTED
            )
        } else {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
        Intent(this, SmsService::class.java).also {
            startService(it)
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main2)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_settings, R.id.navigation_notifications,R.id.navigation_search
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    private fun checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            Timber.i("Permission not granted")
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECEIVE_SMS),
                MY_PERMISSIONS_REQUEST_RECEIVE_SMS
            )
        } else {
            // Permission already granted. Enable the message button.
//            enableSmsButton();
            Toast.makeText(this, "Permission Already granted", Toast.LENGTH_SHORT).show();
        }
    }
}