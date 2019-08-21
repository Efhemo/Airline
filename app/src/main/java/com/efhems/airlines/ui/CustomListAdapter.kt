package com.efhems.airlines.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.efhems.airlines.R
import com.efhems.airlines.domain.Airport


class CustomListAdapter(context: Context, val resource: Int,
                        private var storeDataLst: List<Airport>?): ArrayAdapter<Airport>(context, resource, storeDataLst!!){

    val listFilter: ListFilter = ListFilter()
    private var dataListAllItems: List<Airport>? = null

    override fun getCount(): Int {
        return storeDataLst!!.size
    }

    override fun getItem(position: Int): Airport? {
        Log.d(
            "CustomListAdapter",
            storeDataLst!![position].name
        )
        return storeDataLst!![position]
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var view1 = view

        if (view1 == null) {
            view1 = LayoutInflater.from(parent.context)
                .inflate(resource, parent, false)
        }

        val strName = view1!!.findViewById(R.id.textView) as TextView
        strName.text = getItem(position)?.name
        return view1
    }


    override fun getFilter(): Filter {
        return listFilter
    }

   inner class ListFilter: Filter() {

        private val lock = Any()
        override fun performFiltering(p0: CharSequence?): FilterResults {

            val results = Filter.FilterResults()
            if (dataListAllItems == null) {
                synchronized(lock) {
                    dataListAllItems = ArrayList(storeDataLst!!)
                }
            }

            if (p0 == null || p0.isEmpty()) {
                synchronized(lock) {
                    results.values = dataListAllItems
                    results.count = dataListAllItems!!.size
                }
            } else {
                val searchStrLowerCase = p0.toString().toLowerCase()

                val matchValues = ArrayList<Airport>()

                for (dataItem in dataListAllItems!!) {
                    if (dataItem.name.toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem)
                    }
                }

                results.values = matchValues
                results.count = matchValues.size
            }

            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {

            if (p1?.values != null) {
                storeDataLst = p1.values as ArrayList<Airport>
            } else {
                storeDataLst = null
            }
            if (p1?.count!! > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }

    }
}