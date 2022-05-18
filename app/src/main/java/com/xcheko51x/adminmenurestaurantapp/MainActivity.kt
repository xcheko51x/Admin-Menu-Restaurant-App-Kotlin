package com.xcheko51x.adminmenurestaurantapp

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    lateinit var cvCategorias: CardView
    lateinit var cvPlatillos: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "ADMIN MENÚ RESTAURANTAPP"
        window.statusBarColor = resources.getColor(R.color.gris_oscuro)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.mar_infinito)))

        cvCategorias = findViewById(R.id.cvCategorias)
        cvPlatillos = findViewById(R.id.cvPlatillos)

        cvCategorias.setOnClickListener {

            var intent = Intent(this, AdminCategorias::class.java)
            startActivity(intent)
        }

        cvPlatillos.setOnClickListener {

            var intent = Intent(this, AdminPlatillos::class.java)
            startActivity(intent)
        }
    }
}