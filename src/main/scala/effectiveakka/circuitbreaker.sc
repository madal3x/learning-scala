import akka.actor.{Actor, ActorLogging}
import akka.pattern.CircuitBreaker

import scala.concurrent.duration._

trait CircuitBreakingActor extends Actor with ActorLogging{

  import context.dispatcher

  val circuitBreaker = new CircuitBreaker(context.system.scheduler,
    maxFailures = 10,
    callTimeout = 100.milliseconds,
    resetTimeout = 1.seconds).
    onOpen(logCircuitBreakerOpen())

  def logCircuitBreakerOpen() = log.info("CircuitBreaker is open")
}