package simple2

import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

object SignatureDemo{
    /**
     * 签名
     * @param input 数据源
     * @param privateKey 私钥
     * @return 经过Base63加密后的签名操作结果的签名字节。
     */
    fun sign(input:String,privateKey: PrivateKey):String{
        //获取数字签名实例对象
        val signature = Signature.getInstance("SHA256withRSA")
        //初始化签名
        signature.initSign(privateKey)
        //设置数据源
        signature.update(input.toByteArray())
        //签名
        val sign = signature.sign()
        return Base64.encode(sign)
    }

    /**
     * 校验
     * @param input 数据源
     * @param signStr 签名
     * @param publicKey 公钥
     * @return 如果签名被验证则为true，否则为false。
     */
    fun verify(input: String,signStr:String,publicKey: PublicKey):Boolean{
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initVerify(publicKey)
        signature.update(input.toByteArray())
        val verify = signature.verify(Base64.decode(signStr))
        return verify
    }
}
fun main(args: Array<String>) {
    val privateKeyStr = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKSJIuJtm7ItBAbF7llnygYN2JrPB7Gsu9h0ckcZO7qM0YcnU91bgWRb932OooP2bxFBKgkuhGBE87IfHTRLpTJlgx98UxEi0/37kKfvZcUon8qW5vKI+5dQ9HJzkzF7y9+XiC019j6W0M5sMJOF/CKfpQ7QAS+OZLbazuZZVPVjAgMBAAECgYBtQLZOPdPdqvB6guFysm0+OeFzYjdMrlMJNsFPHp0/kRPsN5wDZyhy+HJNB/I3x+IWRfvgnue9eOuMggaFXBlzWGO8opH/UUba8a/a2zurQmkxjzaXa8CdjT9mx++9xn2VPs514ip1zvDirxD/5ALzw+kOLHL60KUc90RKbd5a4QJBAPp0kg+HCgQEjYek/JRa9YTSb1B5up7xe/8g71F6NRqrXWdWLb4EfVR4lg81Y0ce7C1UzD4EZsym8nh5plYovlMCQQCoLaABvqUrulMixmYdc9nJgguvU7yIeUV7b2w9y+Nb0ZHB9EKcU8MHCtWuIr6PqitXN/U3A/uvoPGjT/yc2WqxAkEA0fREuSWP/NxANNTXNBqoNQcfb4wMKM/xDWfzlw7mU4wSSd1RjThARD6uNOOhbO58OGgcq2SPNMJDA0GfchzHpwJBAI53H+16b3cMfvKdeNGIWzytcnKSj8pYWPjImbv7pN0aOcxAu9Cr0DF+ByddfWo8MDzRRWPWdvX/c9Lxpj/EmfECQQD6V7N3XdnRv2ejpa6b3k1XVkVMXF4vCWO8EPDhJS+8JjCjx93IHWZlvG0OoalKZDvbeq86inr0/PpOG6kzHEOM"
    val publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkiSLibZuyLQQGxe5ZZ8oGDdiazwexrLvYdHJHGTu6jNGHJ1PdW4FkW/d9jqKD9m8RQSoJLoRgRPOyHx00S6UyZYMffFMRItP9+5Cn72XFKJ/KlubyiPuXUPRyc5Mxe8vfl4gtNfY+ltDObDCThfwin6UO0AEvjmS22s7mWVT1YwIDAQAB"
    val input = "最新版的华为P30，¥9起，欢迎订购"
    val input1 = "最新版的华为P30¥9起，欢迎订购"
    val keyFactory = KeyFactory.getInstance("RSA")
    val privateKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(Base64.decode(privateKeyStr)))
    val publicKey = keyFactory.generatePublic(X509EncodedKeySpec(Base64.decode(publicKeyStr)))
    val sign = SignatureDemo.sign(input, privateKey)
    println("sign: $sign")
    println("verify: ${SignatureDemo.verify(input1,sign,publicKey)}")

}