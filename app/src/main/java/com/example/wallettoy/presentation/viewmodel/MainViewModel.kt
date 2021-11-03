package com.example.wallettoy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallettoy.common.Config
import com.klaytn.caver.Caver
import com.klaytn.caver.methods.response.Bytes32
import com.klaytn.caver.transaction.TxPropertyBuilder
import com.klaytn.caver.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    /**
     * 클레이 전송
     */
    fun sendKlay() {
        viewModelScope.launch(Dispatchers.IO) {

            val caver = Caver(Config.URL_BAOBAB)

            // caver.wallet에 키링 추가
            val keyring = caver.wallet.newKeyring(Config.ADDRESS, Config.KEY)


            // 자산 이동 트랜잭션 생성
            val valueTransfer = caver.transaction.valueTransfer.create(
                TxPropertyBuilder.valueTransfer()
                    .setFrom(keyring.address)
                    .setTo(Config.ADDRESS2)
                    .setValue(BigInteger(caver.utils.convertToPeb(BigDecimal.valueOf(1L), Utils.KlayUnit.KLAY)))
                    .setGas(BigInteger.valueOf(30000))
            )

            // caver.waller.sign으로 트랜잭션 서명
            caver.wallet.sign(keyring.address, valueTransfer)
            val rlpEncoded = valueTransfer.rlpEncoding
            Log.w("TEST", ">>>>> rlpEncoded = $rlpEncoded")

            // caver.rpc.klay.sendRawTransaction로 트랜잭션 전송
            kotlin.runCatching {
                caver.rpc.klay.sendRawTransaction(rlpEncoded).send()
            }.onSuccess { sendResult ->
                Log.d("TEST", ">>>>> sendResult = ${sendResult.result}")
                if (sendResult.hasError()) {
                    // 에러 처리
                }
            }.onFailure {
                // 예외 처리
                Log.e("TEST", ">>>>> onFailure = $it")
            }
        }

    }
}