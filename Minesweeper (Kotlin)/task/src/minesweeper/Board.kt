package minesweeper

class Board(size: Int, val mines: Int) {


    val grid: List<MutableList<Char>> = List(size) { MutableList(size) { '.' } }
        .also { grid ->
            generateSequence { grid.indices.random() to grid.indices.random() }
                .distinct()
                .take(mines)
                .forEach { (y, x) ->
                    grid[y][x] = 'X'
                }
        }
    val hintGrid: List<MutableList<Char>> = List(grid.size) { MutableList(grid.size) { '.' } }
        .also { hintGrid ->
            hintGrid.indices.flatMap { y -> hintGrid.indices.map { x -> y to x } }
                .forEach { coord ->
                    countNeighbourMines(coord.first, coord.second)
                        ?.digitToChar()
                        ?.let { hintGrid[coord.first][coord.second] = it }
                }
        }

//    private fun getHintGrid(): List<MutableList<Char>> {
//        hintGrid.indices.flatMap { y -> hintGrid.indices.map { x -> y to x } }
//            .forEach { coord ->
//                countNeighbourMines(coord.first, coord.second)
//                    ?.digitToChar()
//                    ?.let { hintGrid[coord.first][coord.second] = it }
//            }
//        return hintGrid
//
//
//    }

    // my original before intellij offered a refactor
    // override fun toString(): String = grid.map { it.joinToString() }.joinToString("\n")
//    override fun toString(): String = grid.joinToString("\n") { it.joinToString("") }
    private fun listToString(list: List<MutableList<Char>>): String = list.withIndex()
        .joinToString(
            "\n",
            " │123456789│\n" +
                    "—│—————————│\n",
            "\n—│—————————│",
        ) {
            it.value.joinToString(
                "",
                "${it.index + 1}│",
                "│"
            )
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
        println(listToString(hintGrid))
    }

    fun printGrid(): Unit {
        println(listToString(grid))
    }
    // this is wrong impl we need a mark grid
    // or we can gen a list of mine coords, and mark coords, and intersect them
    fun setDel(coord: Coord) {
        val cell: Char = get(hintGrid, coord)
        when (cell) {
            in '0'..'9' -> println("There is a number here!") // if theres a number, bad input
            '*' -> set(hintGrid, coord, '.') // if theres a mark, unmark it
            else -> set(hintGrid, coord, '*') // else we must want to mark it
        }
    }

    private fun set(grid: List<MutableList<Char>>, coord: Coord, c: Char) = c.also { grid[coord.y][coord.x] = it }

    private fun get(grid: List<MutableList<Char>>,coord: Coord): Char = grid[coord.y][coord.x]
    private fun get(y: Int, x: Int): Char = grid[y][x]

    inner class Status() {
        // Stored
        val falseNegatives: Int // missed mines
        val falsePositives: Int // falsely flagged as mine

        // inferred
        val correct: Int
            get() = mines - falseNegatives //correctly flagged as mine
        val hasWon: Boolean
            get() = mines == correct && falsePositives == 0

        init {
            grid.flatten().zip(hintGrid.flatten()).filter { it.first == 'X' || it.second == '*' }.let { list ->
                falseNegatives = list.count { it.first == 'X' && it.second != '*' }
                    .also { falsePositives = list.count { it.first != 'X' && it.second == '*' } }
            }
        }

    }
}