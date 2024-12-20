import Owner.*
import Type.*
import java.util.*
import kotlin.math.sqrt

/**
 * Grow and multiply your organisms to end up larger than your opponent.
 **/
fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val width = input.nextInt() // columns in the game grid
    val height = input.nextInt() // rows in the game grid

    // game loop
    while (true) {
        val fields = mutableListOf<Field>()
        val entityCount = input.nextInt()
        for (i in 0 until entityCount) {
            val x = input.nextInt()
            val y = input.nextInt() // grid coordinate
            val type = Type.fromString(input.next()) // WALL, ROOT, BASIC, TENTACLE, HARVESTER, SPORER, A, B, C, D
            val owner = Owner.fromInt(input.nextInt()) // 1 if your organ, 0 if enemy organ, -1 if neither
            val organId = input.nextInt() // id of this entity if it's an organ, 0 otherwise
            val organDir = input.next() // N,E,S,W or X if not an organ
            val organParentId = input.nextInt()
            val organRootId = input.nextInt()

            fields.add(
                Field(
                    pos = Point(x, y),
                    type = type,
                    owner = owner,
                    organId = organId,
                    organDir = organDir,
                    organParentId = organParentId,
                    organRootId = organRootId
                )
            )
        }

        val myA = input.nextInt()
        val myB = input.nextInt()
        val myC = input.nextInt()
        val myD = input.nextInt() // your protein stock
        val oppA = input.nextInt()
        val oppB = input.nextInt()
        val oppC = input.nextInt()
        val oppD = input.nextInt() // opponent's protein stock

        val requiredActionsCount = input.nextInt() // your number of organisms, output an action for each one in any order
        for (i in 0 until requiredActionsCount) {

            val root = fields.first { it.type == ROOT && it.owner == ME }
            val harvesters = fields.filter { it.type == HARVESTER && it.owner == ME }

            val mostRight = fields.filter { it.type == BASIC }.sortedByDescending { it.pos.x }.firstOrNull()

            val from = mostRight ?: root
            val proteins = fields.filter { it.type == PROTEIN_A }.sortedBy { from.pos.vector(it.pos).distance() }

//            proteins.first().let {
//                val x = it.pos.x
//                val y = it.pos.y
//                println("GROW 1 $x $y BASIC $from")
//            }

//            val targetProtein = proteins.firstOrNull { it.pos.y == 1 || it.pos.y == 3}
            val targetProtein = proteins.firstOrNull()
            targetProtein?.let {target ->
                val x = target.pos.x
                val y = target.pos.y

                val distance = from.pos.vector(targetProtein.pos).distance().toInt()
                if(distance > 1 && harvesters.isEmpty() ) {
                    println("GROW 1 $x $y BASIC $distance")
                } else {
                    val fromId = from.organId
                    if (target.pos.y == 1 || target.pos.y == 2) {
                        val yh = y + 1
                        println("GROW $fromId $x $yh HARVESTER N")
                    }
                    if (target.pos.y == 3) {
                        val yh = y - 1
                        println("GROW $fromId $x $yh HARVESTER S")
                    }
                }
            } ?: run {
                println("GROW 1 16 4 BASIC $from")
            }
        }
    }
}