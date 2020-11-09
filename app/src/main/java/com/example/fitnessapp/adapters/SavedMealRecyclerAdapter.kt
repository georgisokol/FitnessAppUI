package com.example.fitnessapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.dataclasses.SavedMealForGet
import com.example.fitnessapp.interfaces.SavedMealClickListener

class SavedMealRecyclerAdapter(context: Context, private val savedMealsList: List<SavedMealForGet>, savedMealClickListener: SavedMealClickListener) :
    RecyclerView.Adapter<SavedMealRecyclerAdapter.ViewHolder>(){

    private val layoutInflater = LayoutInflater.from(context)
    private var mSavedMealClickListener :SavedMealClickListener? = null

    init {
        this.mSavedMealClickListener = savedMealClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.saved_meals_list_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = savedMealsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val savedMeal = savedMealsList[position]
        holder.savedMealTitle.text = savedMeal.mealName
        holder.savedMealProtein.text = savedMeal.protein.toString()
        holder.savedMealCarbs.text = savedMeal.carbs.toString()
        holder.savedMealFats.text = savedMeal.fats.toString()
    }



    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val savedMealTitle :TextView = itemView.findViewById(R.id.text_savedMealTitle)
        val savedMealProtein :TextView = itemView.findViewById(R.id.text_savedMealProtein)
        val savedMealCarbs :TextView = itemView.findViewById(R.id.text_savedMealCarbs)
        val savedMealFats :TextView = itemView.findViewById(R.id.text_savedMealFats)
        private val savedMealContainer : View = itemView.findViewById(R.id.savedMealContainer)
        override fun onClick(v: View?) {
        }
        init {
            savedMealContainer.setOnClickListener{
                mSavedMealClickListener?.containerClicked(savedMealsList[adapterPosition])
            }
        }


    }

}