package com.example.appfin1trimestreantonioluis

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appfin1trimestreantonioluis.ui.theme.AppFin1TrimestreAntonioLuisTheme
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URL
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppFin1TrimestreAntonioLuisTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PantallaPrincipal()
                }
            }
        }
    }
}

@Composable
fun PantallaPrincipal() {
    // Creamos el Scaffold para crear la TopBar.
    // El valor DrawerValue define si el menu desplegable debe estar abierto o cerrado
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    // El scope define a donde debe apuntar
    val scope = rememberCoroutineScope();
    // Creamos el navegador para cambiar entre ventanas
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope = scope, scaffoldState = scaffoldState)},
        drawerContent = {
            CrearMenu(scope = scope, scaffoldState = scaffoldState, navController = navController)
        }
    ) {
        Navegador(navController = navController)
    }
}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState){
    // Creamos la barra de menu superior
    TopAppBar(
        //Ponemos un titulo a la barra de menu
        title = { Text(text = "Coches - Antonio Luis - Kotlin", fontSize = 18.sp)},
        // Establecemos un icono de navegación
        navigationIcon ={
            // Creamos el metodo para lo que queramos que haga cuando se clickee
            IconButton(onClick = {
                // Le decimos hacia donde tiene que apuntar
                scope.launch {
                    // Cuando se clickee en el icono, el menu se despliega
                    scaffoldState.drawerState.open()
                }
            }) {
                // Definimos el icono que queremos implementar
                Icon(Icons.Filled.Menu, "")
            }
        },
        // Le decimos que color queremos que tenga el menu
        backgroundColor = Color(239, 137, 0),
        // Definimos el color de las letras
        contentColor =Color.Black
    )
}

@Composable
fun CrearMenu(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController){
    //Creamos una lista en la que se guardaran las direciones de cada pantalla cuando se clickeen
    val items= listOf(
        Navegacion.Principal,
        Navegacion.Perfil,
        Navegacion.Opciones
    )

    Column(
        modifier = Modifier
            .background(color = Color(51, 51, 51 ))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(239, 137, 0)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.yeahlogo),
                contentDescription = "",
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(10.dp)
            )

        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { items ->
            CrearObjeto(item = items, selected = currentRoute == items.ruta, onItemClick = {

                navController.navigate(items.ruta) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }

                scope.launch {
                    scaffoldState.drawerState.close()
                }

            })
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "©Antonio Luis Andújar Martínez",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally)
        )

    }

}

@Composable
fun CrearObjeto(item: Navegacion, selected: Boolean, onItemClick: (Navegacion) -> Unit) {
    val background = if (selected) R.color.teal_700 else android.R.color.transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .height(45.dp)
            .background(colorResource(id = background))
            .padding(start = 10.dp)
    ) {

        Image(
            painter = painterResource(id = item.icono),
            contentDescription = item.titulo,
            colorFilter = ColorFilter.tint(Color.Black),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.titulo,
            fontSize = 16.sp,
            color = Color.White
        )

    }

}

@Composable
fun Principal() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .background(color = Color(51, 51, 51)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.background(color = Color.DarkGray)) {
            Column (modifier = Modifier.weight(1f)) {
                Text(text = "Marca", color = Color.White, fontSize = 20.sp)
            }
            Column (modifier = Modifier.weight(1f)) {
                Text(text = "Modelo", color = Color.White, fontSize = 20.sp)
            }
            Column (modifier = Modifier.weight(1f)) {
                Text(text = "Precio", color = Color.White, fontSize = 20.sp)
            }
        }
        Llamada()

    }
}

