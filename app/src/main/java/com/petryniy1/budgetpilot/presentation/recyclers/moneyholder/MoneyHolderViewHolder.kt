package com.petryniy1.budgetpilot.presentation.recyclers.moneyholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.databinding.ItemMoneyHolderBinding
import com.petryniy1.budgetpilot.domain.models.MoneyHolder

class MoneyHolderViewHolder(

    private val binding: ItemMoneyHolderBinding,
    private val itemClickListenerMoneyHolder: MoneyHolderOnItemListener

    ) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun fromParent(
            parent: ViewGroup,
            itemClickListenerMoneyHolder: MoneyHolderOnItemListener
        ):
                MoneyHolderViewHolder {
            return MoneyHolderViewHolder(
                ItemMoneyHolderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                ),
                itemClickListenerMoneyHolder
            )
        }
    }

    fun bindView(item: MoneyHolder?) = with(binding) {
        textViewName.text = item?.name
        textViewBalance.text = root.context.getString(R.string.msg_currency_byn_amount_format,
            item?.balance?.div(100f) ?: 100f
        )

        itemMoneyHolder.setOnClickListener {
            item?.id.let { it1 ->
                if (it1 != null) {
                    itemClickListenerMoneyHolder.onItemClickListener(it1)
                }
            }
        }
    }
}