package lesson3

import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(val value: T) {

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    override fun height(): Int = height(root)

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    override fun remove(element: T): Boolean {
        if (!contains(element)) return false
        var sum = findParent(element)
        val node = restructure(find(element)!!)
        if (sum.left != null && sum.left!!.value == element)
            sum.left = node
        else
            sum.right = node
        while (sum.value != root!!.value) {
            val current = findParent(sum.value)
            if (current.left != null && current.left!!.value == sum.value)
                current.left = sum
            else
                current.right = sum
            sum = current
        }
        root = sum
        return true
    }

    private fun findReplace(node: Node<T>): Pair<T, Char> {
        // 'n' - none - после замены последующая обработка не нужна
        // 's' - slight - после замены нужна небольшая доработка
        // 'f' - full - после замены нужна значительная доработка
        if (node.left == null) return node.right!!.value to 'n'
        var sum = node
        while (sum.left != null)
            sum = sum.left!!
        return if (sum.right == null)
            sum.value to 's'
        else
            sum.value to 'f'
    }

    private fun restructure(node: Node<T>): Node<T>? {
        if (node.left == null && node.right == null) return null
        if (node.left == null) return node.right
        if (node.right == null) return node.left
        val replace = findReplace(node)
        when {
            replace.second == 'n' -> {
                val sum = Node(replace.first)
                sum.left = node.left
                sum.right = find(replace.first)!!.right
                return sum
            }
            replace.second == 's' -> {
                // Согласно случаю 's', replace.first -
                // это - узел без наследников,
                // так что дальше первой строки
                // в этой функции он не продвинется.
                remove(replace.first)
                val sum = Node(replace.first)
                sum.left = node.left
                sum.right = find(replace.first)!!.right
                return sum
            }
            else -> {
                // ???
                remove(replace.first)
                val sum = Node(replace.first)
                sum.left = node.left
                sum.right = find(replace.first)!!.right
                return sum
            }
        }
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    private fun findParent(kid: T): Node<T> {
        var sum = root ?: throw NoSuchElementException()
        while (true) {
            if ((sum.left != null && sum.left!!.value == kid) ||
                (sum.right != null && sum.right!!.value == kid)
            ) return sum
            if (sum.value < kid) sum = sum.right ?: throw NoSuchElementException()
            if (sum.value > kid) sum = sum.left ?: throw NoSuchElementException()
        }
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {

        private var current: T? = null
        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        /** Время реализации = до O(n) **/
        /** Затраты памяти = 1 **/
        override fun hasNext(): Boolean = current != last()

        /**
         * Поиск следующего элемента
         * Средняя
         */
        /** Время реализации = до O(n) **/
        /** Затраты памяти = 1 **/
        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            if (current == null) return first()
            current = nextAfter(find(current!!)!!, current!!)
            return current!!
        }

        private fun nextAfter(node: Node<T>, current: T): T {
            var sum = node
            while (true)
                when {
                    sum.value == current -> {
                        sum = if (sum.right != null) sum.right!!
                        else findParent(sum.value)
                    }
                    sum.value > current -> {
                        if (sum.left != null) {
                            if (sum.left!!.value > current) sum = sum.left!!
                            if (sum.left!!.value == current &&
                                sum.left!!.right == null
                            ) return sum.value
                        } else return sum.value
                    }
                    else -> sum = findParent(sum.value)
                }
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            val now = current
            remove(next())
            current = now
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Найти множество всех элементов в диапазоне [fromElement, toElement)
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        val set = mutableSetOf<T>()
        val iterator = BinaryTreeIterator()
        while (iterator.hasNext()) {
            val value = iterator.next()
            if (value >= fromElement && value < toElement) set.add(value)
            if (value >= toElement) break
        }
        return set.toSortedSet()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        val set = mutableSetOf<T>()
        val iterator = BinaryTreeIterator()
        while (iterator.hasNext()) {
            val value = iterator.next()
            if (value < toElement) set.add(value)
            else break
        }
        return set.toSortedSet()
    }


    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        val set = mutableSetOf<T>()
        val iterator = BinaryTreeIterator()
        while (iterator.hasNext()) {
            val value = iterator.next()
            if (value >= fromElement) set.add(value)
        }
        return set.toSortedSet()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
