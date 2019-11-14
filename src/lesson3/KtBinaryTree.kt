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
        TODO()
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

    // Предполагается, что в эту функцию будут попадать только существующие узлы
    private fun whosMyParent(kid: T, node: Node<T>): Node<T> {
        if ((node.left != null && node.left!!.value == kid) ||
            (node.right != null && node.right!!.value == kid)
        ) return node
        if (node.value < kid) return whosMyParent(kid, node.right!!)
        return whosMyParent(kid, node.left!!)
    }

    private fun nextAfter(node: Node<T>, current: T?): T {
        if (current == null) {
            return if (node.left == null) {
                node.value
            } else nextAfter(node.left!!, current)
        }
        if (node.left != null &&
            node.left!!.value == current &&
            node.left!!.right == null
        ) return node.value
        return when {
            node.value == current -> if (node.right != null) nextAfter(node.right!!, current)
            else nextAfter(whosMyParent(current, root!!), current)
            node.value > current -> if (node.left != null && node.left!!.value > current) nextAfter(
                node.left!!,
                current
            )
            else node.value
            else -> nextAfter(whosMyParent(node.value, root!!), current)
        }
    }

    private fun maxElem(node: Node<T>?): T? {
        if (node == null) return null
        return if (node.right != null) maxElem(node.right!!)
        else node.value
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {

        private var current: T? = null
        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        override fun hasNext(): Boolean = (current != maxElem(root) && root != null)

        /**
         * Поиск следующего элемента
         * Средняя
         */
        override fun next(): T {
            if (current == maxElem(root) || root == null) throw IndexOutOfBoundsException()
            current = if (current == null) nextAfter(root!!, current)
            else nextAfter(find(current!!)!!, current)
            return current!!
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            // TODO
            throw NotImplementedError()
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
