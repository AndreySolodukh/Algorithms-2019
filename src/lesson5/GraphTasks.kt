@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson5

import lesson5.Graph.*
import lesson5.impl.GraphBuilder
import java.util.*

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 */
/** Время реализации = O(n) **/
/** Затраты памяти = O(n) **/

fun Graph.findEulerLoop(): List<Edge> {
    if (vertices.isEmpty() || vertices.any { getNeighbors(it).size % 2 != 0 }) return listOf()
    val path = mutableListOf<Edge>()
    val visitedEdges = mutableSetOf<Edge>()
    val start = vertices.first()
    var current = start
    while (true) {
        val connections = getConnections(current).filter { it.value !in visitedEdges }
        if (connections.size == 1)
            if (visitedEdges.size == edges.size - 1) {
                path.add(connections[start]!!)
                break
            } else {
                current = connections.toList().first().first
                path.add(connections[current]!!)
                visitedEdges.add((connections[current]!!))
                continue
            }
        var minConnections = 0
        for (elem in connections.filter { it.key != start }) {
            val numOfConnections = getConnections(elem.key).filter { it.value !in visitedEdges }.size
            if (minConnections == 0) {
                minConnections = numOfConnections
                current = elem.key
            } else
                if (minConnections > numOfConnections) {
                    minConnections = numOfConnections
                    current = elem.key
                }
        }
        path.add(connections[current]!!)
        visitedEdges.add((connections[current]!!))
    }
    return path
}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
/** Время реализации = O(n) **/
/** Затраты памяти = O(n) **/

fun Graph.minimumSpanningTree(): Graph {
    if (vertices.isEmpty()) return GraphBuilder().build()
    val usedEdges = mutableListOf<Edge>()
    val usedVertices = mutableSetOf<Vertex>()

    fun buildTree(new: Vertex) {
        for ((vertex, edge) in getConnections(new))
            if (vertex !in usedVertices) {
                usedEdges.add(edge)
                usedVertices.add(vertex)
                buildTree(vertex)
            }
    }

    buildTree(vertices.first())
    val builder = GraphBuilder()
    builder.apply {
        for (i in usedEdges.indices) {
            if (i == 0) {
                addVertex(usedEdges[i].begin)
                addVertex(usedEdges[i].end)
            } else
                addVertex(usedEdges[i].end)
            addConnection(usedEdges[i].begin, usedEdges[i].end)
        }
    }
    return builder.build()
}

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Если на входе граф с циклами, бросить IllegalArgumentException
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 */


fun Graph.largestIndependentVertexSet(): Set<Vertex> {
    if (vertices.isEmpty()) return setOf()
    val trees = mutableMapOf<Vertex, Set<Vertex>>()
    val start = vertices.first()
    fun buildTrees(vertex: Vertex): Set<Vertex> {
        println(trees)
        if (trees[vertex] != null) return trees[vertex]!!
        trees[vertex] = setOf(vertex)
        val childLine = mutableSetOf<Vertex>()
        val grandLine = mutableSetOf<Vertex>()
        val childs = getNeighbors(vertex)
        val grandchilds = mutableSetOf<Vertex>()
        for (child in childs) {
            childLine += buildTrees(child)
            for (grand in getNeighbors(child))
                grandchilds += grand
        }
        for (grand in grandchilds)
            grandLine += buildTrees(vertex)
        trees[vertex] = if (grandLine.size + 1 > childLine.size) grandLine else childLine
        return trees[vertex]!!
    }
    return buildTrees(start)
}


/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 */
/** Время реализации = O(n!) **/
/** Затраты памяти = O(n!) **/
fun Graph.longestSimplePath(): Path {
    var sum = Path()
    val paths = ArrayDeque<Path>()
    for (elem in vertices)
        paths.add(Path(elem))
    while (paths.isNotEmpty()) {
        val path = paths.poll()
        if (path.length > sum.length)
            sum = path
        for (elem in getNeighbors(path.vertices.last()))
            if (elem !in path) paths.add(Path(path, this, elem))
    }
    return sum
}