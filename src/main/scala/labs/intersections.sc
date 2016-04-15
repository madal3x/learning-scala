import scala.collection.mutable
import util.control.Breaks._

def solution(A: Array[Int], B: Array[Int], K: Int): Int = {

  def paths(A: Array[Int], B: Array[Int]): Unit = {
    val starts = A.filterNot(B.contains(_))
    val ends = B.filterNot(A.contains(_))
    val neighbours = mutable.HashMap.empty[Int, mutable.MutableList[Int]].withDefaultValue(mutable.MutableList.empty[Int])
    for (i <- A.indices) {
      neighbours(A(i)) += B(i)
    }

    println(neighbours.toString())

    println(findPaths(neighbours, 5, 4))

    /*for (start <- starts; end <- ends) {
      println(findPaths(neighbours, start, end))
    }*/

    def findPaths(neighbours: mutable.Map[Int, mutable.MutableList[Int]], start: Int, finish: Int): mutable.MutableList[Int] = {
      val frontier = mutable.Queue.empty[Int]
      val came_from = mutable.HashMap.empty[Int, Int]
      frontier.enqueue(start)
      while (frontier.nonEmpty) {
        var current = frontier.dequeue()
        for (next <- neighbours(current)) {
          if (!came_from.contains(next)) {
            frontier.enqueue(next)
            came_from(next) = current
          }
        }
      }

      var current = finish
      val path = mutable.MutableList(current)
      while (current != start) {
        current = came_from(current)
        path += current
      }

      path.reverse

      path
    }
  }
  paths(A, B)

  1
}
solution(Array(5, 1, 0, 2, 7, 0, 6, 6, 1), Array(1, 0, 7, 4, 2, 6, 8, 3, 9), 0)