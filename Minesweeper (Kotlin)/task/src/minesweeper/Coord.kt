package minesweeper

data class Coord(private val coordY: Int, private val coordX: Int, private val userInpt: Boolean = false) {

    constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)

    val y: Int
    val x: Int
    val userY
        get() = coordY + 1
    val userX
        get() = coordX + 1


    init {
        if (userInpt) {
            this.y = coordY - 1
            this.x = coordX - 1
        } else {
            this.y = coordY
            this.x = coordX
        }
    }
}
