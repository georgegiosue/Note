package com.nmrc.note.entities

class Person(private var name: String) {

    fun setName(name : String) { this.name = name }

    fun getName() : String = this.name

    override fun toString() : String {
        return "Nombre : ${getName()}"
    }

}