package playground

//import com.typesafe.scalalogging.LazyLogging

import java.util.concurrent.Executors

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

object ParallelismSample extends App {
    implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(5))

  val source = Map(("m1",ServiceOne), ("m2",ServiceTwo), ("m3",ServiceThree))

//    val s1 = source("m1").run
//    val s2 = source("m3").run
//    val s3 = source("m2").run
//
//    val res = for {
//      r1 <- s1
//      r2 <- s2
//      r3 <- s3
//    } yield Map("r1" -> r1, "r2" -> r2, "r3" -> r3)

    val futures1 = source.values.map(_.run)
    val futures2 = for (elem <- source.values) yield elem.run

    val res = Future.sequence(futures1)
    val resp = Await.result(res, 300 millis)

    println(s"Response: $resp")
    println("Stopping...")

  //  Thread.sleep(500)

}

//trait DownStreamService extends LazyLogging  {
  trait DownStreamService {

  def run(implicit ec: ExecutionContext): Future[Any]
  //  def run: Any

  def debug(msg: String): Unit = {
    val thread = Thread.currentThread.getName()
    println(s"[$thread] $msg")
  }
}

object ServiceOne extends DownStreamService {
  override def run(implicit ec: ExecutionContext) = {
    Future {
      debug("One start")
      Thread.sleep(300)
      debug("One stop")
      "ServiceOne"
    }
  }
}

object ServiceTwo extends DownStreamService {
  def run (implicit ec: ExecutionContext)= {
    Future {
      debug("Two start")
      Thread.sleep(100)
      debug("Two stop")
      ("ServiceTwo", "another kind")
    }
  }
}

object ServiceThree extends DownStreamService {
  case class MyResponse(msg: String)

  def run (implicit ec: ExecutionContext)= {
    Future {
      debug("Three start")
      Thread.sleep(200)
      debug("Three stop")
      new MyResponse("Serive three response")
    }
  }
}


// The list of all tasks needed for execution
//  val source: Observable[(String, DownStreamService)] = Observable(("m1",ServiceOne), ("m2", ServiceTwo), ("m3", ServiceThree))
//  val processed = source.mapAsync(parallelism = 3) { x => Task {
////      val resp = Await.result(x._2.run, 200 millis)
//      val resp = x._2.run
//      (x._1, resp)
//    }
//  }
//
////  processed
//  processed.toListL.foreach(println)

//  val res = for {
//    res1 <- source("m1").run
//    res2 <- source("m2").run
//    res3 <- source("m3").run
//  } yield Map("m1" -> res1, "m2" -> res2, "m3" -> res3)
