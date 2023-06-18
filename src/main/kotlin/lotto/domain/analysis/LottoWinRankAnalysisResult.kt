package lotto.domain.analysis

import common.PositiveNumber

data class LottoWinRankAnalysisResult(
    val lottoWinRank: LottoWinRank,
    val ranksCount: PositiveNumber,
) {
    val numberMatchCount = lottoWinRank.matchCondition.matchSuccessCount
    val rankWinAmount = lottoWinRank.winAmount
    val totalWinAmount = lottoWinRank.winAmount * ranksCount
}