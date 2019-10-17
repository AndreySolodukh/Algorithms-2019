@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.io.File

/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */

/** Время реализации = O(n) **/
/** Затраты памяти = O(1) **/
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    var bestPrice = 0
    var lowPrice = 0
    var lowPriceIndex = 1
    var bestPriceFirstIndex = 1
    var bestPriceLastIndex = 2
    var lineNumber = 1
    try {
        for (line in File(inputName).readLines()) {
            val price = line.toInt()
            when (lineNumber) {
                1 -> {
                    bestPrice = -price
                    lowPrice = price
                }
                2 -> {
                    bestPrice += line.toInt()
                    if (price < lowPrice) {
                        lowPrice = price
                        lowPriceIndex = lineNumber
                    }
                }
                else -> {
                    if (price - lowPrice > bestPrice) {
                        bestPriceFirstIndex = lowPriceIndex
                        bestPriceLastIndex = lineNumber
                        bestPrice = price - lowPrice
                    } else
                        if (price < lowPrice) {
                            lowPrice = price
                            lowPriceIndex = lineNumber
                        }
                }
            }
            lineNumber++
        }
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException()
    }
    return bestPriceFirstIndex to bestPriceLastIndex
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 *
 * Общий комментарий: решение из Википедии для этой задачи принимается,
 * но приветствуется попытка решить её самостоятельно.
 */

/** Время реализации = O(n^2) **/
/** Затраты памяти = O(n) **/
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    val people = mutableListOf<Boolean>()
    var killed = -1
    for (i in 1..menNumber) people.add(false)
    for (i in 0..menNumber - 2) {
        for (j in 1..choiceInterval) {
            killed++
            if (killed == menNumber) killed = 0
            while (people[killed]) {
                killed++
                if (killed == menNumber) killed = 0
            }
        }
        people[killed] = true
    }
    return people.indexOf(people.first { !it }) + 1
}

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 */
fun longestCommonSubstring(first: String, second: String): String {
    TODO()
    /*
    var bestSubstring = ""
    var substring = ""
    var index = 0
    for (i in 0 until first.length) {
    }
    return bestSubstring
    */
}

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 */

/** Время реализации = O(n) **/
/** Затраты памяти = с ростом limit -> O(n) **/
fun calcPrimesNumber(limit: Int): Int {
    if (limit <= 1) return 0
    val numbers = mutableListOf<Boolean>()
    val simples = mutableSetOf<Int>()
    repeat(limit + 1) { numbers.add(true) }
    for (i in 2..limit) {
        if (numbers[i]) {
            simples.add(i)
            numbers[i] = false
        }
        if (i <= limit / 2)
            for (elem in simples) {
                val composition = elem * i.toLong()
                if (elem <= i && composition <= limit) {
                    numbers[elem * i] = false
                }
            }
    }
    return simples.size
}


/*
/* Время реализации = O(n^2) (???) */
fun calcPrimesNumber(limit: Int): Int {
    if (limit <= 1) return 0
    val simples = mutableSetOf(2)
    for (i in 2..limit)
        for (elem in simples) {
            if (elem > i / 2) {
                simples.add(i)
                break
            }
            if (i % elem == 0) break
        }
    return simples.size
}
*/

/**
 * Балда
 * Сложная
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 */
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    TODO()
}