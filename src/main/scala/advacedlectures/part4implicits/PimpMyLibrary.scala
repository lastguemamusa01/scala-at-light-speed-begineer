package advacedlectures.part4implicits

object PimpMyLibrary extends App {

  // enrichment - allow us to decorate existing classes that we may not have access to with additional methods and properties

  // 2.isPrime

  /*
  tips
  * keep type enrichment to implicit classes and type classes
  * avoid implicit defs as much as possible
  * package implicits clearly, bring into scope only what you need
  * if you need type conversions, make them specific

   */

  implicit class RichInt(val value: Int) extends AnyVal {  // extends AnyVal for memory optimization
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)

    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if(n <= 0) () // () -> return the unit
        else {
          function()
          timesAux(n-1)
        }
      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n : Int): List[T] =
        if(n <= 0) List()
        else concatenate(n - 1) ++ list

      concatenate(value)
    }


  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }

  new RichInt(42).sqrt

  // but this is implicit class, so we can call like thi
  42.isEven // this call type enrichment or called as pimping -> new RichInt(42).isEven

  // using pimping we can use something like this
  1 to 10 // to method belongs to RichInt class which is from Scala Package

  import scala.concurrent.duration._
  3.seconds  // seconds is implicit methods

  // compiler doesn't do multiple implicit searches

  // 42.isOdd // this will not going to work, compiler show error, cannot wrap twice

  /*
   Enrich the String class
   - asInt
   - encrypt
     Jhon-> use Caesar cipher with 2 characters -> Lqjp

   keep enriching the Int class
   - times(function)
   3.times(() => ...)
   - *
   3 * List(1,2) => List(1,2,1,2,1,2)
   */

  implicit class RichString(string: String) {
    def asInt: Int = Integer.valueOf(string)  // return java.lang.Integer -> scala conver to Int
    def encrypt(cypherDistance: Int): String = string.map(char => (char + cypherDistance).asInstanceOf[Char])
  }

  println("3".asInt + 4)
  println("John".encrypt(2))


  3.times(() => println("scala rocks"))
  println(4 * List(1,2))

  // "3" / 4 -> like javasc ript

  implicit def stringToInt(string: String): Int = Integer.valueOf(string)

  println("6"/2) // stringToInt("6") / 2

  // equivalent: implicit class RichAltInt(value: Int)
  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value) // this is more powerful(implicit conversions with methods
  // are in general more powerful, but this is discouraged.


  // danger zone


  implicit def intToBoolean(i: Int): Boolean = i == 1

  /*
  if (n) do something
  else do something else
   */

  val aConditionValue = if(3) "OK" else "something wrong"  // something wrong -> because the implicit intToBoolean is true
  // only when the number is 1
  println(aConditionValue)

  // if there is a bug in an implicit conversion with a method, it is super, super hard to race it back

  // implicit methods tend to be part of library packages, which are generally in a very different part
  // of the code base and likely written by different people


}
