package com.cmpt362.regathering.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cmpt362.regathering.databinding.ItemEventBinding
import com.cmpt362.regathering.model.Event
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

open class EventAdapter(query: Query, private val listener: OnEventSelectedListener) :
    FirestoreAdapter<EventAdapter.ViewHolder>(query) {

    interface OnEventSelectedListener {
        fun onEventSelected(Event: DocumentSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }

    class ViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            snapshot: DocumentSnapshot,
            listener: OnEventSelectedListener?
        ) {

            val event = snapshot.toObject<Event>()
            if (event == null) {
                return
            }

            binding.eventItemName.text = event.name
            binding.eventItemAddress.text = event.location
            binding.eventItemDate.text = event.date
            if(event.image.isNotEmpty()){
                binding.imageEventShow.setImageBitmap(stringToBitMap(event.image))
            }
            // Click listener
            binding.root.setOnClickListener {
                listener?.onEventSelected(snapshot)
            }
        }
        private fun stringToBitMap(encodedString: String?): Bitmap? {
            return try {
                val encodeByte =
                    Base64.decode(encodedString, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            } catch (e: Exception) {
                e.message
                null
            }
        }
    }

}
