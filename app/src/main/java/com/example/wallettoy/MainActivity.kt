package com.example.wallettoy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.wallettoy.common.Config.ADDRESS
import com.example.wallettoy.common.Config.ADDRESS2
import com.example.wallettoy.common.Config.KEY
import com.klaytn.caver.Caver
import com.klaytn.caver.transaction.type.ValueTransfer
import java.io.File
import java.math.BigInteger

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendingKLAY()
    }

    private fun sendingKLAY() {

        val caver = Caver("BAOBAB_URL")

        // caver.wallet에 키링 추가
        val keyring = caver.wallet.newKeyring(ADDRESS, KEY)
//        caver.wallet.add(keyring)

        // 자산 이동 트랜잭션 생성
        val valueTransfer: ValueTransfer = ValueTransfer.Builder()
            .setFrom(keyring.address)
            .setTo(ADDRESS2)
            .setNonce("")
            .setChainId("")
            .setValue(BigInteger.valueOf(1))
            .setGasPrice("0.1")
            .setGas(BigInteger.valueOf(30000))
            .build()

        // caver.waller.sign으로 트랜잭션 서명
        caver.wallet.sign(keyring.address, valueTransfer)
        val rlpEncoded = valueTransfer.rlpEncoding
        Log.w("TEST", ">>>>> RLP ENCODING $rlpEncoded")

    }
}