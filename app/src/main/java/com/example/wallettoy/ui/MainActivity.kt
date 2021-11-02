package com.example.wallettoy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.wallettoy.common.Config.ADDRESS
import com.example.wallettoy.common.Config.ADDRESS2
import com.example.wallettoy.common.Config.KEY
import com.example.wallettoy.common.Config.URL_BAOBAB
import com.klaytn.caver.Caver
import com.klaytn.caver.transaction.TxPropertyBuilder
import java.math.BigInteger
import androidx.databinding.DataBindingUtil
import com.example.wallettoy.R
import com.example.wallettoy.databinding.ActivityMainBinding
import com.example.wallettoy.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            mainViewModel = this@MainActivity.mainViewModel
            lifecycleOwner = this@MainActivity
        }

    }

    private fun sendingKLAY() {

        val caver = Caver(URL_BAOBAB)

        // caver.wallet에 키링 추가
        val keyring = caver.wallet.newKeyring(ADDRESS, KEY)


        // 자산 이동 트랜잭션 생성
        val valueTransfer = caver.transaction.valueTransfer.create(
            TxPropertyBuilder.valueTransfer()
                .setFrom(keyring.address)
                .setTo(ADDRESS2)
                .setValue(BigInteger.valueOf(1))
                .setGas(BigInteger.valueOf(30000))
        )

        // caver.waller.sign으로 트랜잭션 서명
        caver.wallet.sign(keyring.address, valueTransfer)
        val rlpEncoded = valueTransfer.rlpEncoding
        Log.w("TEST", ">>>>> RLP ENCODING $rlpEncoded")



    }
}