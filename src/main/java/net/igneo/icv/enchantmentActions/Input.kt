package net.igneo.icv.enchantmentActions

enum class Input {
    FORWARD,
    FORWARD_RIGHT,
    RIGHT,
    BACKWARD_RIGHT,
    BACKWARD,
    BACKWARD_LEFT,
    LEFT,
    FORWARD_LEFT;

    companion object {
        @JvmStatic
        fun getInput(type: Int): Input {
            return when (type) {
                (1) -> FORWARD_RIGHT
                (2) -> RIGHT
                (3) -> BACKWARD_RIGHT
                (4) -> BACKWARD
                (5) -> BACKWARD_LEFT
                (6) -> LEFT
                (7) -> FORWARD_LEFT
                else -> FORWARD
            }
        }

        @JvmStatic
        fun flattenInput(input: Input): Input {
            return when (input) {
                FORWARD_RIGHT, BACKWARD_RIGHT -> RIGHT
                FORWARD_LEFT, BACKWARD_LEFT -> LEFT
                else -> input
            }
        }

        @JvmStatic
        fun getRotation(input: Input): Int {
            return when (input) {
                FORWARD -> 0
                FORWARD_RIGHT -> 45
                RIGHT -> 90
                BACKWARD_RIGHT -> 135
                BACKWARD -> 180
                BACKWARD_LEFT -> 225
                LEFT -> 270
                FORWARD_LEFT -> 315
            }
        }
    }
}
