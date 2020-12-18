package com.example.fitnessapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.interfaces.NameDeleteSaveClickListener
import com.example.fitnessapp.dataclasses.MealMacrosDataForGet

class MealMacrosRecyclerAdapter(
    context: Context,
    private val mealMacrosList: List<MealMacrosDataForGet>,
    nameDeleteSaveClickListener: NameDeleteSaveClickListener
) :
    RecyclerView.Adapter<MealMacrosRecyclerAdapter.ViewHolder>() {
    private var mNameDeleteSaveClickListener: NameDeleteSaveClickListener? = null
    private val layoutInflater = LayoutInflater.from(context)

    init {
        this.mNameDeleteSaveClickListener = nameDeleteSaveClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.macro_meals_list_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = mealMacrosList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val macroMeal = mealMacrosList[position]
        if (macroMeal.Uid == null) {
            holder.saveEditMealNameImageButton.visibility = View.GONE
            holder.cancelEditMealNameImageButton.visibility = View.GONE
            holder.editMealName.visibility = View.GONE
            holder.mealName.visibility = View.GONE
            holder.mealName.text = ""
            holder.mealProtein.setText("")
            holder.mealCarbs.setText("")
            holder.mealFats.setText("")
        } else {
            holder.saveEditMealNameImageButton.visibility = View.GONE
            holder.cancelEditMealNameImageButton.visibility = View.GONE
            holder.editMealName.visibility = View.GONE
            holder.mealName.text = macroMeal.mealName
            holder.mealName.visibility = View.VISIBLE
            holder.mealProtein.setText(macroMeal.mealProteins.toString())
            holder.mealCarbs.setText(macroMeal.mealCarbs.toString())
            holder.mealFats.setText(macroMeal.mealFats.toString())
        }


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val mealProtein: EditText = itemView.findViewById(R.id.edit_mealProtein)
        val mealCarbs: EditText = itemView.findViewById(R.id.edit_mealCarbs)
        val mealFats: EditText = itemView.findViewById(R.id.edit_mealFats)
        val mealName: TextView = itemView.findViewById(R.id.mealName)
        val moreOptionsImageButton: ImageButton = itemView.findViewById(R.id.moreOptions)
        val addThisMealButton: Button = itemView.findViewById(R.id.button_addThisMeal)
        val editMealName: EditText = itemView.findViewById(R.id.edit_mealName)
        val saveEditMealNameImageButton: ImageButton = itemView.findViewById(R.id.saveEditMealName)
        val cancelEditMealNameImageButton :ImageButton = itemView.findViewById(R.id.cancelEditSaveMealName)

        init {
            moreOptionsImageButton.setOnClickListener {
                val optionsPopup = PopupMenu(itemView.context, moreOptionsImageButton)
                optionsPopup.inflate(R.menu.macros_meal_options_popup_menu)
                optionsPopup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.changeMealName -> {
                            mNameDeleteSaveClickListener?.giveNameToMeal(
                                editMealName,
                                mealName,
                                saveEditMealNameImageButton, mealMacrosList[adapterPosition], cancelEditMealNameImageButton
                            )
                            return@OnMenuItemClickListener true
                        }
                        R.id.deleteMeal -> {
                            mNameDeleteSaveClickListener?.deleteMeal(mealMacrosList[adapterPosition].Uid, adapterPosition)
                            return@OnMenuItemClickListener true
                        }
                        else -> {
                                mNameDeleteSaveClickListener?.saveMeal(
                                    mealName,
                                    mealProtein,
                                    mealCarbs,
                                    mealFats
                                )

                            return@OnMenuItemClickListener true
                        }

                    }

                })
                optionsPopup.show()
            }


            addThisMealButton.setOnClickListener {
                mNameDeleteSaveClickListener?.addAndPostMeal(
                    mealName.text.toString(),
                    mealProtein.text.toString(),
                    mealCarbs.text.toString(),
                    mealFats.text.toString(),
                    mealMacrosList[adapterPosition].Uid, mealProtein,mealCarbs, mealFats, adapterPosition
                )
            }
        }

        override fun onClick(v: View?) {

        }

    }
}