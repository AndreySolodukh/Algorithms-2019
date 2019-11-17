package lesson3

import java.util.*
import javax.swing.text.StyledEditorKit
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

    private fun elements(): List<T> {
        val list = mutableListOf<T>()
        val iter = BinaryTreeIterator()
        while (iter.hasNext()) {
            list.add(iter.next())
        }
        return list
    }

    override fun remove(element: T): Boolean {
        if (!contains(element)) return false
        if (element == root!!.value) {
            root = restructure(find(element)!!)
        } else {
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
        }
        size--
        return true
    }

    private fun findReplace(node: Node<T>): Pair<T, Boolean> {
        var sum = node.right!!
        if (sum.left == null) return sum.value to false
        while (sum.left != null)
            sum = sum.left!!
        return sum.value to true
    }

    private fun restructure(node: Node<T>): Node<T>? {
        if (node.left == null && node.right == null) return null
        if (node.left == null) return node.right
        if (node.right == null) return node.left
        val replace = findReplace(node)
        if (!replace.second) {
            val sum = Node(replace.first)
            sum.left = node.left
            sum.right = find(replace.first)!!.right
            return sum
        } else {
            remove(replace.first)
            size++
            val sum = Node(replace.first)
            sum.left = node.left
            sum.right = find(node.value)!!.right
            return sum
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
        var sum: Node<T> = root ?: throw NoSuchElementException()
        if (kid == root!!.value) throw NoSuchElementException()
        while (true) {
            if ((sum.left != null && sum.left!!.value == kid) ||
                (sum.right != null && sum.right!!.value == kid)
            ) return sum
            if (sum.value < kid) {
                sum = sum.right ?: throw NoSuchElementException()
                continue
            }
            if (sum.value > kid) {
                sum = sum.left ?: throw NoSuchElementException()
                continue
            }
        }
    }

    private fun nextAfter(node: Node<T>, current: T): T {
        var sum = node
        while (true) {
            sum = when {
                sum.value == current -> if (sum.right != null) sum.right!!
                else findParent(current)
                sum.value > current -> {
                    if (sum.left != null &&
                        sum.left!!.value == current &&
                        sum.left!!.right == null
                    ) return sum.value
                    if (sum.left != null && sum.left!!.value > current) sum.left!!
                    else return sum.value
                }
                else -> findParent(sum.value)
            }
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
        override fun hasNext(): Boolean {
            return try {
                current != last()
            } catch (e: NoSuchElementException) {
                false
            }
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */
        /** Время реализации = до O(n) **/
        /** Затраты памяти = 1 **/
        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            if (current == null) {
                current = first()
                return current!!
            }
            current = nextAfter(find(current!!)!!, current!!)
            return current!!
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            println()
            println("---")
            println("----")
            println(elements())
            remove(current)
            val last = last()
            if (current!! > last) current = last
            println(elements())
            println("----")
            println("---")
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null
/*
    class SubSet<T : Comparable<T>>(
        val tree: BinaryTree<T>,
        val hasStart: Boolean, val start: T,
        val hasFinish: Boolean, val finish: T
    ) : BinaryTree<T>() {

        override fun contains(element: T) =
            tree.contains(element) && (!hasStart || element >= start) && (!hasFinish || element < finish)

        override fun add(element: T): Boolean =
            if ((!hasStart || element >= start) && (!hasFinish || element < finish)) tree.add(element)
            else throw IllegalArgumentException()

        internal fun size(): Int {
            var size = 0
            for (elem in this)
                if (elem != null) size++
            return size
        }
    }
*/
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
