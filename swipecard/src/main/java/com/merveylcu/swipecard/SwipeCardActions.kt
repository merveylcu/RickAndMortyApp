package com.merveylcu.swipecard

interface SwipeCardActions<T> {

    fun onSwipeLeft(item: T)

    fun onSwipeRight(item: T)
}
