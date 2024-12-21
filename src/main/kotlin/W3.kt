import Owner.*
import Type.*
import java.util.*
import kotlin.math.sqrt

/**
 * Grow and multiply your organisms to end up larger than your opponent.
 * Can use harvester to get proteins
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

            val from = mostRight ?: root
            val proteins = fields.filter { it.type == PROTEIN_A }.sortedBy { root.pos.vector(it.pos).distance() }

            val target = proteins.first()
            var premove = false
            val adjust = if(target.pos.y == root.pos.y) 1 else 0
            when(basics.size) {
                0 -> {
                    println("GROW ${from.organId} ${root.pos.x+adjust} ${target.pos.y} BASIC $from")
                    premove = true
                }
                1 -> {
                    println("GROW ${from.organId} ${root.pos.x+1+adjust} ${target.pos.y} BASIC $from")
                    premove = true
                }
            }
            if(!premove) {
                if (harvesters.isEmpty()) {
                    val x = target.pos.x
                    val y = target.pos.y

                    val nextStep = when(target.pos.y) {
                        1 -> Point(x, y+1)
                        2 -> Point(x-1, y)
                        3 -> Point(x, y-1)
                        else -> throw IllegalStateException("Head exploded $target")
                    }

                    val oneLeft = target.pos.moveLeft(2)
                    if(basics.any { it.pos == oneLeft }) {
                        when(nextStep.y) {
                            1 -> println("GROW ${from.organId} ${nextStep.x-1} ${nextStep.y} HARVESTER E $from")
                            2 -> println("GROW ${from.organId} ${nextStep.x-1} ${nextStep.y} HARVESTER E $from")
                            3 -> println("GROW ${from.organId} ${nextStep.x-1} ${nextStep.y} HARVESTER E $from")
                        }
                    } else {
                        println("GROW ${from.organId} ${nextStep.x} ${nextStep.y} BASIC $from")
                    }
                } else {
                    val next = when(target.pos.y) {
                        1 -> Point(target.pos.x, target.pos.y+1)
                        2 -> Point(target.pos.x, target.pos.y+1)
                        3 -> Point(target.pos.x, target.pos.y-1)
                        else -> throw IllegalStateException("Head exploded $target")
                    }
                    if(basics.any { it.pos == next }) {
                        println("GROW 1 6 6 BASIC $from")
                    } else {
                        println("GROW 1 ${next.x} ${next.y} BASIC $from")
                    }
                }
            }
        }
    }
}