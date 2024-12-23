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
            val basics = fields.filter { it.type == BASIC && it.owner == ME }

            val mostRight = basics.filter { it.type == BASIC }.sortedByDescending { it.pos.x }.firstOrNull()
            val newest = fields.filter { it.owner == ME }.sortedByDescending { it.organId }.firstOrNull()

            val from = newest ?: mostRight ?: root
            val proteins = fields.filter { it.type == PROTEIN_A }.sortedBy { root.pos.vector(it.pos).distance() }

            val target = proteins.firstOrNull()
            var premove = true

             when(basics.size) {
                 0 -> println("GROW ${from.organId} ${root.pos.x} ${root.pos.y+1} BASIC $from")
                 1 -> println("GROW ${from.organId} ${root.pos.x} ${root.pos.y+2} BASIC $from")
                 2 -> println("GROW ${from.organId} ${root.pos.x} ${root.pos.y+3} BASIC $from")
                 3 -> println("GROW ${from.organId} ${root.pos.x+1} ${root.pos.y+3} BASIC $from")
                 else -> premove = false
             }
             if(!premove) {
                 val check = from.pos.moveRight(2)
                 val owner = check.owner(fields)
                 if(owner == HIM) {
                     val t = from.pos.moveRight(1)
                     println("GROW ${from.organId} ${t.x} ${t.y} TENTACLE E")
                 } else {
                     println("GROW ${from.organId} 15 5 BASIC $owner $check")
                 }
             }
        }
    }
}