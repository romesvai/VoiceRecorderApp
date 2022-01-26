package com.romes.voicerecorderapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.io.File

class VoiceListAdapter (val allFiles : Array<File>, val context :Context,var clickListener: onItemListClickListener): RecyclerView.Adapter<VoiceListAdapter.VoiceViewHolder>(){


class VoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    var listImage : ImageView = itemView.findViewById(R.id.list_image_view)
    var listTitle : TextView = itemView.findViewById(R.id.filename)
    var listSub : TextView = itemView.findViewById(R.id.subheadingfile)

    fun initialize(allFiles: Array<File>, action:onItemListClickListener){
        itemView.setOnClickListener{
            action.onItemClick(allFiles[adapterPosition],adapterPosition)
        }
    }






}



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceListAdapter.VoiceViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.single_list_layout,parent,false)
        return VoiceListAdapter.VoiceViewHolder(view)

    }

    override fun onBindViewHolder(holder: VoiceViewHolder, position: Int) {
        var timeAgo : TimeAgo
        timeAgo = TimeAgo()
        holder.initialize(allFiles,clickListener)
        holder.listTitle.text = allFiles[position].name
        holder.listSub.text = timeAgo.getTimeAgo(allFiles[position].lastModified())
    }

    override fun getItemCount(): Int {
       return allFiles.size
    }


}
interface onItemListClickListener {
    fun onItemClick(file : File,position: Int)

}
