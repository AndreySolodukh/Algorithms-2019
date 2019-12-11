@file:Suppress("UNUSED_PARAMETER")

package lesson6

import java.io.File
import java.lang.IllegalArgumentException

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
/** Время реализации = O(first.length * second.length) **/
/** Затраты памяти = O(first.length * second.length) **/

fun longestCommonSubSequence(first: String, second: String): String {
    val search = SubSearch()
    return search.longestSub(first, second)
}

class SubSearch {

    private val searched = mutableMapOf<Pair<String, String>, String>()

    private fun maxSub(first: String, second: String): String {
        return when {
            first.length > second.length -> first
            first.length < second.length -> second
            else -> if (first > second) first else second
        }
    }

    fun longestSub(first: String, second: String): String {
        val long = maxSub(first, second)
        val short = if (long == first) second else first
        if (searched[long to short] != null) return searched[long to short]!!
        if (short.isEmpty()) {
            searched[long to short] = ""
            return searched[long to short]!!
        } else
            if (long.first() == short.first()) {
                searched[long to short] = first.first() + longestSub(long.drop(1), short.drop(1))
                return searched[long to short]!!
            } else {
                searched[long to short] = maxSub(longestSub(long.drop(1), short), longestSub(long, short.drop(1)))
                return searched[long to short]!!
            }
    }

}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
/** Время реализации = O(n^2) **/
/** Затраты памяти = O(n) **/

fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    if (list.isEmpty()) return listOf()
    val subLength = mutableListOf<Int>()
    val ancestor = mutableListOf<Int>()
    for (i in list.indices) {
        subLength.add(1)
        ancestor.add(-1)
        for (j in 0..i)
            if (list[j] < list[i])
                if (subLength[i] < subLength[j] + 1 ||
                    (subLength[i] == subLength[j] + 1 && list[ancestor[i]] < list[j])
                ) {
                    ancestor[i] = j
                    subLength[i] = subLength[j] + 1
                }
    }
    // При такой реализации в принципе не должны
    // более подходящие под условие строки стоять позже по индексу,
    // так что дополнительных проверок для `max` не нужно.
    var max = subLength.indexOf(subLength.max())
    val sum = mutableListOf<Int>()
    while (max != -1) {
        sum.add(list[max])
        max = ancestor[max]
    }
    return sum.reversed()
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
/** Время реализации = O(height * width) **/
/** Затраты памяти = O(height * width) **/

fun shortestPathOnField(inputName: String): Int {
    val field = mutableMapOf<Pair<Int, Int>, Int>()
    var height = 0
    var width = 0
    val file = File(inputName).bufferedReader()
    var line = file.readLine()
    while (line != null) {
        val chars = line.split(' ')
        if (width == 0) width = chars.size
        for (i in chars.indices) {
            try {
                field[height to i] = chars[i].toInt()
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException()
            }
        }
        line = file.readLine()
        height++
    }

    for (i in 0 until height) {
        for (j in 0 until width) {
            when {
                i == 0 && j == 0 -> {
                }
                i == 0 -> field[i to j] = field[i to j]!! + field[i to j - 1]!!
                j == 0 -> field[i to j] = field[i to j]!! + field[i - 1 to j]!!
                else -> {
                    val left = field[i to j - 1]!!
                    val up = field[i - 1 to j]!!
                    val diag = field[i - 1 to j - 1]!!
                    when {
                        left < up && left < diag ->
                            field[i to j] = field[i to j]!! + field[i to j - 1]!!
                        up < diag ->
                            field[i to j] = field[i to j]!! + field[i - 1 to j]!!
                        else ->
                            field[i to j] = field[i to j]!! + field[i - 1 to j - 1]!!
                    }
                }
            }
        }
    }
    return field[height - 1 to width - 1]!!
}


// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5