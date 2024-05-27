package com.example.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.ContentView

class CatatanAdapter(private val context: Context, private val catatanList: List<Catatan>) : ArrayAdapter<Catatan>(context, 0, catatanList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false)
        }

        val currentCatatan = getItem(position)

        val textJudul = listItemView!!.findViewById<TextView>(R.id.judul)
        textJudul.text = currentCatatan?.toString()

        val time = listItemView.findViewById<TextView>(R.id.time)
        time.text = currentCatatan?.toString()

        return listItemView
    }
}
