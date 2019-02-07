package scalabook.c30

trait Tree[+T] {
  def elem: T
  def left: Tree[T]
  def right: Tree[T]
}

object EmptyTree extends Tree[Nothing] {
  def elem = throw new NoSuchElementException("EmptyTree.elem")
  def left = throw new NoSuchElementException("EmptyTree.left")
  def right = throw  new NoSuchElementException("EmptyTree.right")
}

class Branch[+T](
  val elem: T,
  val left: Tree[T],
  val right: Tree[T]
) extends Tree[T] {

  override def equals(obj: Any): Boolean = obj match {
    case that: Branch[_] => that.canEqual(this) &&
                            elem == that.elem &&
                            left == that.left &&
                            right == that.right
    case _ => false
  }

  override def hashCode(): Int = (elem, left, right).##

  def canEqual(other: Any): Boolean = other.isInstanceOf[Branch[_]]

}

object TreeTest {

  def main(args: Array[String]) = {
    val b1 = new Branch[ List[String] ](Nil, EmptyTree, EmptyTree)
    val b2 = new Branch[ List[Int] ](Nil, EmptyTree, EmptyTree)

    println( b1 == b2 )
  }
}