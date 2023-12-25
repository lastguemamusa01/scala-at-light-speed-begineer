package advacedlectures.part5ts

object RockingInheritance extends App {

  // convenience

  // api library
  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Closeable {
    def close(status: Int): Unit
  }

  trait GenericStream[T] {
    // some methods
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    // Whenever we don't know who exactly mixes in our specific traits
    // we can use them all in a specific type as a parameter to a method
    stream.foreach(println)
    stream.close(0)

  }

  // inheritance - diamond problem
  trait Animal { def name: String }
  trait Lion extends Animal { override def name: String = "lion" }

  trait Tiger extends Animal { override def name: String = "Tiger" }

  class Mutant extends Lion with Tiger

  val m = new Mutant
  println(m.name) // tiger

  /*
  Mutant
  extends Animal with { override def name: String = "lion" }
  with { override def name: String = "Tiger" }

  last override gets picked
   */


  // 3 - the super problem + type linearization
  trait Cold {
    def print = println("cold")
  }

  trait Green extends Cold {
    override def print(): Unit = {
      println("green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print(): Unit = {
      println("blue")
      super.print
    }
  }

  class Red {
    def print = println("red")
  }

  class White extends Red with Green with Blue {
    override def print(): Unit = {
      println("white")
      super.print
    }
  }

  val color = new White
  color.print
  /*
  white
  blue
  green
  cold
   */

  // type linearization -> = AnyRef with Red with Cold with Green with Blue with White
  // white call super that is blue, blue super is green, green super is cold


}
