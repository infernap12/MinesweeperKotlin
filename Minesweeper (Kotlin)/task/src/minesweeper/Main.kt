package minesweeper



fun main() {
    println("How many mines do you want on the field?")
    val board = Board(9, readInt())
//    board.printGrid()
//    println()
    board.printGrid()
    while (!board.Status().hasWon) {
        println("Set/delete mine marks (x and y coordinates):")
        board.mark(readCoord())
        board.printGrid()
    }
    println("Congratulations! You found all the mines!")
}

fun readInt(): Int {
    while (true) {
        val intput = readln().toIntOrNull()
        if (intput == null) {
            println("A number please")
        } else return intput
    }
}

fun readCoord(): Coord {
    while (true) {
        val coord = readln().split(" ")
            .mapNotNull { it.toIntOrNull() }
            .let { if (it.size == 2) it else null }
            .let {
                if (it != null) {
                    Coord(it.last(), it.first(), true)
                } else null
            }
        if (coord == null) {
            println("Numbers please")
        } else return coord
    }
}

operator fun <T> List<MutableList<T>>.get(coord: Coord): T {
    return this[coord.y][coord.x]
}

operator fun <T> List<MutableList<T>>.set(coord: Coord, value: T) {
    this[coord.y][coord.x] = value
}