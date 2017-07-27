// https://www.youtube.com/watch?v=iobC5yGRWoo

trait Key {
  type Value
}

trait HMap {
  def get(key: Key): Option[key.Value]
  def add(key: Key)(value: key.Value): HMap
}

object HMap {
  def empty: HMap = ???
}

val sort = new Key { type Value = String}
val width = new Key { type Value = Int }

val hmap = HMap.empty
  .add(sort)("time")
  .add(width)(120)
  //.add(width)(true) // doesn't compile