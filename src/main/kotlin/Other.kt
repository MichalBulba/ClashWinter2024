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
}

enum class Type {
    WALL,
    ROOT,
    BASIC,
    HARVESTER,
    PROTEIN_A;

    companion object {
        fun fromString(value: String) = when (value) {
            "WALL" -> WALL
            "ROOT" -> ROOT
            "BASIC" -> BASIC
            "HARVESTER" -> HARVESTER
            "A" -> PROTEIN_A
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