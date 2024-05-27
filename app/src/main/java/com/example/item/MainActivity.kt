package com.example.item

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import com.example.item.TambahCatatan

class MainActivity<Catatan> : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        val listView = findViewById<ListView>(R.id.listcatatan)
        val catatanList = mutableListOf<com.example.item.Catatan>()

        val files = filesDir.listFiles()
        files?.forEach { file ->
            val catatan = bacaCatatan(file.name)
            catatan?.let { catatanList.add(it) }
        }


        val adapter = CatatanAdapter(this,catatanList)
        listView.adapter = adapter
        listView.isClickable = true

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = catatanList[position]
            val intent = Intent(this, TambahCatatan::class.java)
            var time = selectedItem.timestamp.substring(14)
            var filename = "$time.txt"
            intent.putExtra("file",filename)
            Toast.makeText(this,"$filename",Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tambahcatatan -> {
                Intent(this, TambahCatatan::class.java).also {
                    startActivity(it)
                    finish()
                }
                return true
            }

            R.id.listcatatan -> {
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    fun bacaCatatan(filename: String): com.example.item.Catatan? {
        val file = File(filesDir, filename)

        if (!file.exists()) {
            Toast.makeText(this, "File tidak ditemukan", Toast.LENGTH_SHORT).show()
            return null
        }

        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(file)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val text = String(buffer)

            val lines = text.split("\n\n")
            if (lines.size >= 3) {
                val judul: String = lines[0]
                val catatan: String = lines[1]
                val timestamp: String = lines[2]
                return Catatan(judul, catatan, timestamp)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal membaca catatan", Toast.LENGTH_SHORT).show()
        } finally {
            inputStream?.close()
        }
        return null
    }


}