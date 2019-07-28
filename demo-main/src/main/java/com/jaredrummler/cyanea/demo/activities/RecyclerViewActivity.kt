package com.jaredrummler.cyanea.demo.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity
import com.jaredrummler.cyanea.demo.R.layout
import kotlinx.android.synthetic.main.activity_recycler_view.recyclerView
import kotlinx.android.synthetic.main.item_rv.view.subtitleText
import kotlinx.android.synthetic.main.item_rv.view.titleText

class RecyclerViewActivity : CyaneaAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_recycler_view)
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = DemoAdapter()
    recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
  }

  class DemoAdapter : RecyclerView.Adapter<DemoViewHolder>() {

    override fun getItemCount(): Int = 100

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
      LayoutInflater.from(parent.context).run {
        return DemoViewHolder(inflate(layout.item_rv, parent, false))
      }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
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

  class DemoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    init {
      itemView.setOnClickListener {
        Toast.makeText(it.context, "Clicked item #${adapterPosition + 1}", Toast.LENGTH_SHORT).show()
      }
    }
  }
}
