package labs

object PrivateConstructor extends App {
  /*
felher: To be more precise, abstract prevents the creation of those methods while retaining
the others while sealed disallows subtyping in another file,
so it's a very nice way to create wrappers for certain data types.
Same space where refined could be used. E.g., making a sure a counter can not be negative:

tpolecat: Yes, that's all private does here. abstract prevents instantiation without extension,
and sealed prevents extension outside the compilation unit.
 */

  sealed abstract case class Counter(i: Int)
  object Counter { def apply(i: Int): Option[Counter] = Some(new Counter(i) {}).filter(_.i > 0) }
}
