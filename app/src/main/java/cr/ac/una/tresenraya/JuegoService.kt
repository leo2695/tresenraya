package cr.ac.una.tresenraya

class JuegoService {

    var matriz = Array (3) { CharArray(3) }
    var figura: Char = 'X'

    fun jugada(x: Int, y:Int): String {
        matriz[x][y] = figura
        if (figura == 'X') figura = 'O' else figura = 'X'
        return "Turno de "+ figura
    }

    fun inicializar(){
        matriz[0][0] = ' '
        matriz[0][1] = ' '
        matriz[0][2] = ' '
        matriz[1][0] = ' '
        matriz[1][1] = ' '
        matriz[1][2] = ' '
        matriz[2][0] = ' '
        matriz[2][1] = ' '
        matriz[2][2] = ' '
    }
}