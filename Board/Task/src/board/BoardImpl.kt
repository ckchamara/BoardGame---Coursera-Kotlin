package board

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)


open class SquareBoardImpl(override val width: Int) : SquareBoard {

    private val arrayList = ArrayList<Cell>(width * width)

    init {
        (1..width).forEach { it ->
            (1..width).forEach { itr ->
                arrayList.add(Cell(it, itr))
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? = getAllCells().find { it -> it == Cell(i, j) }


    override fun getCell(i: Int, j: Int): Cell {
        return getAllCells().find { it -> it == Cell(i, j) } ?: throw IllegalArgumentException("Invalid Cell ($i, $j)")
    }

    override fun getAllCells(): Collection<Cell> {
        return arrayList
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return jRange.filter { it -> it <= width }.map { itr -> getCell(i, itr) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return iRange.filter { it -> it <= width }.map { itr -> getCell(itr, j) }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            Direction.UP -> getCellOrNull(this.i - 1, this.j)
            Direction.DOWN -> getCellOrNull(this.i + 1, this.j)
            Direction.LEFT -> getCellOrNull(this.i, this.j - 1)
            Direction.RIGHT -> getCellOrNull(this.i, this.j + 1)
        }
    }

}

//class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {

class GameBoardImpl<T>(width: Int): GameBoard<T>, SquareBoardImpl(width) {

    private val boardCells = getAllCells().map { it to null as T? }.toMap().toMutableMap()

    override fun get(cell: Cell): T? = boardCells[cell]

    override fun set(cell: Cell, value: T?) {
        boardCells[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
            return boardCells.filterValues(predicate).keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return boardCells.filter { predicate(it.value) }.keys.first()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return boardCells.any { predicate(it.value) }
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return boardCells.all { predicate(it.value) }
    }

}
