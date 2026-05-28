package com.petryniy1.budgetpilot.presentation.recyclers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.petryniy1.budgetpilot.domain.models.BaseItem

abstract class BaseViewHolder(view: View): RecyclerView.ViewHolder(view) {

    abstract fun bindViewHolder(item: BaseItem?)

}