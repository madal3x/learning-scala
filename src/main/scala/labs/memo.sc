// https://stackoverflow.com/questions/16257378/is-there-a-generic-way-to-memoize-in-scala

import scala.collection.mutable

def memoize[I, O](f: I => O): I => O = new mutable.HashMap[I, O]() {
  override def apply(key: I) = getOrElseUpdate(key, f(key))
}