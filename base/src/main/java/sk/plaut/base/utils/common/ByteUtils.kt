package sk.plaut.base.utils.common

import timber.log.Timber
import java.math.BigInteger

object ByteUtils {

    fun bytesToHexString(src: ByteArray): String? {
        if (isNullOrEmpty(src)) {
            return null
        }
        val sb = StringBuilder()
        for (b in src) {
            sb.append(String.format("%02X ", b))
        }
        return sb.toString()
    }

    fun intToBytes(value: Int): ByteArray {
        val valueAsBytes = ByteArray(2)

        valueAsBytes[0] = (value shr 8 and 0xFF).toByte()
        valueAsBytes[1] = (value and 0xFF).toByte()

        return valueAsBytes
    }

    fun bytesToInt(bytes: ByteArray): Int {
        return BigInteger(bytes).toInt()
    }

    fun logByteData(tag: String, data: ByteArray) {
        val dataString = bytesToHexString(data)
        if (dataString != null) {
            Timber.tag(tag).e(dataString)
        }
    }

    private fun isNullOrEmpty(array: ByteArray?): Boolean {
        if (array == null) {
            return true
        }
        val length = array.size
        for (i in 0 until length) {
            if (array[i].toInt() != 0) {
                return false
            }
        }
        return true
    }
}
