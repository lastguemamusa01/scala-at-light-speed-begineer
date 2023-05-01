package lectures.part3fp

object AnonymousFunctions extends App {

  // problem is that instantiating a function is still very much tied to the object oriented way

  // this is still object oriented way of defining an anonymous function and instantiating it on the spot
  val doubler = new Function[Int, Int] {
    override def apply(x: Int): Int = x*2
  }
  // in scala, syntatic sugar for above anonymous function
  // anonymous function (lambda -> from lambda functions by alonzo church)
  val doublerAnonymous = (x: Int) => x * 2
  val doublerAnonymous2: Int => Int = x => x * 2

  // multiple params in a lambda

  val adder = (a: Int, b: Int) => a + b
  val adder2: (Int, Int) => Int =  (a,b) => a + b

  // no params
  val justDoSomething = () => 3  // just return value 3
  val justDoSomething2: () => Int = () => 3

  // very careful
  println(justDoSomething2) // lectures.part3fp.AnonymousFunctions$$$Lambda$7/942731712@17d99928v  -> function itself
  println(justDoSomething2())  // 3 -> this is call

  // when you call lambda, you must call with parentheses

  // curly braces with lambdas
  // this style sometime is used
  val stringToInt = { (str: String) =>
    str.toInt
  }

  // MOAR syntactic sugar
  val niceIncrementer: Int => Int = _ + 1  // equivalent to x => x + 1
  val niceAdder: (Int, Int) => Int = _ + _ // equivalent (a, b) => a + b

  /*
   1. MyList: replace all FunctionX call with lambdas -> LambdaMyList.scala
   2. Rewrite the "special" adder as anonymous function
   */

  // 2 -> lambda version of the curried function
  /*
    val superAdder: Function1[Int, Function1[Int,Int]] = new Function1[Int, Function1[Int,Int]] {
    override def apply(x: Int): Function1[Int,Int] = new Function[Int,Int] {
      override def apply(y: Int): Int = x + y
     }
   }
   */
  // much more elegant and shorter
  val superAdd = (x: Int) => (y: Int) => x + y

  println(superAdd(3)(4))

}
