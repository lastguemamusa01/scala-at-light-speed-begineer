package advacedlectures.part2afp

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int,Int] === (sugar type) Int => Int -> like math functions

  val aFussyFunction = (x: Int) => // little bit cluncky
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  // pattern matching

  val aNicerFussyFunction = (x: Int) => x match { // proper function
    case 1 => 42  // this will throw MatchError if there is no match, so don't need throw Exception
    case 2 => 56
    case 5 => 999
  } // {1,2,5} => Int -> 1,2,5 is subset of Int, so you can use partial functions

  // partial functions

  val aPartialFunction: PartialFunction[Int, Int] = { // only accet Int what declared in PartialFunction
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value

  println(aPartialFunction(2)) // this invoke apply member of the PartialFunction
  // println(aPartialFunction(24235)) // this will crash -> with MatchError because PartialFunction is based on pattern matching

  // partial function utilities

  println(aPartialFunction.isDefinedAt(67))  // false -> check if 67 value is available in the case

  // lift
  val lifted = aPartialFunction.lift // Int => Option[Int] -> partial function convert to total function
  println(lifted(2)) // Some(56)
  println(lifted(98)) // None

  val pfChain = aPartialFunction.orElse[Int,Int] { // orElse take another partial functions
    case 45 => 67
  }

  println(pfChain(2)) // 56
  println(pfChain(45)) // 67

  // PF(Partial Functions) extends from normal Functions
  val aTotalFunction: Int => Int = {  // in normal function i can assign partial function, becuase PF is subtype of total functions
    case 1 => 99
  }

  // HOFs(High Order Functions) accept partial functions as well

  val aMappedList = List(1,2,3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }

  println(aMappedList) // List(42, 78, 1000)

  /*
  Note : PF can only have one parameter type
   */

  /*
  Excercises

  1 - contruct a PF instance yourself (anonymous class) trait
  2 - small  dumb chatbot as a PF
  3 -
   */

  // read from console

  /*
  scala.io.Source.stdin.getLines().foreach(line => println("you said: " + line))
  when your write in console : hello
  what print in console -> you said: hello
  */

  // 1 - contruct a PF instance yourself (anonymous class) trait
  val aManualFussyFunction = new PartialFunction[Int,Int] {
    override def apply(x: Int): Int = x match {
      case 1 => 42
      case 2 => 56
      case 5 => 999
    }
    override def isDefinedAt(x: Int): Boolean =  // is defined for this argument
    x == 1 || x == 2 || x == 5
  }

  // 2 - small  dumb chatbot as a PF

  val chatbot: PartialFunction[String, String] = {
    case "hello" => "Hi, my name is HAL9000"
    case "goodbye" => "Once you start talking to me, there is no return, human!"
    case "call mom" => "Unable to find your phone without your credit card"
  }

  // scala.io.Source.stdin.getLines().foreach(line => println("chatbot says: " + chatbot(line)))
  scala.io.Source.stdin.getLines().map(chatbot).foreach(println)
  // scala.io.Source.stdin.getLines() -> all the line i write
  // .map(chatbot) -> i put them throught the chatbot PartialFunction
  // .foreach(println) - the response i println

  // Sequences are callable trhough an integer index
  // Maps are callable through their keys
  /*
  val numbers = List(1,2,3)
  numbers(1) // 2
  numbers(3) // java.lang.IndexOutOfBoundsException

  Seqs are partially defined on the domain [0, ..., length-1]
  sequences are partial funcions !

  val phoneMappings = Map(2 -> "ABC", 3 -> "DEF")
  phoneMappings(2) // "ABC"
  phoneMappings(1) // java.lang.NoSuchElementException

  A map is defined  on the domain of its keys
  Maps are partial functions.

  */

}
