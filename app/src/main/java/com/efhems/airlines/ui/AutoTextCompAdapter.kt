package com.efhems.airlines.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.efhems.airlines.R
import com.efhems.airlines.domain.Airport

import java.util.ArrayList

class AutoTextCompAdapter(context: Context, resouceId: Int, textviewId: Int, categories: ArrayList<Airport>) :
    ArrayAdapter<Airport>(context, resouceId, textviewId, categories) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val airpot = getItem(position)

        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(parent.context).inflate(R.layout.spinner_item_layout, parent, false)
        }

        val textView = listItemView!!.findViewById<TextView>(R.id.textView)
        if (airpot != null) {
            textView.text = airpot.name
        }
        return listItemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getDropDownView(position, convertView, parent);
        val airport = getItem(position)
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(parent.context).inflate(R.layout.spinner_item_layout, parent, false)
        }

        val textView = listItemView!!.findViewById<TextView>(R.id.textView)
        if (airport != null) {
            textView.text = airport.name
        }
        return listItemView
    }
}
