package lotto.domain.analysis

import lotto.domain.lottonumber.LottoNumber
import lotto.domain.money.sum
import lotto.domain.money.toMoney
import lotto.domain.shop.LottoGame
import math.PositiveNumber
import math.orZero

class LottoResultAnalyst {

    fun analyze(request: LottoAnalysisRequest): LottoAnalysisResult {
        val winRankAnalysisResults = analyzeWinRanks(request)
        val revenue = calculateRevenue(
            lottoPurchaseAmount = request.lottoPurchaseAmount,
            winRankMatchResults = winRankAnalysisResults,
        )
        return LottoAnalysisResult(
            lottoWinRankAnalysisResults = winRankAnalysisResults,
            revenue = revenue,
        )
    }

    private fun analyzeWinRanks(
        request: LottoAnalysisRequest,
    ): List<LottoWinRankAnalysisResult> {
        val lottoWinRanksCountMap = calculateWinRankCount(request)

        return LottoWinRank.values()
            .sortedDescending()
            .map { winRank -> LottoWinRankAnalysisResult(winRank, lottoWinRanksCountMap[winRank].orZero()) }
    }

    private fun calculateWinRankCount(
        request: LottoAnalysisRequest,
    ): Map<LottoWinRank, PositiveNumber> {
        val lastWeekLottoNumbers = request.lastWeekWinLottoNumbers
        val lastWeekWinNumberSet = lastWeekLottoNumbers.lottoNumbers.value.toSet()

        return request.lottoGames
            .map { lottoGame -> calculateMatchCondition(lastWeekWinNumberSet, lastWeekLottoNumbers.bonusNumber, lottoGame) }
            .mapNotNull { matchCondition -> LottoWinRank.findOrNull(matchCondition) }
            .groupBy { it }
            .mapValues { (_, lottoWinRanks) -> PositiveNumber(lottoWinRanks.size) }
    }

    private fun calculateMatchCondition(
        lastWeekWinNumberSet: Set<LottoNumber>,
        bonusNumber: LottoNumber,
        lottoGame: LottoGame
    ): MatchCondition {
        val matchSuccessCount = lottoGame.lottoNumbers
            .value
            .count { lottoNumber -> lastWeekWinNumberSet.contains(lottoNumber) }
        val hasBonus = lottoGame.lottoNumbers.value.contains(bonusNumber)
        return MatchCondition(
            matchSuccessCount = PositiveNumber(matchSuccessCount),
            hasBonus = hasBonus,
        )
    }

    private fun calculateRevenue(
        lottoPurchaseAmount: PositiveNumber,
        winRankMatchResults: List<LottoWinRankAnalysisResult>,
    ): Revenue {
        return Revenue(
            totalCost = lottoPurchaseAmount.toMoney(),
            totalRevenue = winRankMatchResults.map { it.totalWinAmount }.sum()
        )
    }
}
