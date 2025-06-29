package com.example.orangequality

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private fun setupMenu(menuId: Int, label: String, iconRes: Int, destination: Class<*>) {
        val menuLayout = findViewById<LinearLayout>(menuId)
        val textView = menuLayout.findViewById<TextView>(R.id.textMenu)
        val iconView = menuLayout.findViewById<ImageView>(R.id.menuIcon)

        textView.text = label
        iconView.setImageResource(iconRes)

        menuLayout.setOnClickListener {
            startActivity(Intent(this, destination))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi toolbar
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)

        setupMenu(R.id.menu_tentang, "Tentang Aplikasi", R.drawable.ic_info, TentangActivity::class.java)
        setupMenu(R.id.menu_dataset, "Dataset", R.drawable.ic_dataset, DatasetActivity::class.java)
        setupMenu(R.id.menu_fitur, "Fitur", R.drawable.ic_fitur, FiturActivity::class.java)
        setupMenu(R.id.menu_model, "Model", R.drawable.ic_model, ModelActivity::class.java)
        setupMenu(R.id.menu_simulasi, "Simulasi", R.drawable.ic_simulasi, SimulasiActivity::class.java)
    }
}
