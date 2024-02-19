package cz.olgapec.myway

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cz.olgapec.myway.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var startWalkingButton: Button
    private lateinit var stepDetectorSensor: Sensor
    private var isWalkingStarted = false
    private var stepCount = 0

    companion object {
        private const val REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 1
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        startWalkingButton = findViewById(R.id.startWalkingButton)
        startWalkingButton.text = getString(R.string.tlacitko)
        startWalkingButton.setOnClickListener {
            isWalkingStarted = true
            stepCount = 0
            binding.stepCountTextView.text = getString(R.string.pocet_kroku, stepCount)
            stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!!
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                REQUEST_ACTIVITY_RECOGNITION_PERMISSION
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("stepCount", stepCount)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        stepCount = savedInstanceState.getInt("stepCount")
        binding.stepCountTextView.text = getString(R.string.pocet_kroku, stepCount)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR && isWalkingStarted) {
            stepCount++
            runOnUiThread {
                binding.stepCountTextView.text = getString(R.string.pocet_kroku, stepCount)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_ACTIVITY_RECOGNITION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, do nothing for now
            } else {
                Toast.makeText(this, "Oprávnění k aktivnímu rozpoznání aktivity není uděleno.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (isWalkingStarted) {
            sensorManager.unregisterListener(this)
        }
    }
}