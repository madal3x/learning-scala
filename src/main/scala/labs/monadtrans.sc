import scalaz._
import Scalaz._
import scala.concurrent.Future

/*val fa = OptionT[Future, Int](Future(Some(1)))
val fb = OptionT[Future, Int](Future(Some(2)))
val r = fa.flatMap(a => fb.map(b => a + b))*/

import scala.concurrent.ExecutionContext.Implicits.global

def getOrders: Future[List[Int]] = Future(List(1,2,3))
def getOrderItems(orderId: Int): Future[List[Int]] = Future(List(3,4,5))

val items: ListT[Future, Int] = {
  val r =
    for {
      orderId <- ListT(getOrders)
      itemId  <- ListT(getOrderItems(orderId))
    } yield itemId

  r
}

items.run