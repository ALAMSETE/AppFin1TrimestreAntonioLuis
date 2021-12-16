package com.example.appfin1trimestreantonioluis

// Esta es la clase encargada de llamar a una ventana u otra, segun la opcion que se clickee en el menu desplegable
sealed class Navegacion(var ruta:String, var icono: Int, var titulo:String) {
    object Principal: Navegacion("principal", R.drawable.ic_home, "Inicio")
    object Perfil : Navegacion("perfil", R.drawable.outline_directions_car_filled_20, "Añadir")
    object Opciones : Navegacion("opciones", R.drawable.ic_settings, "Eliminar")
}