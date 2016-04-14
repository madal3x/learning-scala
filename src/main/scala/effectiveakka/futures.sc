package effectiveakka

import scala.concurrent.Future

class SecurityPricingActor(exchange: Exchange,
                           backOffice: BackOffice) extends Actor {
  def receive = {
    case GetPricingInfo(security: Security) =>
      val originalSender = sender
      val bidAndAskFuture = Future { exchange.getBidAndAsk(security.id) }
      val lastPriceFuture = Future { backOffice.getLastPrice(security.id) }

      // parallel futures
      val response = for {
        (bid, ask) <- bidAndAskFuture
         lastPrice <- lastPriceFuture
      } yield SecurityPricing(bid, ask, lastPrice)
      response map (originalSender ! _)
  }
}

object futures {
  // sequential futures
  val accountsForCustomers = for {
    customer <- Future { databaseService.getCustomers }
    account <- Future { accountService.getAccounts(customer.id) }
  } yield (customer, account)
}
