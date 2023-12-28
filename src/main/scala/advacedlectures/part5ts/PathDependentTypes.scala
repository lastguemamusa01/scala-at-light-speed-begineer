package advacedlectures.part5ts

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)
    // Outer#Inner
    def printlnGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String // type is used as aliases outside of class and trait
    2
  }

  // path dependent types

  // per-instance

  val o = new Outer
  // val inner = new Inner // inner not exist, only exist in the context of Outer -> this doesn't compile

  val inner = new o.Inner // o.Inner is a TYPE

  val oo = new Outer
  // val otherInner: oo.Inner = new o.Inner // doesn't compile, these are 2 different instances

  o.print(inner)
  // oo.print(inner) // this doesn't compile, they are different type

  // Outer#Inner
  o.printlnGeneral(inner)
  oo.printlnGeneral(inner)  // this is valid

  /*
  Exercise
  DB keyed by Int or String, but maybe others key will be expanded at the future

  use path-dependent types
  abstract type members and/or type aliases
   */

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  // def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???  // contrainted or upper bounded by ItemLike

  // get[IntItem](42) // ok
  // get[StringItem]("home") // ok

  // get[IntItem]("scala" ) // not ok







}
