package common.shffule

interface Shuffler<T> {

    fun shuffled(source: List<T>): List<T>
}