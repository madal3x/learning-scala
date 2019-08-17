import akka.agent.Agent

case class AveragePrice(day: Int, price: BigDecimal)

//val a = Agent(AveragePrice(1, 10))

def max(l: List[Int]) = l.fold(Int.MinValue)((z, e) => if (z < e) e else z)

max(List(1,2,3,4,1,2,5,6,1,29,1))