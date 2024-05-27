package com.example.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CatatanAdapter(private val context: Context, private val catatanList: List<Catatan>) : ArrayAdapter<Catatan>(context, 0, catatanList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false)

        val currentCatatan = getItem(position)

        val textJudul = listItemView.findViewById<TextView>(R.id.judul)
        val time = listItemView.findViewById<TextView>(R.id.time)

        textJudul.text = currentCatatan?.judul
        time.text = currentCatatan?.timestamp

        return listItemView
    }
}
