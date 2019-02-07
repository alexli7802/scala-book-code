package scalabook.c30

import scala.collection.mutable

/* Key Notes:
*  - Java uses "==" for value types and object reference, but 'equals' for object content!
*  - Scala uses final "==" to delegate the comparison to overriden-able'equals' or 'eq'!
*    (analogy - identical twins. They look exactly the same, but they're different individuals.)
*  - 'equals' and 'hashCode' MUST be overriden together, and 'canEqual'
*    (when 2 objects equal to each other, their 'hashCode' MUST return the same integer code!)
*  - in a class hierarchy, it's considered too strict to require 'this.getClass == that.getClass',
*    as a solution, 'canEqual' is checked in 'equal()', refer to 'Point' <|-- 'ColoredPoint'
*  - generally, when sub-class redefine 'equals', 'hashCode', 'canEqual', then we cannot make it
*    compare to its super-class.
*  - In the 'Tree[T]' example, compare 2 branches will not take [T] into account,
*    because type-parameter will be eliminated at runtime. ( critical fact! )
*
*  - Best Practice
*    It's surprisingly difficult to get the equality method right, so it's a good idea to use
*    'case class' instead of 'class', as Scala compiler will override 'equals' and 'hashCode'
*    for you~~~
* */

class Point_1(val x: Int, val y: Int) {

  // an overloaded 'equals'
  def equals(other: Point_1): Boolean = ( this.x == other.x && this.y == other.y )
}

class Point_2(val x: Int, val y: Int) {

  // an overriden 'equals'
  override def equals(obj: Any): Boolean =
    obj match {
      case p: Point_2 => this.x == p.x && this.y == p.y
      case _ => false
    }

}

class Point(val x: Int, val y: Int) {

  override def hashCode(): Int = (x, y).##

  override def equals(obj: Any): Boolean =
    obj match {
      case that: Point => this.x == that.x && this.y == that.y && this.getClass == that.getClass
      case _ => false
    }
}

// sub-type
object Color extends Enumeration {
  val Red, Orange, Yellow, Green, Blue, Indigo, Violet = Value
}

class ColoredPoint_1(x: Int, y: Int, val color: Color.Value) extends Point(x, y) {

  override def equals(obj: Any): Boolean = obj match {
    case that: ColoredPoint_1 => this.color == that.color && super.equals(that)
    case _ => false
  }
}

class ColoredPoint_2(x: Int, y: Int, val color: Color.Value) extends Point(x, y) {

  override def equals(obj: Any): Boolean = obj match {
    case that: ColoredPoint_2 => this.color == that.color && super.equals(that)
    case that: Point => that.equals(this)
    case _ => false
  }
}

object ScalaEquality {

  import scala.collection.mutable

  def test_color_point_2() = {
    val (p, cp) = ( new Point(1,2), new ColoredPoint_2(1,2,Color.Red) )

    assert( p != cp && cp != p )      // 'equals' is symmetric now !!!!

//    val cp2 = new ColoredPoint_2(1,2,Color.Blue)
//    assert( cp == p && cp2 == p )
//    assert( cp != cp2 )               // 'equals' is not transitive, BROKEN !

  }

  def test_color_point_1() = {
    val p = new Point(1,2)
    val cp = new ColoredPoint_1(1,2,Color.Red)
    val cp2: Point = new ColoredPoint_1(1,2,Color.Blue)

    assert( p == cp )     // sub-type is accepted in 'case : Point =>'
    assert( cp != p )     // sup-type is not matched in 'case : ColoredPoint =>'
    assert( cp2 != p )    // why?  ( runtime type of cp2 is 'ColoredPoint_1' )
  }


  def test_point() = {
    val p1, p2 = new Point(1, 2)
    assert( p1 == p2 )

    val points = mutable.HashSet(p1)
    assert( points.contains(p2) )
  }

  def test_point_1() = {

    val p1, p2 = new Point_1(1,2)
    assert( p1.equals(p2) == true)
    assert( p1 != p2 )                  // unexpected: p1 != p2
  }

  def test_point_2() = {
    val p1, p2 = new Point_2(1,2)
    assert( p1 == p2 )                       // now is good!

    val points = mutable.HashSet(p1)
    assert( points.contains(p2) == false )   // Oh, NO~~~~ Why ??
  }

  def main(args: Array[String]) = {

    println("c30.ScalaEquality starts ...")
//    test_point_1()
//    test_point_2()
//    test_point()
//    test_color_point_1()
    test_color_point_2()
  }


}
