import scala.math.Numeric.Implicits._

def func[A: Numeric](a: A, b: A): A =
  a + b

func(1D, 2D)
func(0, 1)