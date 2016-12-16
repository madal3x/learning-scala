import rx.lang.scala.Observable._
val status = items("ok", "still ok") ++ error(new Exception)
val fixedStatus =
  status.onErrorReturn(e => "exception occurred.")
fixedStatus.subscribe(println(_))

val continuedStatus =
  status.onErrorResumeNext(e => items("better", "much better"))
continuedStatus.subscribe(println(_))
