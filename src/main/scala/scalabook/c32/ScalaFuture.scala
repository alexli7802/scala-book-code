package scalabook.c32

/* scala.concurrent
*   - trait Future[+T]         { map, flatMap, filter, collect, ... }
*   - trait ExecutionContext
*   - trait Promise[T]         { future, complete, completeWith, failure, success, ... }
* */

import com.sun.net.httpserver.Authenticator

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object ScalaFuture {

  /*
  *  construction: Future.apply
  * */
  lazy val fut_0 = Future { Thread.sleep(3000); 42 }

  lazy val fut_1 = Future { Thread.sleep(2000); 21 / 0 }

  lazy val fut_2 = fut_0.map { Thread.sleep(500); _ * 2 }

  /*
  *  construction: Future.successful/failed/fromTry
  * */
  lazy val fut_3 = Future.successful { 21 + 21 }                // not need 'ExecutionContext'
  lazy val fut_4 = Future.failed(new Exception("screwed up!"))  // not need 'ExecutionContext'

  lazy val fut_5 = Future.fromTry( Success(5 * 2) )
  lazy val fut_6 = Future.fromTry( Failure(new Exception("bummer!")) )

  /*
  *  construction: Promise
  * */
  lazy val pro = Promise[Int]
  lazy val fut_7 = pro.future

  // use this to create successful/failed Future
  def get_mean(sum: Int, len: Int) = Future { sum / len }
  def get_num(v: Int, ms: Int) = Future { Thread.sleep(ms); v }
  def get_error(ms: Int) = Future { Thread.sleep(ms); 25 / 0 }

  /*
  *    Test functions
  * */
  def tran_flatmap() =
    for {
      x <- fut_0
    } yield x / 2

  def sequencing() =
    for {
      x <- Future { Thread.sleep(3000); 42 }
      y <- Future { Thread.sleep(2000); 2 }
    } yield x * y

  // Q: filter() vs withFilter() ??
  def filter_test() = fut_0.filter( _ % 2 != 0)

  // collect()
  val fut_8 = fut_0.collect {
      case v if v % 2 != 0 => v
    }

  // test recovery
  def recover_test() = {
    val mayFailed: Future[Int] = get_mean(56, 0)
    val recovered = mayFailed.recover {
      case _: ArithmeticException => -1
      //      case _: IllegalArgumentException => -2
    }

    val recovered2 = mayFailed.recoverWith {
      case _: ArithmeticException => Future { Thread.sleep(2000); -1}
    }

    Thread.sleep(3000)
    println( mayFailed.value )
    println( recovered.value )
    println( recovered2.value )
  }

  // transform / transformWith
  def transform_test() = {
    val f1 = get_mean(34, 0).transform(
      m => s"mean value = $m",
      ex => ex
    )

    val f2 = get_mean(34, 0).transform {
      case Success(m) => Success( s"mean value = $m" )
      case Failure(_) => Success( "-1" )
    }

    val f3 = get_num(100, 2000).transformWith {
      case Success(v) => get_mean(v, 5)
      case Failure(_) => Future( -1 )
    }

    Thread.sleep(3000)
    println( f1.value )
    println( f2.value )
    println( f3.value )
  }

  // zip / zipWith
  def zip_test() = {
    val cnt = get_num(120, 1000)
    val sum = get_num(3010, ms = 2000)
    val err = get_error(2000)

    val f = cnt.zip(sum)
    val meanV = sum.zipWith(cnt)(_ / _)
    val meanv2 =
      for {
        s <- sum
        c <- cnt
      } yield s / c


    Thread.sleep(2500)
    println( f.value )
    println( meanV.value )
    println( meanv2.value )
  }

  // foldLeft / reduceLeft / sequence
  def fold_test() = {
    val parCounts = List(
      get_num(20, 1000),
      get_num(120, 2000),
      get_num(85, 500),
//      get_error(100),
      get_num(99, 600)
    )

    val finalCnt1 = Future.foldLeft(parCounts)(0)(_ + _)
    val finalCnt2 = Future.reduceLeft(parCounts)(_ + _)
    val finalCnt3 = Future.sequence(parCounts).map(_.sum)
    Thread.sleep(2500)
    println( finalCnt1.value )
    println( finalCnt2.value )
    println( finalCnt3.value )
  }

  // traverse
  def traverse_test() = {
    val f: Future[List[Int]] = Future.traverse(List(300, 2000, 950, 420))(get_num(_, 1000))

    // total execution time will be sum of each Future
    Thread.sleep(5000)
    println( f.value )
  }

  // foreach / onComplete / andThen
  def sideeffect_test() = {
    val f = get_num(100, 1000)
    f.foreach( v => println( s"number: $v comes out of the future" ))
    f.onComplete {
      case Success(v) => println("number is : " + v)
      case Failure(_) => println( "error!" )
    }

    Thread.sleep(1500)
    println("what about here ???")
  }

  // flatten
  /* Future[Future[T]]: means future after future !!!!
  *  imagine: I (not married)    ------>  (married)  --------> (have a child)
  *  So, 'future child' is after 'future marriage'
  * */
  def flatten_test() = {
    val signedFunds: Future[Int] = get_num(5000, 5000)

    val availableFuncs = Future {
      Thread.sleep(2000)
      signedFunds
    }

    Thread.sleep(5000)
    println( availableFuncs.flatten.value )
  }

  // block to get result: good for testing
  def await_test() = {
    import scala.concurrent.Await
    import scala.concurrent.duration._
    val ret = Await.result(get_num(500, 3000), 10.seconds)
    println ("final result = " + ret)
  }
  /*
  *   main function
  * */
  def main(args: Array[String]): Unit = {
    println("c32.ScalaSideTest starts ...")

//    recover_test()
//    transform_test()
//    zip_test()
//    fold_test()
//    traverse_test()
//    sideeffect_test()
//    flatten_test()
    await_test()
  }


}
