package simple2

import java.security.AlgorithmParameters
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object DESCipher {
    //    val transformations = "DES/ECB/PKCS5Padding"
    val transformations = "DES/CBC/PKCS5Padding"
    val algorithm = "DES"

    /**
     * DES加密
     */
    fun encrypt(input: String, password: String): String {
        //    1.创建cipher对象
        val cipher = Cipher.getInstance(transformations)
//        val skf = SecretKeyFactory.getInstance("DES")
//        val spec = DESKeySpec(password.toByteArray())
//        val key: Key = skf.generateSecret(spec)
        val keySpec = SecretKeySpec(password.toByteArray(), algorithm)
        //    2.初始化cipher
        val aps = IvParameterSpec(password.toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, aps)
        //    3.加密/解密
        val result = cipher.doFinal(input.toByteArray())
        return Base64.encode(result)
    }

    /**
     * DES解密
     */
    fun decrypt(input: String, password: String): String {
        val cipher = Cipher.getInstance(transformations)
//        val skf = SecretKeyFactory.getInstance("DES")
//        val spec = DESKeySpec(password.toByteArray())
        val keySpec = SecretKeySpec(password.toByteArray(), algorithm)
        //    2.初始化cipher
        val aps = IvParameterSpec(password.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, keySpec, aps)//cbc 模式需要添加额外的参数
        //    3.加密/解密
        val result = cipher.doFinal(Base64.decode(input))
        return String(result)
    }
}

fun main(args: Array<String>) {
    val input = "欢迎来到Kotlin世界！"
    val password = "12345678"
////    1.创建cipher对象
//    val cipher = Cipher.getInstance("DES")
////    2.初始化cipher
//    val skf = SecretKeyFactory.getInstance("DES")
//    val desKeySpec = DESKeySpec(password.toByteArray())
//    val key: Key = skf.generateSecret(desKeySpec)
//    cipher.init(Cipher.ENCRYPT_MODE, key)
////    3.加密/解密
//    val result = cipher.doFinal(input.toByteArray())
    val result = DESCipher.encrypt(input, password)
    println(result)
    val decrypt = DESCipher.decrypt(result, password)
    print(decrypt)
}