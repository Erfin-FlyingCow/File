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

    lateinit var inputjudul :EditText
    lateinit var inputcatatan : EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_catatan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputjudul  = findViewById<EditText>(R.id.NamaFile)
        inputcatatan  = findViewById<EditText>(R.id.catatan)



        var filename = intent.getStringExtra("file").toString()
        bacaCatatan(filename)

        val simpan : Button = findViewById(R.id.btnsimpan)
        simpan.setOnClickListener(){
            if(filename.isEmpty()){
                buatcatatan()
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
            else{
                editcatatan(filename)
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu,menu)
        return true
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

    fun buatcatatan(){



        var judul = inputjudul.text.toString()
        var catatan =inputcatatan.text.toString()

        if (judul.isEmpty()||catatan.isEmpty()){
            Toast.makeText(this,"Judul dan Catatan tidak boleh kosong",Toast.LENGTH_SHORT).show()
            return
        }
        else {


            var filename = "${timepicker()}.txt"
            var file = File(filesDir,filename)
            var lastModified = "Last Modified ${timepicker()}"

            var outputStream : FileOutputStream? = null

            var data = "$judul\n\n$catatan\n\n$lastModified"

            try {
                outputStream = FileOutputStream(file,false)
                outputStream.write(data.toByteArray())
                outputStream.flush()
                outputStream.close()
                Toast.makeText(this, "catatan berhasil disimpan", Toast.LENGTH_SHORT).show()
            } catch (e : IOException){
                e.printStackTrace()
                Toast.makeText(this, "catatan gagal disimpan", Toast.LENGTH_SHORT).show()
            }

        }


    }

    fun editcatatan(file :String){



        var judul = inputjudul.text.toString()
        var catatan =inputcatatan.text.toString()

        if (judul.isEmpty()||catatan.isEmpty()){
            Toast.makeText(this,"Judul dan Catatan tidak boleh kosong",Toast.LENGTH_SHORT).show()
            return
        }
        else {


            var filename = "$file"
            var file = File(filesDir,filename)
            var lastModified = "Last Modified ${timepicker()}"

            var outputStream : FileOutputStream? = null

            var data = "$judul\n\n$catatan\n\n$lastModified"

            try {
                outputStream = FileOutputStream(file,false)
                outputStream.write(data.toByteArray())
                outputStream.flush()
                outputStream.close()
                Toast.makeText(this, "catatan berhasil disimpan", Toast.LENGTH_SHORT).show()
            } catch (e : IOException){
                e.printStackTrace()
                Toast.makeText(this, "catatan gagal disimpan", Toast.LENGTH_SHORT).show()
            }

        }


    }

    public fun bacaCatatan(filename: String) {
        val file = File(filesDir, filename)

        if (!file.exists()) {
            Toast.makeText(this, "File tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(file)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val text = String(buffer)

            val lines = text.split("\n\n")
            if (lines.size >= 2) {
                val judul = lines[0]
                val catatan = lines[1]
                val time = lines[2]
                inputjudul.setText(judul)
                inputcatatan.setText(catatan)
                return
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal membaca catatan", Toast.LENGTH_SHORT).show()
        } finally {
            inputStream?.close()
        }
    }

    fun timepicker (): String {
        var currentTime = Calendar.getInstance().time
        var formatTime = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault())
        var formatedDateTime = formatTime.format(currentTime)


        return formatedDateTime
    }

}
