package net.igneo.icv.init

import kotlin.math.abs
import kotlin.math.sqrt

object TrigMath {
    private const val SECOND_ORDER_COEFFICIENT = 1.0911122665310369E-9

    private fun quasiCos2(x: Double): Double {
        return 1.0 - SECOND_ORDER_COEFFICIENT * x * x
    }

    fun sincos(intAngle: Int): DoubleArray {
        val shifter = (intAngle xor (intAngle shl 1)) and 0xC000
        val x = (((intAngle + shifter) shl 17) shr 16).toDouble()

        var cosx = quasiCos2(x)
        var sinx = sqrt(1.0 - cosx * cosx)

        if ((shifter and 0x4000) != 0) {
            val temp = cosx
            cosx = sinx
            sinx = temp
        }

        if (intAngle < 0) {
            sinx = -sinx
        }

        if ((shifter and 0x8000) != 0) {
            cosx = -cosx
        }

        return doubleArrayOf(sinx, cosx)
    }

    fun tan(intAngle: Int): Double {
        val result = sincos(intAngle)
        val sin = result[0]
        val cos = result[1]

        if (abs(cos) < 1e-10) {
            return if (sin > 0) Double.POSITIVE_INFINITY else Double.NEGATIVE_INFINITY
        }

        return sin / cos
    }

    fun radiansToInternalAngle(radians: Double): Int {
        return ((radians % (2 * Math.PI)) * 32768 / (2 * Math.PI)).toInt() and 0xFFFF
    }
}