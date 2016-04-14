package reactiveprog.actors.urlcrawl

import java.util.concurrent.Executor

import com.ning.http.client.AsyncHttpClient

import scala.concurrent.{Promise, Future}

object WebClient {
  case class BadStatus(statusCode: Int) extends Throwable

  private val client = new AsyncHttpClient()

  // mapping from listenable futures to scala futures
  def get(url: String)(implicit exec: Executor): Future[String] = {
    val f = client.prepareGet(url).execute()
    val p = Promise[String]()

    f.addListener(new Runnable {
      def run(): Unit = {
        val response = f.get
        if (response.getStatusCode < 400)
          p.success(response.getResponseBodyExcerpt(131072))
        else
          p.failure(BadStatus(response.getStatusCode))
      }
    }, exec)

    p.future
  }

  def shutdown(): Unit ={
    client.close()
  }
}