package minesweeper

import kotlin.random.Random

const val MINE = 'X'
const val CLEAR = '/'
const val MARK = '*'
const val FOG = '.'
// initCoord: Coord

class Board(private val size: Int, val mines: Int, private val random: Random = Random.Default) {

    // todo solve the init on free issue
    private var haveStepped: Boolean = false
    private val grid: List<MutableList<Char>> = List(size) { MutableList(size) { '/' } }

    private fun initGrid(coord: Coord) {
        //Lay mines
        generateSequence { Coord(random.nextInt(size), random.nextInt(size)) }
            .filter { it != coord }
            .distinct()
            .take(mines)
            .forEach {
                grid[it] = MINE
            }
        grid.indices.flatMap { y -> grid.indices.map { x -> Coord(y, x) } }
            .forEach { coord ->
                countNeighbourMines(coord)
                    ?.digitToChar()
                    ?.let { grid[coord] = it }
            }
    }


    val fogGrid: List<MutableList<Char>> = List(grid.size) { MutableList(grid.size) { '.' } }

    // todo print String needs work if we're to work with sizes above 9
    private fun listToString(list: List<MutableList<Char>>): String = list.withIndex()
        .joinToString(
            "\n",
            " │${(1..list.size).joinToString("")}│\n" +
                    "—│${"—".repeat(list.size)}│\n",
            "\n—│${"—".repeat(list.size)}│",
        ) {
            it.value.joinToString(
                if (size > 9) " " else "",
                "${if (size > 9 && it.index < 9) "0" else ""}${it.index + 1}│",
                "│"
            )
        }

    // todo test and impl optimised version
    private fun countNeighbourMines(coord: Coord): Int? {
        if (grid[coord.y][coord.x] == 'X') return null
        //verbose impl
        return getNeighbours(coord) // make sure the coords are in the grid
            .map { grid[it] } // map them to chars
            .count { it == MINE } // count the mines
            .takeIf { it != 0 } // don't take zero

    }


    fun printGrid() {
        println(listToString(fogGrid))
    }

    /**
     * mark/ mine command
     * Marks the cell at the specified coordinates with a flag.
     *
     * @param coord the coordinates of the cell to be marked
     */
    fun mark(coord: Coord) {
        val cell: Char = fogGrid[coord]
        when (cell) {
//            in '0'..'9' -> println("There is a number here!") // if there's a number, bad input
            MARK -> fogGrid[coord] = FOG // if there's a mark, unmark it
            else -> fogGrid[coord] = MARK // else we must want to mark it
        }
    }

    /**
     * Clears a cell and its neighbouring cells on the game board.
     *
     * This function is used to explore a cell and its neighbours to check if they are free or have mines.
     * It updates the game board accordingly.
     */
    fun clear(coord: Coord) {
        if (haveStepped) return
        when (grid[coord]) {
            MINE -> {
                haveStepped = true // use some kind of board.revealMine // fail route
                return
            }

            CLEAR -> getNeighbours(coord).forEach(::clear)

            in '1'..'9' -> {
                fogGrid[coord] = grid[coord]
            }
        }


    }

    /**
     * Returns a list of neighbouring coordinates for a given coordinate.
     *
     * This function calculates the coordinates of the neighbours of the given coordinate
     * by iterating over a range of -1 to 1 for both x and y coordinates.
     * It then maps each pair
     *  of coordinates to a new coordinate by adding the corresponding values to the given coordinate.
     *  The resulting coordinates are filtered based on whether they are within the bounds
     *  of the grid.
     *
     * @param coord the coordinate for which to find neighbours
     * @return the list of neighbouring coordinates
     */
    private fun getNeighbours(coord: Coord): List<Coord> {
        return (-1..1).flatMap { xo -> (-1..1).map { yo -> xo to yo } }
            .map { Coord(it.first + coord.y, it.second + coord.x) }
            .filter { it.y in grid.indices && it.x in grid.indices }
            .filter { it != coord }
    }

    inner class Status() {
        // Stored
        val falseNegatives: Int // missed mines
        val falsePositives: Int // falsely flagged as mine

        // inferred
        val correct: Int
            get() = mines - falseNegatives //correctly flagged as mine
        val hasWon: Boolean
            get() = mines == correct && falsePositives == 0

        val initialised: Boolean
            get() = grid.flatten().contains(MINE)

        init {
            grid.flatten().zip(fogGrid.flatten()).filter { it.first == 'X' || it.second == '*' }.let { list ->
                falseNegatives = list.count { it.first == 'X' && it.second != '*' }
                    .also { falsePositives = list.count { it.first != 'X' && it.second == '*' } }
            }
        }

    }
}