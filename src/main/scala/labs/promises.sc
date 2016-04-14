// futures are promises
// try to have as much non-blocking as possible
// consider separate ExecutionContext for database calls
// use the Future block for not executing stuff on the webserver thread

import scala.concurrent.{Future, Promise}
case class TaxCut(reduction: Int)
// either give the type as a type parameter to the factory method:
val taxcut = Promise[TaxCut]()
// or give the compiler a hint by specifying the type of your val:
val taxcut2: Promise[TaxCut] = Promise()

// succeeding a promise
object Government {
  def redeemCampaignPledge(): Future[TaxCut] = {
    val p = Promise[TaxCut]()
    Future {
      println("Starting the new legislative period.")
      Thread.sleep(2000)
      p.success(TaxCut(20))
      println("We reduced the taxes! You must reelect us!!!!1111")
    }
    p.future
  }
}

import scala.util.{Success, Failure}
val taxCutF: Future[TaxCut] = Government.redeemCampaignPledge()
println("Now that they're elected, let's see if they remember their promises...")
taxCutF.onComplete {
  case Success(TaxCut(reduction)) =>
    println(s"A miracle! They really cut our taxes by $reduction percentage points!")
  case Failure(ex) =>
    println(s"They broke their promises! Again! Because of a ${ex.getMessage}")
}

//failing a promise
case class LameExcuse(msg: String) extends Exception(msg)
object LameGovernment {
  def redeemCampaignPledge(): Future[TaxCut] = {
    val p = Promise[TaxCut]()
    Future {
      println("Starting the new legislative period.")
      Thread.sleep(2000)
      p.failure(LameExcuse("global economy crisis"))
      println("We didn't fulfill our promises, but surely they'll understand.")
    }
    p.future
  }
}

