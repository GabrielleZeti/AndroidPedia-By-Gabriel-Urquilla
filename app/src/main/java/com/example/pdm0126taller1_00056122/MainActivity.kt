package com.example.pdm0126taller1_00056122

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
                AppAndroidPedia()
            }
        }
    }
}

@Composable
fun AppAndroidPedia() {
    var pantallaActual by remember { mutableStateOf("Bienvenida") }
    var indicePregunta by remember { mutableStateOf(0) }
    var puntaje by remember { mutableStateOf(0) }

    when (pantallaActual) {
        "Bienvenida" -> {
            PantallaInicio(
                onEmpezar = {
                    indicePregunta = 0
                    puntaje = 0
                    pantallaActual = "Quiz"
                }
            )
        }
        "Quiz" -> {
            PantallaCuestionario(
                pregunta = quizQuestions[indicePregunta],
                numero = indicePregunta + 1,
                total = quizQuestions.size,
                puntosActuales = puntaje,
                alResponder = { acerto ->
                    if (acerto) puntaje = puntaje + 1
                },
                alPresionarSiguiente = {
                    if (indicePregunta < quizQuestions.size - 1) {
                        indicePregunta = indicePregunta + 1
                    } else {
                        pantallaActual = "Resultado"
                    }
                }
            )
        }
        "Resultado" -> {
            PantallaFinal(
                puntosTotales = puntaje,
                maximo = quizQuestions.size,
                alReiniciar = {
                    pantallaActual = "Bienvenida"
                }
            )
        }
    }
}

@Composable
fun PantallaInicio(onEmpezar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "AndroidPedia", fontSize = 35.sp, color = Color.Black)
        Text(text = "Historia y Evolucion", fontSize = 20.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(50.dp))

        Text(text = "Estudiante:Gabriel Urquilla", color = Color.Black)
        Text(text = "Carnet: 00056122", color = Color.Black)

        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = onEmpezar, modifier = Modifier.fillMaxWidth()) {
            Text("Comenzar Taller")
        }
    }
}

@Composable
fun PantallaCuestionario(
    pregunta: Question,
    numero: Int,
    total: Int,
    puntosActuales: Int,
    alResponder: (Boolean) -> Unit,
    alPresionarSiguiente: () -> Unit
) {
    var seleccion by remember(pregunta.id) { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Pregunta $numero de $total", color = Color.Black)
            Text(text = "Puntos: $puntosActuales", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(25.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = pregunta.question,
                modifier = Modifier.padding(20.dp),
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Ciclo forEach para los botones
        pregunta.options.forEach { opcion ->
            val yaRespondio = seleccion != ""
            val esLaCorrecta = opcion == pregunta.correctAnswer
            val laElegida = opcion == seleccion

            // Logica de colores con nombres de colores basicos
            var colorBoton = Color.Blue
            if (yaRespondio) {
                if (esLaCorrecta) {
                    colorBoton = Color.Green
                } else if (laElegida) {
                    colorBoton = Color.Red
                } else {
                    colorBoton = Color.Gray
                }
            }

            Button(
                onClick = {
                    if (seleccion == "") {
                        seleccion = opcion
                        alResponder(esLaCorrecta)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorBoton,
                    contentColor = Color.White,
                    disabledContainerColor = colorBoton,
                    disabledContentColor = Color.White
                ),
                enabled = !yaRespondio
            ) {
                Text(text = opcion)
            }
        }

        if (seleccion != "") {
            Spacer(modifier = Modifier.height(25.dp))
            Text(text = "Dato curioso: " + pregunta.funFact, color = Color.Black)
            Spacer(modifier = Modifier.height(25.dp))
            Button(onClick = alPresionarSiguiente, modifier = Modifier.fillMaxWidth()) {
                if (numero == total) {
                    Text("Ver Resultado")
                } else {
                    Text("Siguiente Pregunta")
                }
            }
        }
    }
}

@Composable
fun PantallaFinal(puntosTotales: Int, maximo: Int, alReiniciar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Puntaje Final: $puntosTotales de $maximo", fontSize = 30.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(20.dp))

        val mensaje = when (puntosTotales) {
            3 -> "Excelente trabajo experto"
            2 -> "Muy bien hecho"
            else -> "Sigue intentandolo"
        }

        Text(text = mensaje, fontSize = 20.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = alReiniciar, modifier = Modifier.fillMaxWidth()) {
            Text("Reiniciar Quiz")
        }
    }
}