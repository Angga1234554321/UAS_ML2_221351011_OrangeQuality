package com.example.orangequality

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class SimulasiActivity : AppCompatActivity() {

    private lateinit var tflite: Interpreter
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulasi)

        // Setup custom AppBar
        val toolbar: Toolbar = findViewById(R.id.custom_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Simulasi Prediksi Kualitas Jeruk"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Load model TFLite dari assets
        tflite = Interpreter(loadModelFile())

        // Referensi elemen UI
        val inputWeight = findViewById<EditText>(R.id.input_weight)
        val inputSize = findViewById<EditText>(R.id.input_size)
        val inputSweetness = findViewById<EditText>(R.id.input_sweetness)
        val inputAcidity = findViewById<EditText>(R.id.input_acidity)
        val inputFirmness = findViewById<EditText>(R.id.input_firmness)
        val inputSkinColor = findViewById<EditText>(R.id.input_skin_color)
        val inputJuice = findViewById<EditText>(R.id.input_juice)

        val btnPredict = findViewById<Button>(R.id.btn_predict)
        val txtResult = findViewById<TextView>(R.id.txt_result)
        pieChart = findViewById(R.id.pie_chart)
        pieChart.visibility = View.GONE

        btnPredict.setOnClickListener {
            try {
                val inputValues = floatArrayOf(
                    inputWeight.text.toString().toFloatOrNull() ?: 0f,
                    inputSize.text.toString().toFloatOrNull() ?: 0f,
                    inputSweetness.text.toString().toFloatOrNull() ?: 0f,
                    inputAcidity.text.toString().toFloatOrNull() ?: 0f,
                    inputFirmness.text.toString().toFloatOrNull() ?: 0f,
                    inputSkinColor.text.toString().toFloatOrNull() ?: 0f,
                    inputJuice.text.toString().toFloatOrNull() ?: 0f
                )

                if (inputValues.any { it == 0f }) {
                    Toast.makeText(this, "Mohon isi semua input dengan benar", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val inputArray = arrayOf(inputValues)
                val outputArray = Array(1) { FloatArray(1) }

                tflite.run(inputArray, outputArray)
                val result = outputArray[0][0]

                Log.d("SimulasiActivity", "Hasil prediksi: $result")

                // Tampilkan hasil teks
                if (result > 0.5f) {
                    txtResult.text = "🍊 Kualitas Jeruk: Baik!\nSegar dan layak konsumsi!"
                    txtResult.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
                } else {
                    txtResult.text = "⚠️ Kualitas Jeruk: Buruk\nSebaiknya tidak dikonsumsi 😢"
                    txtResult.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                }

                showPieChart(result)

            } catch (e: Exception) {
                Toast.makeText(this, "Terjadi error saat prediksi 🍊", Toast.LENGTH_SHORT).show()
                Log.e("SimulasiActivity", "TF Lite Error: ${e.message}", e)
            }
        }
    }

    private fun showPieChart(score: Float) {
        val entries = listOf(
            PieEntry(score * 100, "Baik"),
            PieEntry((1 - score) * 100, "Buruk")
        )

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(
            ContextCompat.getColor(this, android.R.color.holo_green_light),
            ContextCompat.getColor(this, android.R.color.holo_red_light)
        )
        dataSet.sliceSpace = 3f
        dataSet.valueTextSize = 14f

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.centerText = "Kualitas"
        pieChart.setEntryLabelColor(android.graphics.Color.BLACK)
        pieChart.visibility = View.VISIBLE
        pieChart.animateY(1000)
        pieChart.invalidate()
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = assets.openFd("jeruk_quality_reduced_features.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
