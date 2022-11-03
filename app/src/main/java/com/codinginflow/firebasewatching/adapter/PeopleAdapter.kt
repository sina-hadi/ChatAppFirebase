package com.codinginflow.firebasewatching.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.firebasewatching.R
import com.codinginflow.firebasewatching.data.User
import com.codinginflow.firebasewatching.ui.ChatActivity

class PeopleAdapter(var context: Context, var peopleList: List<User>) :
    RecyclerView.Adapter<PeopleAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.people, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.textView).text = peopleList[position].name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("USER_UID", peopleList[position].uid.toString())
            intent.putExtra("USER_NAME", peopleList[position].name.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return peopleList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}