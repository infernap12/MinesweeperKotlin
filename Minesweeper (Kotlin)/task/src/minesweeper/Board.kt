package minesweeper

class Board(size: Int, mines: Int) {


    val grid: List<MutableList<Char>> = List(size) { MutableList(size) { '.' } }
        .also { grid ->
            generateSequence { grid.indices.random() to grid.indices.random() }
                .distinct()
                .take(mines)
                .forEach { (y, x) ->
                    grid[y][x] = 'X'
                }
        }

    // my original before intellij offered a refactor
    // override fun toString(): String = grid.map { it.joinToString() }.joinToString("\n")
//    override fun toString(): String = grid.joinToString("\n") { it.joinToString("") }
    private fun listToString(list: List<MutableList<Char>>): String = list.joinToString("\n") { it.joinToString("") }

    private fun getHintGrid(): List<MutableList<Char>> {
        val hintGrid = grid.map { it.toMutableList() }
        hintGrid.indices.flatMap { y -> hintGrid.indices.map { x -> y to x } }
            .forEach { coord ->
                countNeighbourMines(coord.first, coord.second)
                    ?.digitToChar()
                    ?.let { hintGrid[coord.first][coord.second] = it }
            }
        return hintGrid


    }

    private fun countNeighbourMines(y: Int, x: Int): Int? {
        if (grid[y][x] == 'X') return null
        //verbose impl
        return (-1..1).flatMap { xo -> (-1..1).map { yo -> xo to yo } }
            .map { Pair(it.first + y, it.second + x) } // get coords
            .filter { it.first in grid.indices && it.second in grid.indices } // make sure the coords are in the grid
            .map { grid[it.first][it.second] } // map them to chars
            .count { it == 'X' } // count the mines
            .takeIf { it != 0 } // don't take zero

    }

    fun printHint(): Unit {
        println(listToString(getHintGrid()))
    }

    fun printGrid(): Unit {
        println(listToString(grid))
    }
}