package simple2

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESCipher {
    //    val transformations = "AES/ECB/PKCS5Padding"
    val transformations = "AES/CBC/PKCS5Padding"
    val algorithm = "AES"

    fun encrypt(input: String, password: String): String {
        //    1.创建cipher对象
        val cipher = Cipher.getInstance(transformations)
//    2.初始化cipher
        val keySpec = SecretKeySpec(password.toByteArray(), algorithm)
        val aps = IvParameterSpec(password.toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, aps)//CBC模式需要添加额外的参数
//    3.加密/解密
        val result = cipher.doFinal(input.toByteArray())
        return Base64.encode(result)
    }

    fun decrypt(input: String, password: String): String {
        //    1.创建cipher对象
        val cipher = Cipher.getInstance(transformations)
//    2.初始化cipher
        val keySpec = SecretKeySpec(password.toByteArray(), algorithm)
        val aps = IvParameterSpec(password.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, keySpec, aps)
//    3.加密/解密
        val result = cipher.doFinal(Base64.decode(input))
        return String(result)
    }
}

fun main(args: Array<String>) {
    val input = "AES欢迎来到Kotlin世界"
    val password = "1234567891234567"

    val encrypt = AESCipher.encrypt(input, password)
    val decrypt = AESCipher.decrypt(encrypt, password)
    println("加密：" + encrypt)
    println("解密：" + decrypt)

}