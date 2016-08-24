// http://stackoverflow.com/questions/5259006/underscore-in-named-arguments

case class WithUnit(value: Int, unit: String)

List(1,2,3,4).map(WithUnit(_, "cm"))

// not possible, _ is interpretted in relation to the most shortsighted scope defined by paranthesis (or blocks {})
// compiler sees the _ as an anonymous function
// x => value = x instead of
// value = x => x
//List(1,2,3,4).map(WithUnit(value = _, "cm"))

// instead
List(1,2,3,4).map(x => WithUnit(value = x, "cm"))