package net.igneo.icv.enchantment

enum class EnchantType {
    WEAPON,
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS,
    BOW,
    CROSSBOW,
    SHIELD,
    TRIDENT;

    companion object {
        @Suppress("UNREACHABLE_CODE")
        @JvmStatic
        fun applicableSlot(type: EnchantType, slot: Int): Boolean {
            return when (type) {
                BOOTS -> slot == 0
                LEGGINGS -> slot == 1
                CHESTPLATE -> slot == 2
                HELMET -> slot == 3
                WEAPON, TRIDENT -> slot == 4 || slot == 5

                BOW -> TODO()
                CROSSBOW -> TODO()
                SHIELD -> TODO()
            }
            return false
        }
    }
}
