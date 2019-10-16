@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortTimes(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */

// O(n*log(n)) - ???
fun sortAddresses(inputName: String, outputName: String) {
    File(outputName).bufferedWriter().use {
        val citizens: MutableMap<Pair<String, Int>, List<String>> = mutableMapOf()
        for (line in File(inputName).readLines()) {
            try {
                val roomer = line.split(" - ")[0]
                val house = line.split(" - ")[1].split(" ")[0]
                // Проверка на "лишние" элементы после полной записи формата "фамилия имя - дом номер"
                if (line.split(" ").size != 5) {
                    throw IllegalArgumentException()
                }
                // Проверка формата номера дома
                val number = line.split(" - ")[1].split(" ")[1].toInt()
                citizens[Pair(house, number)] = (citizens[Pair(house, number)] ?: listOf()) + roomer
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException()
            }
        }
        for (elem in citizens.keys.sortedBy { it.second }.sortedBy { it.first }) {
            it.write("${elem.first} ${elem.second}")
            it.write(" - ")
            it.write(citizens[elem]!!.sorted().toString().drop(1).dropLast(1))
            it.newLine()
        }
    }
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */

/** Время реализации >= O(n*log(n)) **/
fun sortTemperatures(inputName: String, outputName: String) {
    File(outputName).bufferedWriter().use {
        val temperatures: MutableMap<Float, Int> = mutableMapOf()
        try {
            for (line in File(inputName).readLines()) {
                val value = line.toFloat()
                temperatures[value] = (temperatures[value] ?: 0) + 1
            }
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException()
        }
        for ((value, repeats) in temperatures.toSortedMap()) {
            for (i in 1..repeats) {
                it.write(value.toString())
                it.newLine()
            }
        }
    }
}
/*
/** Время исполнения = O(n^2) **/
/** Падает по времени при ~150000 и более значениях в inputName **/
fun sortTemperatures(inputName: String, outputName: String) {
    File(outputName).bufferedWriter().use {
        val temperatures: MutableList<Float> = mutableListOf()
        try {
            for (line in File(inputName).readLines()) {
                val value = line.toFloat()
                when {
                    temperatures.isEmpty() || (value >= temperatures.last()) -> temperatures.add(value)
                    value <= temperatures.first() -> temperatures.add(0, value)
                    else ->
                        for (i in 0 until temperatures.size)
                            if ((value >= temperatures[i]) && (value <= temperatures[i + 1])) {
                                temperatures.add(i + 1, value)
                                break
                            }
                }
            }
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException()
        }
        for (elem in temperatures) {
            it.write(elem.toString())
            it.newLine()
        }
    }
}
*/


/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 *
 * ПОРЯДОК РАСПОЛОЖЕНИЯ ОСТАЛЬНЫХ ЧИСЕЛ ДОЛЖЕН ОСТАТЬСЯ БЕЗ ИЗМЕНЕНИЯ.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */

/** Время исполнения ~ O(n) **/

fun sortSequence(inputName: String, outputName: String) {
    File(outputName).bufferedWriter().use {
        val repeats = mutableMapOf<Int, Int>()
        var maxRepeats = 0
        var minOften: Int? = null
        try {
            for (line in File(inputName).readLines()) {
                val value = line.toInt()
                repeats[value] = (repeats[value] ?: 0) + 1
                if ((minOften == null) ||
                    (repeats[value]!! > maxRepeats) ||
                    (repeats[value]!! == maxRepeats && value < minOften)
                ) {
                    maxRepeats = repeats[value]!!
                    minOften = value
                }
            }
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException()
        }


        for (line in File(inputName).readLines()) {
            if (line != minOften.toString()) {
                it.write(line)
                it.newLine()
            }
        }
        for (i in 1..maxRepeats) {
            it.write(minOften.toString())
            it.newLine()
        }
    }
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */

/** Время исполнения - O(n*log(n)) **/
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    for (i in 0 until first.size) {
        second[i] = first[i]
    }
    second.sort()
}


