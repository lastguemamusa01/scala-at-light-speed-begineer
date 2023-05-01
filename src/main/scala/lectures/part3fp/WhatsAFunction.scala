package lectures.part3fp

object WhatsAFunction extends App  {

  // use functions as first class elements
  // pass functions as parameters or use functions as values

  // first class element is objects - instances of classes in java, so all scala functions are objects (Function1..22)

  // we have function traits, up to 22 params
  // syntactic sugar function types ->   Function2[Int, String, Int] === (Int,String) => Int

  val doubler = new MyFunction[Int, Int] {
    override def apply(element: Int): Int = element * 2
  }

  println(doubler(2)) // scala, you can call like function

  // function types = Function1[A,B] ... Function22

  val stringToIntConverter = new Function1[String, Int] {
    override def apply(string: String): Int = string.toInt
  }

  println(stringToIntConverter("3")+4)

  val adder: ((Int,Int) => Int) = new Function2[Int, Int, Int] { // can support 22 parameters, ((Int,Int) => Int) syntatic sugar for Function2
    override def apply(a: Int, b: Int): Int = a+b
  }

  // Function types Function2[A,B,R] === (A,B) => R
  // All scala functions are objects or instances of classes of function1..22

  /*
  1. a function which takes 2 strings and concatenates them
  2. transform the MyPredicate and MyTtransformer into function types
  3. define a function which thakes an int and return another function which takes an int and returns an int
      - what is the type of this function
      - how to do it
  */


  // 1.
  def concatenator: (String,String) => String = new Function2[String,String,String] {
    override def apply(a: String, b: String): String = a+b
  }
  println(concatenator("hello ","scala"))

  // 2 -> FunctionMyList.scala

  // 3 -> high order function
  // Function1[Int, Function1[Int,Int]]

  val superAdder: Function1[Int, Function1[Int,Int]] = new Function1[Int, Function1[Int,Int]] {
    override def apply(x: Int): Function1[Int,Int] = new Function[Int,Int] {
      override def apply(y: Int): Int = x + y
    }
  }

  val adder3 = superAdder(3)
  println(adder3(4))

  println(superAdder(3)(4))   // this is same as above name curried function
  // curried function - have property that they can be called with multiple parameter lists just by mere definitions

  println(superAdder(3))


}

// function traits example
trait MyFunction[A,B] {
  def apply(element: A): B
}
