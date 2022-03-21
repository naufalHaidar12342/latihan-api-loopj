package xyz.heydarrn.loopjdanparsingjson

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotesAdapter(val listReview :ArrayList<String>) :RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
        var bindQuote:TextView=view.findViewById(R.id.textview_each_quote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.quote_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindQuote.text=listReview[position]
    }

    override fun getItemCount(): Int {
        return listReview.size
    }
}