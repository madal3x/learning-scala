import akka.agent.Agent

case class AveragePrice(day: Int, price: BigDecimal)

val a = Agent(AveragePrice(1, 10))
