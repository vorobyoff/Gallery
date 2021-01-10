package com.vorobyoff.gallery.base

interface Mapper<in T, out R> {
    fun map(type: T?): R
}