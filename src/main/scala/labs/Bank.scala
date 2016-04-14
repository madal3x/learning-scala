// by Debasish Ghosh http://www.infoq.com/presentations/functional-ddd-financial
package labs

import scalaz.Kleisli

case class Order(id: String)
case class ClientOrderSheet(clientName: String)
case class Market(name: String)
case class Account(no: String)
case class Execution(seqNo: Int)
case class Trade(qty: Int)

trait Bank1 {
  def clientOrders: ClientOrderSheet => List[Order]
  def execute: Market => Account => Order => List[Execution]
  def allocate: List[Account] => Execution => List[Trade]


}


trait Bank2 {
  def clientOrders: ClientOrderSheet => List[Order]
  def execute(m: Market, broker: Account): Order => List[Execution]
  def allocate(accounts: List[Account]): Execution => List[Trade]

  // this implementation is my own, without scalaz
  // however it holds only for List?
  def tradeGeneration(market: Market, broker: Account, clientAccounts: List[Account]): ClientOrderSheet => List[Trade] =
    clientOrders(_)
      .flatMap(execute(market, broker))
      .flatMap(allocate(clientAccounts))
}


trait Bank3 {
  def clientOrders: Kleisli[List, ClientOrderSheet, Order]
  def execute(m: Market, broker: Account): Kleisli[List, Order, Execution]
  def allocate(accounts: List[Account]): Kleisli[List, Execution, Trade]

  def tradeGeneration(market: Market, broker: Account, clientAccounts: List[Account]) =
    clientOrders andThen
      execute(market, broker) andThen
        allocate(clientAccounts)
}