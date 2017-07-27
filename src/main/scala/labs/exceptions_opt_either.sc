import scala.util.control.Exception._

catching(classOf[ArithmeticException]) opt (2 / 0)
allCatch opt (2 / 0)
allCatch either (2 / 0)
allCatch opt (2 / 1)