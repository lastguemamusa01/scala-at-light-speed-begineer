package lectures.part4pm

object PatternsEverywhere extends App {

  // big idea #1
  try {
    // code
  } catch {
    case e: RuntimeException => "runtime"
    case npe: NullPointerException => "npe"
    case _ => "something else"
  }

  // looks odd try catch after learning pattern matching

  // catches are actually matches

  /* scala pseudocode

    try {
      // code
    } catch (e) {
      e match {
        case e: RuntimeException => "runtime"
        case npe: NullPointerException => "npe"
        case _ => "something else"
      }
    }
  */

  // big idea #2
  val list = List(1,2,3,4)
  val evenOnes = for {
    x <- list if x % 2 == 0 // this generator , odd , now you are know pattern matching
  } yield 10 * x

  // generators are also based on Pattern Matching
  val tuples = List((1,2), (3,4))
  // you can actually iterate over those tuples with pattern matching
  val filterTuples = for {
    (first, second) <- tuples
  } yield  first * second
  // case classes,  :: operators, ...

  // big idea # 3

  val tuple = (1,2,3)
  val (a,b,c) = tuple // assign multiple values by exploiting the name binding property of pattern matching
  // a is 1 , b is 2 and c is 3

  println(b) // 2
  // multiple value definitions based on pattern matching
  // not limited to tuple, all the power is available

  val head :: tail = list
  println(head) // 1
  println(tail) // List(2, 3, 4)

  // big idea # 4
  // partial function -> based on pattern matching

  val mappedList = list.map {
    case v if v % 2 == 0 => v + " is even"
    case 1 => "the one"
    case _ => "something else"
  } // partial function literal

  // equivalent mappedList with mappeList2
  val mappedList2 = list.map { x =>
    x match {
      case v if v % 2 == 0 => v + " is even"
      case 1 => "the one"
      case _ => "something else"
    }
  }

  println(mappedList) // List(the one, 2 is even, something else, 4 is even)

}
