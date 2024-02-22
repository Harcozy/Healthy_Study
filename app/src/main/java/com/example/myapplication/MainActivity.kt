import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val startButton: ImageButton = findViewById(R.id.imageButton)
        startButton.setOnClickListener {
            // Navigate to the Main Screen Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
