package minesweeper

class Coord(y: Int, x: Int, userInpt: Boolean = false) {

    val y: Int
    val x: Int
    val userY
        get() = y + 1
    val userX
        get() = x + 1


    init {
        if (userInpt) {
            this.y = y - 1
            this.x = x - 1
        } else {
            this.y = y
            this.x = x
        }
    }
}
