package com.example.item

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TambahCatatan : AppCompatActivity() {

    lateinit var inputjudul: EditText
    lateinit var inputcatatan: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_catatan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputjudul = findViewById(R.id.NamaFile)
        inputcatatan = findViewById(R.id.catatan)

        var filename = intent.getStringExtra("file") ?: ""
        if (filename.isNotEmpty()) {
            bacaCatatan(filename)
        }

        val simpan: Button = findViewById(R.id.btnsimpan)
        simpan.setOnClickListener {
            if (filename.isEmpty()) {
                buatcatatan()
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            } else {
                editcatatan(filename)
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tambahcatatan -> {
                Intent(this, TambahCatatan::class.java).also {
                    startActivity(it)
                    finish()
                }
                true
            }
            R.id.listcatatan -> {
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun buatcatatan() {
        val judul = inputjudul.text.toString()
        val catatan = inputcatatan.text.toString()

        if (judul.isEmpty() || catatan.isEmpty()) {
            Toast.makeText(this, "Judul dan Catatan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val time = timepicker()
        val filename = "$time.txt"
        val file = File(filesDir, filename)
        val lastModified = "Last Modified ${timepicker()}"

        val data = "$judul\n\n$catatan\n\n$lastModified"

        try {
            FileOutputStream(file, false).use { outputStream ->
                outputStream.write(data.toByteArray())
                outputStream.flush()
                Toast.makeText(this, "catatan berhasil disimpan", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "catatan gagal disimpan", Toast.LENGTH_SHORT).show()
        }
    }

    fun editcatatan(file: String) {
        val judul = inputjudul.text.toString()
        val catatan = inputcatatan.text.toString()

        if (judul.isEmpty() || catatan.isEmpty()) {
            Toast.makeText(this, "Judul dan Catatan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val filename = file
        val file = File(filesDir, filename)
        val lastModified = "Last Modified ${timepicker()}"

        val data = "$judul\n\n$catatan\n\n$lastModified"

        try {
            FileOutputStream(file, false).use { outputStream ->
                outputStream.write(data.toByteArray())
                outputStream.flush()
                Toast.makeText(this, "catatan berhasil disimpan", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "catatan gagal disimpan", Toast.LENGTH_SHORT).show()
        }
    }

    fun bacaCatatan(filename: String) {
        val file = File(filesDir, filename)

        if (!file.exists()) {
            Toast.makeText(this, "File tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            FileInputStream(file).use { inputStream ->
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                val text = String(buffer)

                val lines = text.split("\n\n")
                if (lines.size >= 3) {
                    val judul = lines[0]
                    val catatan = lines[1]
                    inputjudul.setText(judul)
                    inputcatatan.setText(catatan)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal membaca catatan", Toast.LENGTH_SHORT).show()
        }
    }

    fun timepicker(): String {
        val currentTime = Calendar.getInstance().time
        val formatTime = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault())
        return formatTime.format(currentTime)
    }
}
