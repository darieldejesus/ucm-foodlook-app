package es.ucm.foodlook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import es.ucm.foodlook.R
import es.ucm.foodlook.types.DishImage
import es.ucm.foodlook.types.Recent

class RecentResultAdapter(private val context: Context, private val dataSource: ArrayList<Recent>) : BaseAdapter() {
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
        val rowView = inflater.inflate(R.layout.list_item_recent, parent, false)
        // https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin
        val textView = rowView.findViewById<TextView>(R.id.tvName);
        val imageView = rowView.findViewById<ImageView>(R.id.ivPreview);

        val recent = getItem(position) as Recent

        Picasso.get().load(recent.image).into(imageView)
        textView.text = recent.name

        return rowView
    }
}