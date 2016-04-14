abstract class X {
  val name: String
  val size = name.size
}

class Y extends {
  val name = "class Y"
} with X