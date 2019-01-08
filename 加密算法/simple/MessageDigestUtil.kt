package simple2

import java.security.MessageDigest
import java.security.Signature

object MessageDigestUtil {
    /**
     * MD5加密
     */
    fun MD5(input: String): String {
        val mDigest = MessageDigest.getInstance("MD5")
        val digestStr = mDigest.digest(input.toByteArray())
        println("加密后数据长度：" + digestStr.size)
        val result = toHex(digestStr)
        println("转为16进制后长度：" + result.toByteArray().size)
        return result
    }

    /**
     *SHA-1加密
     */
    fun SHA1(intput: String): String{
        val mDigest = MessageDigest.getInstance("SHA-1")
        val digestStr = mDigest.digest(intput.toByteArray())
        println("加密后数据长度："+digestStr.size)
        val result = toHex(digestStr)
        println("转为16进制后长度：" + result.toByteArray().size)
        return result
    }

    /**
     * SHA-256加密
     */
    fun SHA256(intput: String): String{
        val mDigest = MessageDigest.getInstance("SHA-256")
        val digestStr = mDigest.digest(intput.toByteArray())
        println("加密后数据长度："+digestStr.size)
        val result = toHex(digestStr)
        println("转为16进制后长度：" + result.toByteArray().size)
        return result
    }

    /**
     * 转成16进制
     */
    fun toHex(strArray: ByteArray): String {
        val result = with(StringBuilder()) {
            strArray.forEach {
                val value = it
                val hex = value.toInt() and (0xFF)
                val hexString = Integer.toHexString(hex)
                if (hexString.length == 1) {
                    append(0).append(hexString)
                } else {
                    append(hexString)
                }
            }
            toString()
        }
        return result
    }
}

fun main(args: Array<String>) {
    val input = "欢迎来到加密世界"
    val md5 = MessageDigestUtil.MD5(input)
    println(md5)
    println(MessageDigestUtil.SHA1(input))
    println(MessageDigestUtil.SHA256(input))
}