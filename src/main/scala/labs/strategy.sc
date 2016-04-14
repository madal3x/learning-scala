type Strategy = (Int, Int) => Int

class Context(computer: Strategy) {
  def use(a: Int, b: Int) = {
    computer(a, b)
  }
}

val add: Strategy = _ + _
val multiply: Strategy = _ * _

val addContext = new Context(add)
addContext.use(4, 5)

val multiplyContext = new Context(multiply)
multiplyContext.use(4, 5)