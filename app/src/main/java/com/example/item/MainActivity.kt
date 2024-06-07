package com.example.item

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var listView: ListView
    val catatanList = mutableListOf<Catatan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.listcatatan)
    }

    override fun onStart() {
        super.onStart()
        catatanList.clear()

        val files = filesDir.listFiles()
        files?.forEach { file ->
            val catatan = bacaCatatan(file.name)
            catatan?.let { catatanList.add(it) }
        }

        val adapter = CatatanAdapter(this, catatanList)
        listView.adapter = adapter
        listView.isClickable = true

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = catatanList[position]
            val intent = Intent(this, TambahCatatan::class.java)
            val time = selectedItem.filename
            val filename = "$time"
            intent.putExtra("file", filename)
            Toast.makeText(this, filename, Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val selectedItem = catatanList[position]
            val filename = "${selectedItem.timestamp.substring(14)}.txt"

            hapusCatatan(filename)
            catatanList.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Catatan dihapus", Toast.LENGTH_SHORT).show()

            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tambahcatatan -> {
                Intent(this, TambahCatatan::class.java).also {
                    startActivity(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    fun bacaCatatan(filename: String): Catatan? {
        val file = File(filesDir, filename)

        if (!file.exists()) {
            Toast.makeText(this, "File tidak ditemukan", Toast.LENGTH_SHORT).show()
            return null
        }

        var inputStream: FileInputStream? = null
        return try {
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
                val filename: String = lines[3]
                Catatan(judul, catatan, timestamp, filename)
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal membaca catatan", Toast.LENGTH_SHORT).show()
            null
        } finally {
            inputStream?.close()
        }
    }

    private fun hapusCatatan(filename: String) {
        val file = File(filesDir, filename)
        if (file.exists()) {
            val deleted = file.delete()
            if (!deleted) {
                Toast.makeText(this, "Gagal menghapus catatan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

