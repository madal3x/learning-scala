import akka.actor.Actor
import akka.pattern.CircuitBreaker

class CircuitBreakingActor extends Actor {

  import context.dispatcher

  val circuitBreaker = new CircuitBreaker(context.system.scheduler,
    maxFailures = 10,
    callTimeout = 100.milliseconds,
    resetTimeout = 1.seconds).
    onOpen(logCircuitBreakerOpen())

  def logCircuitBreakerOpen() = log.info("CircuitBreaker is open")
}