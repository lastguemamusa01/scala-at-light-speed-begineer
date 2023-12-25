package advacedlectures.part4implicits

object ExtensionMethods extends App {


  case class Person(name: String) {
    def greet(): String = s"Hi, my name is $name"
  }

  // scala 3 - extension -> pimping your library(scala 2)

//  extension (string: String) { // extension method
//    def greetAsPerson(): String = Person(string).greet()
//  }

  // val minGreeting = "Min".greetAsPerson()

  // scala 3 extension methods <==> implicit classes in scala 2

  object Scala2ExtensionMethods {
    implicit class RichInt(val value: Int) { // extends AnyVal for memory optimization
      def isEven: Boolean = value % 2 == 0

      def sqrt: Double = Math.sqrt(value)

      def times(function: () => Unit): Unit = {
        def timesAux(n: Int): Unit =
          if (n <= 0) () // () -> return the unit
          else {
            function()
            timesAux(n - 1)
          }

        timesAux(value)
      }
    }
  }


  //val is3Evwen = 3.isEven // new RichInt(3).isEven

//  extension (value: Int) {
//    // define all methods
//    def isEven: Boolean = value % 2 == 0
//
//    def sqrt: Double = Math.sqrt(value)
//
//    def times(function: () => Unit): Unit = {
//      def timesAux(n: Int): Unit =
//        if (n <= 0) () // () -> return the unit
//        else {
//          function()
//          timesAux(n - 1)
//        }
//
//      timesAux(value)
//    }
//
//  }

  // generic extensions
//  extension [A](list: List[A]) {
//    def ends: (A, A) = (list.head, list.last)
//    def extremes(using ordering: Ordering[A]): (A, A) = list.sorted.ends // i can call an extesion method here that is inside
//  }


}