@Composable
fun Anadir() {
    var textFieldValueMarca by rememberSaveable { mutableStateOf("") }
    var textFieldValueModelo by rememberSaveable { mutableStateOf("") }
    var textFieldValuePrecio by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(51, 51, 51))


    ) {
        Text(
            text = "INSERTAR VEHICULOS",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )


        TextField(
            value = textFieldValueMarca,
            onValueChange = { nuevo ->
                textFieldValueMarca = nuevo
            },
            label = {
                Text(text = "Introducir marca", color = Color.White)
            },
            modifier = Modifier
                .padding(10.dp)
                .align(alignment = Alignment.CenterHorizontally),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right, color = Color.White)
        )


        TextField(
            value = textFieldValueModelo,
            onValueChange = { nuevo ->
                textFieldValueModelo = nuevo
            },
            label = {
                Text(text = "Introducir modelo", color = Color.White)
            },
            modifier = Modifier
                .padding( 10.dp)
                .align(alignment = Alignment.CenterHorizontally),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right, color = Color.White)
        )


        TextField(
            value = textFieldValuePrecio,
            onValueChange = { nuevo ->
                textFieldValuePrecio = nuevo
            },
            label = {
                Text(text = "Introducir precio", color = Color.White)
            },
            modifier = Modifier
                .padding( 10.dp)
                .align(alignment = Alignment.CenterHorizontally),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(textAlign = TextAlign.Right, color = Color.White)
        )

        Spacer(Modifier.height(20.dp) )


        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .size(width = 200.dp, height = 50.dp)
            ,


            onClick = {
                insertar(textFieldValueMarca,textFieldValueModelo,textFieldValuePrecio)
                textFieldValueMarca=""
                textFieldValueModelo=""
                textFieldValuePrecio=""
            },
            colors = ButtonDefaults.buttonColors(Color.Cyan)
        ){
            Text(text = "Insertar vehículo"
            )
        }


    }


}

@Composable
fun Eliminar() {
    var textFieldValueModelo by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(51, 51, 51))


    ) {
        Text(
            text = "ELIMINAR VEHICULOS",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        TextField(
            value = textFieldValueModelo,
            onValueChange = { nuevo ->
                textFieldValueModelo = nuevo
            },
            label = {
                Text(text = "Introducir modelo", color = Color.White)
            },
            modifier = Modifier
                .padding( 10.dp)
                .align(alignment = Alignment.CenterHorizontally),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right, color = Color.White)
        )

        Spacer(Modifier.height(20.dp) )


        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .size(width = 200.dp, height = 50.dp)
            ,


            onClick = {
                eliminar(textFieldValueModelo)
                textFieldValueModelo=""
            },
            colors = ButtonDefaults.buttonColors(Color.Cyan)
        ){
            Text(text = "Eliminar vehículo"
            )
        }


    }
}

@Composable
fun Navegador(navController: NavHostController) {

    NavHost(navController, startDestination = Navegacion.Principal.ruta) {

        composable(Navegacion.Principal.ruta) {
            Principal()
        }

        composable(Navegacion.Perfil.ruta) {
            Anadir()
        }

        composable(Navegacion.Opciones.ruta) {
            Eliminar()
        }

    }

}

fun insertar(marca:String,modelo:String,precio:String){

    val url = "http://iesayala.ddns.net/alamsete/insertcoche.php/?marca=$marca&modelo=$modelo&precio=$precio"

    leerUrl(url)

}

fun eliminar(modelo:String){

    val url = "http://iesayala.ddns.net/alamsete/deletecoche.php/?modelo=$modelo"

    leerUrl(url)

}


fun leerUrl(urlString:String){
    GlobalScope.launch(Dispatchers.IO)   {
        val response = try {
            URL(urlString)
                .openStream()
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            "Error with ${e.message}."
            Log.d("io", e.message.toString())
        } catch (e: Exception) {
            "Error with ${e.message}."
            Log.d("io", e.message.toString())
        }
    }

    return
}


@Composable
fun cargarJson(): CocheInfo {

    var coches by rememberSaveable { mutableStateOf(CocheInfo()) }
    val coche = CocheInstance.cocheInterface.cocheInformation()

    coche.enqueue(object : Callback<CocheInfo> {
        override fun onResponse(
            call: Call<CocheInfo>,
            response: Response<CocheInfo>
        ) {
            val userInfo: CocheInfo? = response.body()
            if (userInfo != null) {
                coches = userInfo
            }
        }

        override fun onFailure(call: Call<CocheInfo>, t: Throwable)
        {
            //Error
        }

    })
    println(coches)
    return coches
}


@Composable
fun Llamada() {
    var lista= cargarJson()

    LazyColumn()
    {

        items(lista) { usu ->
            Box(modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 16.dp))){
                Row(modifier = Modifier.background(color = Color.DarkGray)) {
                    Column (modifier = Modifier.weight(1f)) {
                        Text(text = usu.marca, color = Color.White)
                    }
                    Column (modifier = Modifier.weight(1f)) {
                        Text(text = usu.modelo, color = Color.White)
                    }
                    Column (modifier = Modifier.weight(1f)) {
                        Text(text = usu.precio, color = Color.White)
                    }
                }
            }

        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppFin1TrimestreAntonioLuisTheme {
        PantallaPrincipal()
    }
}