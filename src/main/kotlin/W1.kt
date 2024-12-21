import Owner.*
import Type.*
import java.util.*
import kotlin.math.sqrt

/**
 * Grow and multiply your organisms to end up larger than your opponent.
 **/
fun main(args: Array<String>) {
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

        val requiredActionsCount =
            input.nextInt() // your number of organisms, output an action for each one in any order
        for (i in 0 until requiredActionsCount) {

            val root = fields.first { it.type == ROOT && it.owner == ME }
            val roots = fields.filter { it.type == ROOT && it.owner == ME }.sortedBy { it.organId }
            val harvesters = fields.filter { it.type == HARVESTER && it.owner == ME }
            val tentacles = fields.filter { it.type == TENTACLE && it.owner == ME }
            val basics = fields.filter { it.type == BASIC && it.owner == ME }
            val sporers = fields.filter { it.type == SPORER && it.owner == ME }

            val mostRight = basics.filter { it.type == BASIC }.sortedByDescending { it.pos.x }.firstOrNull()
            val newest = fields.filter { it.owner == ME }.sortedByDescending { it.organId }.firstOrNull()

            val from = newest ?: mostRight ?: root
            val proteins = fields.filter { it.type == PROTEIN_A }.sortedBy { root.pos.vector(it.pos).distance() }

            val target = proteins.firstOrNull()

            if (sporers.isEmpty()) {
                target?.let {
                    when (target.pos.y) {
                        1 -> println("GROW ${from.organId} ${from.pos.x} ${from.pos.y - 1} SPORER E $from")
                        2 -> println("GROW ${from.organId} ${from.pos.x + 1} ${from.pos.y} SPORER E $from")
                        3 -> println("GROW ${from.organId} ${from.pos.x} ${from.pos.y + 1} SPORER E $from")
                        else -> throw IllegalStateException("Head exploded $target")
                    }

                }
            } else if (roots.size == 1) {
                target?.let {
                    val sporer = sporers.first()
                    println("SPORE ${sporer.organId} ${target.pos.x-2} ${target.pos.y}")
                }
            } else if (roots.size == 2) {
                if(harvesters.isEmpty()){
                    val root = roots[1]
                    println("GROW ${root.organId} ${root.pos.x+1} ${root.pos.y} HARVESTER E")
                    println("GROW ${roots[0].organId} 16 1 BASIC")
                } else {
                    println("GROW ${roots[0].organId} 16 1 BASIC")
                    println("GROW ${roots[1].organId} 2 3 BASIC")
                }
            }
        }
    }
}