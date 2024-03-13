package cr.ac.una.tresenraya

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var boton: Button
    lateinit var pos1: ImageButton
    lateinit var pos2: ImageButton
    lateinit var pos3: ImageButton
    lateinit var pos4: ImageButton
    lateinit var pos5: ImageButton
    lateinit var pos6: ImageButton
    lateinit var pos7: ImageButton
    lateinit var pos8: ImageButton
    lateinit var pos9: ImageButton
    var juegoService = JuegoService()
    var tablero = Array(3) { IntArray(3) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boton = findViewById(R.id.boton)
        pos1 = findViewById(R.id.pos1)
        pos2 = findViewById(R.id.pos2)
        pos3 = findViewById(R.id.pos3)
        pos4 = findViewById(R.id.pos4)
        pos5 = findViewById(R.id.pos5)
        pos6 = findViewById(R.id.pos6)
        pos7 = findViewById(R.id.pos7)
        pos8 = findViewById(R.id.pos8)
        pos9 = findViewById(R.id.pos9)

        boton.setOnClickListener() {
            //enableDisableButton()
            reiniciarJuego()
        }
        pos1.setOnClickListener() { seleccionaFigura(it as ImageButton, 0, 0) }
        pos2.setOnClickListener() { seleccionaFigura(it as ImageButton, 0, 1) }
        pos3.setOnClickListener() { seleccionaFigura(it as ImageButton, 0, 2) }
        pos4.setOnClickListener() { seleccionaFigura(it as ImageButton, 1, 0) }
        pos5.setOnClickListener() { seleccionaFigura(it as ImageButton, 1, 1) }
        pos6.setOnClickListener() { seleccionaFigura(it as ImageButton, 1, 2) }
        pos7.setOnClickListener() { seleccionaFigura(it as ImageButton, 2, 0) }
        pos8.setOnClickListener() { seleccionaFigura(it as ImageButton, 2, 1) }
        pos9.setOnClickListener() { seleccionaFigura(it as ImageButton, 2, 2) }

        enableDisableButton()
    }

    private fun enableDisableButton() {
        pos1.isEnabled = !pos1.isEnabled
        pos2.isEnabled = !pos2.isEnabled
        pos3.isEnabled = !pos3.isEnabled
        pos4.isEnabled = !pos4.isEnabled
        pos5.isEnabled = !pos5.isEnabled
        pos6.isEnabled = !pos6.isEnabled
        pos7.isEnabled = !pos7.isEnabled
        pos8.isEnabled = !pos8.isEnabled
        pos9.isEnabled = !pos9.isEnabled
    }

    private fun seleccionaFigura(imageButton: ImageButton, fila: Int, columna: Int) {
        if (juegoService.figura == 'O') {
            imageButton.setImageResource(R.drawable.circulo)
            tablero[fila][columna] = 1
        } else {
            imageButton.setImageResource(R.drawable.cruz)
            tablero[fila][columna] = 2
        }
        imageButton.isEnabled = false

        val ganador = hayGanador()
        if (ganador != null) {
            muestraDialogo("¡Hay un ganador: $ganador!")

            // Obtener las celdas ganadoras
            val celdasGanadoras = obtenerCeldasGanadoras()

            // Cambiar las imágenes de las celdas ganadoras
            for ((filaGanadora, columnaGanadora) in celdasGanadoras) {
                val drawableResource = if (ganador == 'O') R.drawable.circulo_rojo else R.drawable.cruz_roja
                when (filaGanadora) {
                    0 -> when (columnaGanadora) {
                        0 -> pos1.setImageResource(drawableResource)
                        1 -> pos2.setImageResource(drawableResource)
                        2 -> pos3.setImageResource(drawableResource)
                    }
                    1 -> when (columnaGanadora) {
                        0 -> pos4.setImageResource(drawableResource)
                        1 -> pos5.setImageResource(drawableResource)
                        2 -> pos6.setImageResource(drawableResource)
                    }
                    2 -> when (columnaGanadora) {
                        0 -> pos7.setImageResource(drawableResource)
                        1 -> pos8.setImageResource(drawableResource)
                        2 -> pos9.setImageResource(drawableResource)
                    }
                }
            }
        } else if (tableroLleno()) {
            muestraDialogo("¡Empate!")
        } else {
            cambiarTurno()
        }
    }

    private fun obtenerCeldasGanadoras(): List<Pair<Int, Int>> {
        // Buscar filas ganadoras
        for (i in 0 until 3) {
            if (tablero[i][0] != 0 && tablero[i][0] == tablero[i][1] && tablero[i][0] == tablero[i][2]) {
                return listOf(Pair(i, 0), Pair(i, 1), Pair(i, 2))
            }
        }

        // Buscar columnas ganadoras
        for (i in 0 until 3) {
            if (tablero[0][i] != 0 && tablero[0][i] == tablero[1][i] && tablero[0][i] == tablero[2][i]) {
                return listOf(Pair(0, i), Pair(1, i), Pair(2, i))
            }
        }

        // Buscar diagonales ganadoras
        if (tablero[0][0] != 0 && tablero[0][0] == tablero[1][1] && tablero[0][0] == tablero[2][2]) {
            return listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2))
        }
        if (tablero[0][2] != 0 && tablero[0][2] == tablero[1][1] && tablero[0][2] == tablero[2][0]) {
            return listOf(Pair(0, 2), Pair(1, 1), Pair(2, 0))
        }

        return emptyList()
    }

    private fun muestraDialogo(mensaje: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage("Aviso")
            .setTitle(mensaje)
            .setPositiveButton("¡Entendido!") { dialog, which ->
                // Do something.
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun hayGanador(): Char? {
        // Verificar filas
        for (i in 0 until 3) {
            if (tablero[i][0] != 0 && tablero[i][0] == tablero[i][1] && tablero[i][0] == tablero[i][2]) {
                return if (tablero[i][0] == 1) 'O' else 'X'
            }
        }

        // Verificar columnas
        for (i in 0 until 3) {
            if (tablero[0][i] != 0 && tablero[0][i] == tablero[1][i] && tablero[0][i] == tablero[2][i]) {
                return if (tablero[0][i] == 1) 'O' else 'X'
            }
        }

        // Verificar diagonales
        if (tablero[0][0] != 0 && tablero[0][0] == tablero[1][1] && tablero[0][0] == tablero[2][2]) {
            return if (tablero[0][0] == 1) 'O' else 'X'
        }
        if (tablero[0][2] != 0 && tablero[0][2] == tablero[1][1] && tablero[0][2] == tablero[2][0]) {
            return if (tablero[0][2] == 1) 'O' else 'X'
        }

        return null
    }
    private fun cambiarTurno() {
        if (juegoService.figura == 'X') {
            juegoService.figura = 'O'
        } else {
            juegoService.figura = 'X'
        }
        muestraDialogo("Turno: ${juegoService.figura}")
    }
    private fun tableroLleno(): Boolean {
        for (fila in tablero) {
            for (celda in fila) {
                if (celda == 0) {
                    return false // Si hay al menos una celda vacía, el tablero no está lleno
                }
            }
        }
        return true // Si no hay celdas vacías, el tablero está lleno
    }

    private fun reiniciarJuego() {
        // Limpiar el tablero
        tablero = Array(3) { IntArray(3) }

        // Restablecer las imágenes de las casillas
        val casillas = listOf(pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9)
        casillas.forEach { it.setImageResource(R.drawable.limpio) }

        // Habilitar las casillas y deshabilitar el botón de jugar
        enableDisableButton()
    }
}