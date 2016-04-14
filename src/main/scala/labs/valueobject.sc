type Point = (Int, Int)
val point: Point = (1, 2)

// x, y automatically vals -> immutable
case class Point2(x: Int, y: Int)
val point2 = Point2(1, 2)
point2.x



