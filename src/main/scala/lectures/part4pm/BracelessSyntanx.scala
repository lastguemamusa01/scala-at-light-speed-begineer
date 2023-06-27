package lectures.part4pm

object BracelessSyntanx {

  // scala 3 languages - introduces bunch of alternative structures to the common expressions
  // if - expressions

  val anIfExpression = if (2 > 3) "bigger" else "smaller" // one liner style

  // java-style -> with curly braces
  val anIfExpression_V2 =
    if ( 2 > 3) {
      "bigger"
    } else {
      "smaller"
    }

  // compact stype -> preferred style
  val anIfExpression_v3 =
    if(2 > 3) "bigger"
    else "smaller"

  // scala 3 - eliminate curly braces and introduced then word
  /*
  val anIfExpression_V4 =
    if 2 > 3 then
      "bigger" // higher indentation than the if part
    else
      "smaller"

  val anIfExpression_V5 =
    if 2 > 3 then
      val result = "bigger" // you need to add indentation (1 space or tab) -> signficant indentation
      result
    else
      val result = "smaller" // this is having phanton set of curly braces -> python style
      result

  // scala 3 one-liner version
  val anIfExpression_V6 = if 2 > 3 then "bigger" else "smaller"

  */

  // for comprehensions

  val aForComprehension = for {
    n <- List(1,2,3)
    s <- List("black","white")
  } yield s"$n$s"

  //scala 3 for comprehension
  /*
  val aForComprehension_v2 =
    for
      n <- List(1,2,3)
      s <- List("black","white")
    yield s"$n$s"
  */

  // pattern matching

  val meaningOfLife = 42
  val aPatternMatch = meaningOfLife match {
    case 1 => "the one"
    case 2 => "double or nothing"
    case _ => "something else"
  }

  // scala 3 pattern matching
  /*

  val aPatternMatch_V2 =
    meaningOfLife match
      case 1 => "the one"
      case 2 => "double or nothing"
      case _ => "something else"

  // or

  val aPatternMatch_V2 = meaningOfLife match
    case 1 => "the one"
    case 2 => "double or nothing"
    case _ => "something else"

   */

  // choose scala 2 or scala 3 -> choose only one style - stick to that


  // methods without braces
  def computeMeaningOfLife(arg: Int): Int =  {
    val partialResult = 40



    partialResult + 2
  }

  // scala 3 - still valid - like phantom block
  // same identation for scala 3
  /*
  def computeMeaningOfLife(arg: Int): Int =
    val partialResult = 40



    partialResult + 2
  */

  // classes, traits, objects, Enums, data types -> scala 3 use significant identation

  class Animal {
    def eat(): Unit =
      println("I'm eating")
  }

  // scala 3
  // for class you need to add class Animal:   <- colon token
  // same indentation after Animal:
  /*
  class Animal:  // compiler expects the body of animal
    def eat(): Unit =
      println("I'm eating")

    def grow(): Unit =
      println("I'm growing")


   // end token

    class Animal:  // compiler expects the body of animal
    def eat(): Unit =
      println("I'm eating")
    end eat // end token of method -> add end token for more than 4 lines of code for methods

    def grow(): Unit =
      println("I'm growing")

    // 3000 more lines of code
    end Animal // end token

    // end token used for large if, match, for, methods, classes, traits, enums, objects
    // add end token when this is not fit in a one screen

  */

  // anonymous classes

  val aSpecialAnimal = new Animal {
    override def eat(): Unit = println("I'm special")
  }

  // scala 3
  /*
  val aSpecialAnimal = new Animal:
    override def eat(): Unit = println("I'm special")
  */

  // indentation = strictly larger indentation
  // 3 spaces + 2 tabs > 2 spaces + 2 tabs
  // 3 spaces + 2 tabs > 3 spaces + 1 tab
  // 3 tabs + 2 spaces  ??(not really comparable) 2 tabs + 3 spaces

  // either indent spaces or ident with tabs -> don't mix them

  def main(args: Array[String]): Unit = {

  }

}
