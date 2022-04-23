package hu.ait.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        YoYo.with(Techniques.RollIn)
            .duration(2000)
            .playOn(findViewById(R.id.image))

        YoYo.with(Techniques.RollOut)
            .duration(2000)
            .onEnd{
                val intentScrolling = Intent()
                intentScrolling.setClass(this, ScrollingActivity::class.java)

                startActivity(intentScrolling)
            }
            .delay(3000)
            .playOn(findViewById(R.id.image))
    }
}