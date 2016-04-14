try {
  remoteCalculatorService.add(1, 2)
} catch {
  case e: ServerIsDownException => log.error(e, "the remote calculator service is unavailable. should have kept your trusty HP.")
} finally {
  remoteCalculatorService.close()
}


try
{
  // your scala code here
}
catch
  {
    case foo: FooException => handleFooException(foo)
    case bar: BarException => handleBarException(bar)
    case _: Throwable => println("Got some other kind of exception")
  }
finally
{
  // your scala code here, such as to close a database connection
}