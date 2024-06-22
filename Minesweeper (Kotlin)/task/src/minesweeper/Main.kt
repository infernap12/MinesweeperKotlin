package minesweeper


fun main() {
    println("How many mines do you want on the field?")
    val board = Board(9, readInt())
//    board.printGrid()
//    println()
    board.printHint()
}

fun readInt(): Int {
    while (true) {
        val intput = readln().toIntOrNull()
        if (intput == null) {
            println("A number please")
        } else return intput
    }
}
