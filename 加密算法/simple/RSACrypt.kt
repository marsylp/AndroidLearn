package simple2

import java.io.ByteArrayOutputStream
import java.security.Key
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSACrypt {
    val transformation = "RSA"
    val ENCRYPT_MAX_SIZE = 117
    val DECRYPT_MAX_SIZE = 128
    /**
     * RSA加密
     */
    fun encrypt(input: String, key: Key): String {
        val inputArray = input.toByteArray()
        //    1.创建cipher对象
        val cipher = Cipher.getInstance(transformation)
        //    2.初始化cipher
        cipher.init(Cipher.ENCRYPT_MODE, key)
        //    3.加密/解密
        var offset = 0
        var encode: ByteArray
        val stream = ByteArrayOutputStream()
        while (inputArray.size - offset > 0) {
            if (inputArray.size - offset >= ENCRYPT_MAX_SIZE) {
                encode = cipher.doFinal(inputArray, offset, ENCRYPT_MAX_SIZE)
                offset += ENCRYPT_MAX_SIZE
            } else {
                encode = cipher.doFinal(inputArray, offset, inputArray.size - offset)
                offset = inputArray.size
            }
            stream.write(encode)
        }
        stream.close()
        return Base64.encode(stream.toByteArray())
    }

    /**
     * RSA解密
     */
    fun decrypt(input: String, key: Key): String {
        val inputArray = Base64.decode(input)
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.DECRYPT_MODE, key)
        var offset = 0
        var decrypt: ByteArray
        val stream = ByteArrayOutputStream()
        while (inputArray.size - offset > 0) {
            if (inputArray.size - offset >= DECRYPT_MAX_SIZE) {
                decrypt = cipher.doFinal(inputArray, offset, DECRYPT_MAX_SIZE)
                offset += DECRYPT_MAX_SIZE
            } else {
                decrypt = cipher.doFinal(inputArray, offset, inputArray.size - DECRYPT_MAX_SIZE)
                offset = inputArray.size
            }
            stream.write(decrypt)
        }
        stream.close()
        return String(stream.toByteArray())
    }
}

fun main(args: Array<String>) {
    val input = "欢迎来到Kotlin世界,欢迎来到Kotlin世界,欢迎来到Kotlin世界,欢迎来到Kotlin世界,欢迎来到Kotlin世界"
    //RSA保存密钥对
    val privateKeyStr = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKSJIuJtm7ItBAbF7llnygYN2JrPB7Gsu9h0ckcZO7qM0YcnU91bgWRb932OooP2bxFBKgkuhGBE87IfHTRLpTJlgx98UxEi0/37kKfvZcUon8qW5vKI+5dQ9HJzkzF7y9+XiC019j6W0M5sMJOF/CKfpQ7QAS+OZLbazuZZVPVjAgMBAAECgYBtQLZOPdPdqvB6guFysm0+OeFzYjdMrlMJNsFPHp0/kRPsN5wDZyhy+HJNB/I3x+IWRfvgnue9eOuMggaFXBlzWGO8opH/UUba8a/a2zurQmkxjzaXa8CdjT9mx++9xn2VPs514ip1zvDirxD/5ALzw+kOLHL60KUc90RKbd5a4QJBAPp0kg+HCgQEjYek/JRa9YTSb1B5up7xe/8g71F6NRqrXWdWLb4EfVR4lg81Y0ce7C1UzD4EZsym8nh5plYovlMCQQCoLaABvqUrulMixmYdc9nJgguvU7yIeUV7b2w9y+Nb0ZHB9EKcU8MHCtWuIr6PqitXN/U3A/uvoPGjT/yc2WqxAkEA0fREuSWP/NxANNTXNBqoNQcfb4wMKM/xDWfzlw7mU4wSSd1RjThARD6uNOOhbO58OGgcq2SPNMJDA0GfchzHpwJBAI53H+16b3cMfvKdeNGIWzytcnKSj8pYWPjImbv7pN0aOcxAu9Cr0DF+ByddfWo8MDzRRWPWdvX/c9Lxpj/EmfECQQD6V7N3XdnRv2ejpa6b3k1XVkVMXF4vCWO8EPDhJS+8JjCjx93IHWZlvG0OoalKZDvbeq86inr0/PpOG6kzHEOM"
    val publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkiSLibZuyLQQGxe5ZZ8oGDdiazwexrLvYdHJHGTu6jNGHJ1PdW4FkW/d9jqKD9m8RQSoJLoRgRPOyHx00S6UyZYMffFMRItP9+5Cn72XFKJ/KlubyiPuXUPRyc5Mxe8vfl4gtNfY+ltDObDCThfwin6UO0AEvjmS22s7mWVT1YwIDAQAB"
    //生成私钥和公钥
//    val generator = KeyPairGenerator.getInstance(RSACrypt.transformation)
//    val keyPair = generator.genKeyPair()
//    val privateKey = keyPair.private
//    val publicKey = keyPair.public
    val kf = KeyFactory.getInstance(RSACrypt.transformation)
    val privateKey = kf.generatePrivate(PKCS8EncodedKeySpec(Base64.decode(privateKeyStr)))
    val publicKey = kf.generatePublic(X509EncodedKeySpec(Base64.decode(publicKeyStr)))
    val privateEncrypt = RSACrypt.encrypt(input, publicKey)
    println("私密加密：" + privateEncrypt)
    val publicKeyDecrypt = RSACrypt.decrypt(privateEncrypt, privateKey)
    println("公密解密：" + publicKeyDecrypt)


}