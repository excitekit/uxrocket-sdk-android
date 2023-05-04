package com.napoleonit.getcrmandroid.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.napoleonit.getcrmandroid.R
import com.napoleonit.uxrocket.UXRocket
import com.napoleonit.uxrocket.data.models.http.ContextEvent
import com.napoleonit.uxrocket.data.models.local.LogIdentityModel

class IdentityFragment : Fragment() {

    companion object {
        fun getInstance() = IdentityFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UXRocket.logEvent(
            itemIdentificator = "IdentityPage",
            itemName = "Identity page",
            event = ContextEvent.OPEN_PAGE
        )
    }
    // Список товаров в корзине
    private val identityItems = mutableListOf<Item>()

    // Адаптер для списка товаров
    private lateinit var adapter: IdentityItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Надуваем (inflate) вашу XML разметку для фрагмента.
        val view = inflater.inflate(R.layout.fragment_identity, container, false)

        // Настраиваем ваш фрагмент, например, добавляем обработчик событий на кнопку.
        val checkoutButton = view.findViewById<Button>(R.id.checkout_button)
        checkoutButton.setOnClickListener {
            // Обработчик событий на кнопку Оформить заказ.
            // Вызываем метод для обновления списка товаров в корзине.
            view.findViewById<TextInputEditText>(R.id.match_type).text.toString().toIntOrNull()
                ?.let { it1 -> updateCartItems(it1) }
        }

        val addItemButton = view.findViewById<FloatingActionButton>(R.id.add_item_button)
        addItemButton.setOnClickListener {
            addItem()
        }

        // Инициализируем адаптер для списка товаров в корзине.
        adapter = IdentityItemsAdapter(identityItems)

        // Находим RecyclerView и настраиваем его.
        val cartItemsRecyclerView = view.findViewById<RecyclerView>(R.id.identity_items)
        cartItemsRecyclerView.layoutManager = LinearLayoutManager(activity)
        cartItemsRecyclerView.adapter = adapter

        // Возвращаем надутую (inflated) разметку для фрагмента.
        return view
    }

    // Метод для обновления списка товаров в корзине.
    private fun updateCartItems(identityMatch: Int) {
        adapter.notifyDataSetChanged()
        val logIdentityModel = LogIdentityModel(identityMatch)
        for ((index, item) in identityItems.withIndex()) {
            when (index) {
                0 -> {logIdentityModel.type1 = item.identityType.toIntOrNull(); logIdentityModel.value1 = item.identityValue}
                1 -> {logIdentityModel.type2 = item.identityType.toIntOrNull(); logIdentityModel.value2 = item.identityValue}
                2 -> {logIdentityModel.type3 = item.identityType.toIntOrNull(); logIdentityModel.value3 = item.identityValue}
            }
        }
        UXRocket.logIdentityMatch(logIdentityModel)
        setItems(identityItems)
    }

    fun setItems(items: List<Item>) {
        identityItems.clear()
        identityItems.addAll(items)
        adapter.notifyDataSetChanged()
    }

    private fun addItem() {
        val newItem = Item()
        identityItems.add(newItem)
        adapter.notifyDataSetChanged()
    }

    private inner class IdentityItemsAdapter(val items: List<Item>) : RecyclerView.Adapter<IdentityItemsAdapter.ViewHolder>() {

        // Создаем ViewHolder для элемента списка.
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.identity_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.identityType.setText(item.identityType)
            holder.identityValue.setText(item.identityValue)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val identityType: TextInputEditText = itemView.findViewById(R.id.item_type)
            val identityValue: TextInputEditText = itemView.findViewById(R.id.item_value)

            init {
                // Add afterTextChanged listener to update Item values
                identityType.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        items[adapterPosition].identityType = s.toString()
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })

                identityValue.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        items[adapterPosition].identityValue = s.toString()
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
            }
        }
    }

    data class Item(
        var identityType: String = "",
        var identityValue: String = ""
    )
}