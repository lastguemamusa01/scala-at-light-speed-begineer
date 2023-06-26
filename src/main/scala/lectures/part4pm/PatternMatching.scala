package lectures.part4pm

import scala.util.Random

object PatternMatching extends App {

  // swtich on steroids

  val random = new Random
  val x = random.nextInt(10) // 0 to 10

  val description = x match {
    case 1 => "the One"
    case 2 => "double or nothing"
    case 3 => "third time is the charm"
    case _ => "something else" // _ is wildcard(anything) -> default
  }

  println(x)
  println(description)

}
