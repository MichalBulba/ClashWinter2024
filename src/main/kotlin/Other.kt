import kotlin.math.sqrt

data class Field(
    val pos: Point,
    val type: Type,
    val owner: Owner,
    val organId: Int,
    val organDir: String,
    val organParentId: Int,
    val organRootId: Int
)

data class Point(
    val x: Int,
    val y: Int
) {
    fun vector(p2: Point) = Point(p2.x - this.x, p2.y - this.y)

    fun distance() = sqrt((x * x + y * y).toDouble())

    fun moveLeft(left: Int) = Point(x - left, y)
    fun moveRight(right: Int) = Point(x + right, y)
    fun moveDown(down: Int) = Point(x, y + down)
}

fun Point.owner(fields: List<Field>) = fields.firstOrNull { it.pos == this }?.owner ?: Owner.NOONE

enum class Type {
    WALL,
    ROOT,
    BASIC,
    HARVESTER,
    TENTACLE,
    SPORER,
    PROTEIN_A,
    PROTEIN_B,
    PROTEIN_C,
    PROTEIN_D;

    companion object {
        fun fromString(value: String) = when (value) {
            "WALL" -> WALL
            "ROOT" -> ROOT
            "BASIC" -> BASIC
            "HARVESTER" -> HARVESTER
            "TENTACLE" -> TENTACLE
            "SPORER" -> SPORER
            "A" -> PROTEIN_A
            "B" -> PROTEIN_B
            "C" -> PROTEIN_C
            "D" -> PROTEIN_D
            else -> throw IllegalArgumentException("Unknown type: $value")
        }
    }
}

enum class Owner {
    ME, HIM, NOONE;
    companion object {
        fun fromInt(value: Int) = when (value) {
            1 -> ME
            0 -> HIM
            -1 -> NOONE
            else -> throw IllegalArgumentException("Unknown owner: $value")
        }
    }
}

enum class Direction {
    N, S, W, E
}