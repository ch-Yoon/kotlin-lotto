package lotto.view

import lotto.domain.lottonumber.unWrapping
import lotto.domain.shop.LottoPurchaseResult

class RealLottoResultView : LottoResultView {

    override fun display(lottoPurchaseResult: LottoPurchaseResult) {
        println("${lottoPurchaseResult.lottoGames.size}개를 구매했습니다.")
        lottoPurchaseResult.lottoGames
            .map { lottoGame -> lottoGame.lottoNumbers.value }
            .forEach { lottoNumbers -> println("[${lottoNumbers.unWrapping().joinToString(", ")}]") }
    }
}
