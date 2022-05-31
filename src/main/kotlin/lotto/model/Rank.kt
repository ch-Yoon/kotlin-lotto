package lotto.model

import lotto.domain.dto.LottoMatchResult

enum class Rank(val count: Int, val amount: Long) {
    FIFTH(3, 5000),
    FOURTH(4, 50000),
    THIRD(5, 1500000),
    SECOND(5, 30000000),
    FIRST(6, 2000000000),
    NONE(0, 0);

    companion object {
        fun of(matchResult: LottoMatchResult): Rank {
            return if (matchResult.matchCount == THIRD.count) {
                getSecondOrThird(matchResult.isBonusMatch)
            } else
                Rank.values().find { it.count == matchResult.matchCount } ?: NONE
        }

        private fun getSecondOrThird(isBonusMatch: Boolean): Rank {
            return if (isBonusMatch) SECOND else THIRD
        }
    }
}