package com.xcheko51x.adminmenurestaurantapp.Clases

class Platillo {

    lateinit var nombrePlatillo: String
    lateinit var descripcionPlatillo: String
    lateinit var precio: String
    lateinit var categoria: String

    constructor(
        nombrePlatillo: String,
        descripcionPlatillo: String,
        precio: String,
        categoria: String
    ) {
        this.nombrePlatillo = nombrePlatillo
        this.descripcionPlatillo = descripcionPlatillo
        this.precio = precio
        this.categoria = categoria
    }

}