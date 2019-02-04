import org.scalatest._
import org.scalatest.concurrent.ScalaFutures._

import scala.concurrent.Future

class FutureSpec extends FlatSpec with Matchers {

  "testing" should "block for the value" in {
    import scala.concurrent.ExecutionContext.Implicits.global

    val fut = Future { Thread.sleep(100); 55 }

    // futureValue will block 150-ms(by default) trying to get result
    fut.futureValue should be (55)
  }
}

// 'AsyncFunSpec' is added since ScalaTest 3.0 to test 'asynchronous computations'
class AddSpec extends AsyncFunSpec {

  def getSum(xs: Int*): Future[Int] = Future { Thread.sleep(10000); xs.sum }

  describe("getSum") {
    it("will eventually compute a sum of passed Ints") {
      val f: Future[Int] = getSum(34, 58, 9, 19, 302, -66)
      f.map { s => assert(s == List(34, 58, 9, 19, 302, -66).sum) }
    }
  }
}
