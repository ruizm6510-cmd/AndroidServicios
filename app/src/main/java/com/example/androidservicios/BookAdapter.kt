package com.example.androidservicios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
    private val bookList: List<Book>,
    private val onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tvBookId)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvBookTitulo)
        val tvAutor: TextView = itemView.findViewById(R.id.tvBookAutor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.tvId.text = book.id.toString()
        holder.tvTitulo.text = book.titulo
        holder.tvAutor.text = book.autor

        holder.itemView.setOnClickListener {
            onItemClick(book)
        }
    }

    override fun getItemCount(): Int = bookList.size
}
