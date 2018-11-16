package com.jaredrummler.cyanea.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity
import kotlinx.android.synthetic.main.activity_recycler_view.recyclerView
import kotlinx.android.synthetic.main.item_rv.view.subtitleText
import kotlinx.android.synthetic.main.item_rv.view.titleText


class RecyclerViewActivity : CyaneaAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_recycler_view)
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = DemoAdapter()
    recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
  }

  override fun getThemeResId(): Int = cyanea.themes.actionBarTheme

}

class DemoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  init {
    itemView.setOnClickListener {
      Toast.makeText(it.context, "Clicked item #${adapterPosition + 1}", Toast.LENGTH_SHORT).show()
    }
  }
}

class DemoAdapter : RecyclerView.Adapter<ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    LayoutInflater.from(parent.context).run {
      return DemoViewHolder(inflate(R.layout.item_rv, parent, false))
    }
  }

  override fun getItemCount(): Int = 100

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val number = position + 1
    holder.itemView.titleText.text = "Item #$number"
    holder.itemView.subtitleText.text = (when {
      number % 15 == 0 -> "FizzBuzz"
      number % 3 == 0 -> "Fizz"
      number % 5 == 0 -> "Buzz"
      else -> number.toString()
    })
  }

}
