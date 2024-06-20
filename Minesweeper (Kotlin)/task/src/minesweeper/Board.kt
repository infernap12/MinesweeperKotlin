package minesweeper

class Board(size: Int, mines: Int) {


    private val grid: List<MutableList<Char>> = List(size) { MutableList(size) { '.' } }
        .also { grid ->
            generateSequence { grid.indices.random() to grid.indices.random() }
                .distinct()
                .take(mines)
                .forEach { (y, x) ->
                    grid[y][x] = 'x'
                }
        }

    // my original before intellij offered a refactor
    // override fun toString(): String = grid.map { it.joinToString() }.joinToString("\n")
    override fun toString(): String = grid.joinToString("\n") { it.joinToString("") }

}