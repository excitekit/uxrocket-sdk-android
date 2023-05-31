package com.napoleonit.getcrmandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.napoleonit.getcrmandroid.R
import com.napoleonit.uxrocket.UXRocket


class CartFragment : Fragment() {

    companion object {
        fun getInstance() = CartFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UXRocket.logEvent(
            itemIdentificator = "CardPage",
            itemName = "Card page",
            event = "openpage"
        )
    }
    // Список товаров в корзине
    private val cartItems = mutableListOf<Item>()

    // Адаптер для списка товаров
    private lateinit var adapter: CartItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Надуваем (inflate) вашу XML разметку для фрагмента.
        val view = inflater.inflate(R.layout.fragment_card, container, false)

        // Настраиваем ваш фрагмент, например, добавляем обработчик событий на кнопку.
        val checkoutButton = view.findViewById<Button>(R.id.checkout_button)
        checkoutButton.setOnClickListener {
            // Обработчик событий на кнопку Оформить заказ.
            // Вызываем метод для обновления списка товаров в корзине.
            updateCartItems()
        }

        val addItemButton = view.findViewById<FloatingActionButton>(R.id.add_item_button)
        addItemButton.setOnClickListener {
            addItem()
        }

        // Инициализируем адаптер для списка товаров в корзине.
        adapter = CartItemsAdapter(cartItems)

        // Находим RecyclerView и настраиваем его.
        val cartItemsRecyclerView = view.findViewById<RecyclerView>(R.id.cart_items)
        cartItemsRecyclerView.layoutManager = LinearLayoutManager(activity)
        cartItemsRecyclerView.adapter = adapter

        // Возвращаем надутую (inflated) разметку для фрагмента.
        return view
    }

    // Метод для обновления списка товаров в корзине.
    private fun updateCartItems() {
        UXRocket.logEvent(
            itemIdentificator = "checkout_button",
            itemName = "checkout_button pressed",
            event = "buttons",
            cartSum = cartItems.sumOf { item: Item -> item.price},
            productCode = "All Items",
            productCount = cartItems.count(),
            productPrice = null,
        )
        setItems(cartItems)
    }

    fun setItems(items: List<Item>) {
        cartItems.clear()
        cartItems.addAll(items)
        adapter.notifyDataSetChanged()
    }

    private fun addItem() {
        val countItems = cartItems.count() + 1
        val newItem = Item("New Item$countItems")
        cartItems.add(newItem)
        adapter.notifyDataSetChanged()

        UXRocket.logEvent(
            itemIdentificator = "add_item_button",
            itemName = "add_item_button pressed",
            event = "buttons",
            cartSum = cartItems.sumOf { item: Item -> item.price},
            productCode = newItem.name,
            productCount = countItems,
            productPrice = newItem.price,
        )
    }

    // Внутренний класс адаптера для списка товаров в корзине.
    private inner class CartItemsAdapter(val items: List<Item>) : RecyclerView.Adapter<CartItemsAdapter.ViewHolder>() {

        // Создаем ViewHolder для элемента списка.
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
            return ViewHolder(view)
        }

        // Связываем данные с ViewHolder.
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.nameTextView.text = item.name
            holder.priceTextView.text = item.price.toString()
        }

        // Возвращает количество элементов в списке.
        override fun getItemCount(): Int {
            return items.size
        }

        // ViewHolder для элемента списка.
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.item_name)
            val priceTextView: TextView = itemView.findViewById(R.id.item_price)
        }
    }

    // Внутренний класс для модели товара.
    data class Item(
        val name: String = "",
        val price: Double = (1..10000).random() / 100.0
    )
}