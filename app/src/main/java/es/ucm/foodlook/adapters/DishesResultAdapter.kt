package es.ucm.foodlook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import es.ucm.foodlook.R
import es.ucm.foodlook.types.Dish

class DishesResultAdapter(private val context: Context, private val dataSource: ArrayList<Dish>) : BaseAdapter() {
    private val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_item_dish, parent, false)
        // https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin
        val textView = rowView.findViewById<TextView>(R.id.dish_name);

        val dish = getItem(position) as Dish

        textView.text = dish.name

        return rowView
    }
}