//This is the package name of the application.
// It's used to uniquely identify the app on the device and in the Google Play Store.
package com.example.myapplication

//These are the imports for the Android classes that is need to use in the code.
import android.content.Intent //This is used to create an intent that will start the SecondActivity.
import android.os.Bundle //This is used to pass data between activities.
import android.widget.ImageButton //This is used to create a button that will start the SecondActivity.
import androidx.appcompat.app.AppCompatActivity //This is used to create the MainActivity class.

//This is the MainActivity class that extends the AppCompatActivity class.
class MainActivity : AppCompatActivity() { //This is the MainActivity class.
    //This is the onCreate function that is called when the activity is created.
    override fun onCreate(savedInstanceState: Bundle?) { //This is the onCreate function.
        super.onCreate(savedInstanceState) //This calls the onCreate function of the parent class.
        setContentView(R.layout.activity_start) //This sets the content view to the activity_start layout.

        supportActionBar?.hide() //This hides the action bar.

        //This sets an onClickListener on the start button that will start the SecondActivity.
        findViewById<ImageButton>(R.id.startbut1).setOnClickListener {//This sets an onClickListener on the start button.
            startActivity(Intent(this, SecondActivity::class.java)) //This starts the SecondActivity.
            //This sets the animation for the activity transition.
            this@MainActivity.overridePendingTransition(
                R.anim.animate_zoom_enter,
                R.anim.animate_zoom_exit
            )
        } 
    }
}
