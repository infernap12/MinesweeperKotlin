package minesweeper


fun main() {
    println("How many mines do you want on the field?")
    println(Board(9, readInt()))
}

fun readInt(): Int {
    while (true) {
        val intput = readln().toIntOrNull()
        if (intput == null) {
            println("A number please")
        } else return intput
    }
}
