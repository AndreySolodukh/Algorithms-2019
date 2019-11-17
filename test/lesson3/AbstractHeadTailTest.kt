package lesson3

import java.util.*
import kotlin.test.*

abstract class AbstractHeadTailTest {
    private lateinit var tree: SortedSet<Int>
    private lateinit var custom: SortedSet<Int>
    private lateinit var randomTree: SortedSet<Int>
    private val randomTreeSize = 1000
    private val randomValues = mutableListOf<Int>()

    protected fun fillTree(create: () -> SortedSet<Int>) {
        this.custom = create()
        for (elem in setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17).shuffled())
            custom.add(elem)

        this.tree = create()
        //В произвольном порядке добавим числа от 1 до 10
        tree.add(5)
        tree.add(1)
        tree.add(2)
        tree.add(7)
        tree.add(9)
        tree.add(10)
        tree.add(8)
        tree.add(4)
        tree.add(3)
        tree.add(6)

        this.randomTree = create()
        val random = Random()
        for (i in 0 until randomTreeSize) {
            val randomValue = random.nextInt(randomTreeSize) + 1
            if (randomTree.add(randomValue)) {
                randomValues.add(randomValue)
            }
        }
    }


    protected fun doHeadSetTest() {
        var customSet = tree.headSet(10)
        for (i in 1..17) {
            if (i < 10) assertTrue(customSet.contains(i))
            else assertFalse(customSet.contains(i))
        }
        customSet = tree.headSet(1)
        for (i in 1..17)
            assertFalse(customSet.contains(i))

        var set: SortedSet<Int> = tree.headSet(5)
        assertEquals(true, set.contains(1))
        assertEquals(true, set.contains(2))
        assertEquals(true, set.contains(3))
        assertEquals(true, set.contains(4))
        assertEquals(false, set.contains(5))
        assertEquals(false, set.contains(6))
        assertEquals(false, set.contains(7))
        assertEquals(false, set.contains(8))
        assertEquals(false, set.contains(9))
        assertEquals(false, set.contains(10))


        set = tree.headSet(127)
        for (i in 1..10)
            assertEquals(true, set.contains(i))

    }

    protected fun doTailSetTest() {
        var customSet = custom.tailSet(10)
        for (i in 1..17) {
            if (i < 10) assertFalse(customSet.contains(i))
            else assertTrue(customSet.contains(i))
        }
        customSet = custom.tailSet(1)
        for (i in 1..17)
            assertTrue(customSet.contains(i))

        var set: SortedSet<Int> = tree.tailSet(5)
        assertEquals(false, set.contains(1))
        assertEquals(false, set.contains(2))
        assertEquals(false, set.contains(3))
        assertEquals(false, set.contains(4))
        assertEquals(true, set.contains(5))
        assertEquals(true, set.contains(6))
        assertEquals(true, set.contains(7))
        assertEquals(true, set.contains(8))
        assertEquals(true, set.contains(9))
        assertEquals(true, set.contains(10))

        set = tree.tailSet(-128)
        for (i in 1..10)
            assertEquals(true, set.contains(i))

    }

    protected fun doHeadSetRelationTest() {
        val customSet = custom.headSet(10)
        assertEquals(9, customSet.size)
        assertEquals(17, custom.size)
        custom.add(-5)
        assertTrue(customSet.contains(-5))
        custom.add(-1)
        assertTrue(customSet.contains(-1))
        custom.add(19)
        assertFalse(customSet.contains(19))
        assertFailsWith<IllegalArgumentException> { customSet.add(12) }
        assertEquals(11, customSet.size)
        assertEquals(20, custom.size)

        val set: SortedSet<Int> = tree.headSet(7)
        assertEquals(6, set.size)
        assertEquals(10, tree.size)
        tree.add(0)
        assertTrue(set.contains(0))
        set.add(-2)
        assertTrue(tree.contains(-2))
        tree.add(12)
        assertFalse(set.contains(12))
        assertFailsWith<IllegalArgumentException> { set.add(8) }
        assertEquals(8, set.size)
        assertEquals(13, tree.size)
    }

    protected fun doTailSetRelationTest() {
        val customSet = custom.tailSet(10)
        assertEquals(8, customSet.size)
        assertEquals(17, custom.size)
        custom.add(22)
        assertTrue(customSet.contains(22))
        custom.add(19)
        assertTrue(customSet.contains(19))
        custom.add(-1)
        assertFalse(customSet.contains(-1))
        assertFailsWith<IllegalArgumentException> { customSet.add(0) }
        assertEquals(10, customSet.size)
        assertEquals(20, custom.size)

        val set: SortedSet<Int> = tree.tailSet(4)
        assertEquals(7, set.size)
        assertEquals(10, tree.size)
        tree.add(12)
        assertTrue(set.contains(12))
        set.add(42)
        assertTrue(tree.contains(42))
        tree.add(0)
        assertFalse(set.contains(0))
        assertFailsWith<IllegalArgumentException> { set.add(-2) }
        assertEquals(9, set.size)
        assertEquals(13, tree.size)
    }

    protected fun doSubSetTest() {
        val smallCustom = custom.subSet(5, 12)
        for (i in 1..17) {
            if (i in 5..11) assertTrue(smallCustom.contains(i))
            else assertFalse(smallCustom.contains(i))
        }

        assertFailsWith<IllegalArgumentException> { smallCustom.add(1) }
        assertFailsWith<IllegalArgumentException> { smallCustom.add(15) }

        val allCustom = custom.subSet(-128, 128)
        for (i in 1..17)
            assertTrue(allCustom.contains(i))

        val smallSet: SortedSet<Int> = tree.subSet(3, 8)
        assertEquals(false, smallSet.contains(1))
        assertEquals(false, smallSet.contains(2))
        assertEquals(true, smallSet.contains(3))
        assertEquals(true, smallSet.contains(4))
        assertEquals(true, smallSet.contains(5))
        assertEquals(true, smallSet.contains(6))
        assertEquals(true, smallSet.contains(7))
        assertEquals(false, smallSet.contains(8))
        assertEquals(false, smallSet.contains(9))
        assertEquals(false, smallSet.contains(10))

        assertFailsWith<IllegalArgumentException> { smallSet.add(2) }
        assertFailsWith<IllegalArgumentException> { smallSet.add(9) }

        val allSet = tree.subSet(-128, 128)
        for (i in 1..10)
            assertEquals(true, allSet.contains(i))

        val random = Random()
        val toElement = random.nextInt(randomTreeSize) + 1
        val fromElement = random.nextInt(toElement - 1) + 1

        val randomSubset = randomTree.subSet(fromElement, toElement)
        randomValues.forEach { element ->
            assertEquals(element in fromElement until toElement, randomSubset.contains(element))
        }
    }

    protected fun doSubSetRelationTest() {
        val customSet = custom.subSet(10, 25)
        assertEquals(8, customSet.size)
        assertEquals(17, custom.size)
        custom.add(22)
        assertTrue(customSet.contains(22))
        customSet.add(23)
        assertTrue(custom.contains(23))
        custom.add(-3)
        assertFalse(customSet.contains(-3))
        custom.add(30)
        assertFalse(customSet.contains(30))
        assertFailsWith<IllegalArgumentException> { customSet.add(5) }
        assertFailsWith<IllegalArgumentException> { customSet.add(27) }
        assertEquals(10, customSet.size)
        assertEquals(21, custom.size)

        val set: SortedSet<Int> = tree.subSet(2, 15)
        assertEquals(9, set.size)
        assertEquals(10, tree.size)
        tree.add(11)
        assertTrue(set.contains(11))
        set.add(14)
        assertTrue(tree.contains(14))
        tree.add(0)
        assertFalse(set.contains(0))
        tree.add(15)
        assertFalse(set.contains(15))
        assertFailsWith<IllegalArgumentException> { set.add(1) }
        assertFailsWith<IllegalArgumentException> { set.add(20) }
        assertEquals(11, set.size)
        assertEquals(14, tree.size)
    }

}