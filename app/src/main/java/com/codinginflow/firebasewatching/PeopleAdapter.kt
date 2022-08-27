package com.codinginflow.firebasewatching

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class PeopleAdapter (var context: Context, var peopleList : List<User>) :
 RecyclerView.Adapter<PeopleAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.people , parent , false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.textView).text = peopleList[position].name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)


            val ss = peopleList[position].uid.toString()

            Log.e("TAG4" , ss)

            intent.putExtra("ABC",ss)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return peopleList.size
    }

    class MyViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

    }


}