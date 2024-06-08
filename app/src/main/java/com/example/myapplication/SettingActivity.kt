//This is the package name of the application.
// It's used to uniquely identify the app on the device and in the Google Play Store.
package com.example.myapplication

//These are the imports for the Android classes that is need to use in the code.
import android.content.Intent //This is used to create an intent that will start the SecondActivity.
import android.os.Bundle //This is used to pass data between activities.
import android.widget.ImageButton //This is used to create a button that will start the SecondActivity.
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity //This is used to create the MainActivity class.

//This is the SettingActivity class that extends the AppCompatActivity class.
class SettingActivity : AppCompatActivity() { //This is the SettingActivity class.
    //This is the onCreate function that is called when the activity is created.
    override fun onCreate(savedInstanceState: Bundle?) { //This is the onCreate function.
        super.onCreate(savedInstanceState) //This calls the onCreate function of the parent class.
        setContentView(R.layout.activity_setting) //This sets the content view to the activity_start layout.

        supportActionBar?.hide() //This hides the action bar.

        //This sets an onClickListener on the start button that will start the SecondActivity.
        findViewById<ImageButton>(R.id.home2_ico).setOnClickListener {//This sets an onClickListener on the start button.
            startActivity(Intent(this, MainActivity::class.java)) //This starts the SecondActivity.
        }

        val button7: ImageButton = findViewById(R.id.art_ico)
        button7.setOnClickListener {
            comingsoon()
        }

        val set1: ImageButton = findViewById(R.id.set1_button)
        set1.setOnClickListener {
            comingsoon()
        }

        val set2: ImageButton = findViewById(R.id.set2_button)
        set2.setOnClickListener {
            comingsoon()
        }

        val set3: ImageButton = findViewById(R.id.set3_button)
        set3.setOnClickListener {
            comingsoon()
        }

        val set4: ImageButton = findViewById(R.id.set4_button)
        set4.setOnClickListener {
            comingsoon()
        }

        val set5: ImageButton = findViewById(R.id.set5_button)
        set5.setOnClickListener {
            comingsoon()
        }

    }

    private fun comingsoon() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Soooo Sorry!")
        builder.setMessage("We know you're excited to use this feature, but it's still under heavy development. Please check back later.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
