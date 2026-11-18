package com.example.androidservicios

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageRequest

class CharacterAdapter(
    private val characterList: List<Character>,
    private val onItemClick: (Character) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImagen: ImageView = itemView.findViewById(R.id.ivItemImagen)
        val tvNombre: TextView = itemView.findViewById(R.id.tvItemNombre)
        val tvEspecie: TextView = itemView.findViewById(R.id.tvItemEspecie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characterList[position]
        holder.tvNombre.text = character.name
        holder.tvEspecie.text = character.species

        // Clear image previous state
        holder.ivImagen.setImageResource(android.R.color.transparent)

        // Download image using Volley ImageRequest
        val context = holder.itemView.context
        val imageRequest = ImageRequest(
            character.imageUrl,
            { bitmap: Bitmap ->
                holder.ivImagen.setImageBitmap(bitmap)
            },
            0,
            0,
            ImageView.ScaleType.CENTER_CROP,
            Bitmap.Config.RGB_565,
            { error ->
                error.printStackTrace()
            }
        )

        VolleySingleton.getInstance(context).addToRequestQueue(imageRequest)

        holder.itemView.setOnClickListener {
            onItemClick(character)
        }
    }

    override fun getItemCount(): Int = characterList.size
}
