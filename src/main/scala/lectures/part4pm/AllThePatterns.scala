package lectures.part4pm

import exercises.CaseClassMyList
import exercises.CaseObjectEmpty
import exercises.CaseClassCons

object AllThePatterns extends App {

  // 1 - constants
  val x: Any = "Scala"
  val constants = x match {
    case 1 => "a number"
    case "Scala" => "The Scala"
    case true => "The truth"
    case AllThePatterns => "A singleton object"
  }

  // 2 - match anything
  // 2.1 - wildcard
  val matchAnything = x match {
    case _ =>
  }

  // 2.2 variable
  val matchAVariable = x match {
    case something => s"I've found $something"  // something is any value, use that value later in the return expression
  }

  // 3 - tuples(data structure)
  val aTuple = (1,2)
  val matchATuple = aTuple match {
    case (1,1) =>   // (1,1) literal tuple
    case (something, 2) => s"I've found $something" // tuple composed of nested patterns,
  }

  val nestedTuple = (1, (2, 3))
  val matchAnNestedTuple = nestedTuple match {
    case (_, (2, v)) =>   // nested pattern
  }

  // PMs can be nested

  // 4 - cae classes - contructor pattern
 // PMs can be nested with case classes as well
  val aList: CaseClassMyList[Int] = CaseClassCons(1, CaseClassCons(2, CaseObjectEmpty))
  val matchAList = aList match {
    case CaseObjectEmpty =>
    case CaseClassCons(head, tail) => // head and tail are variables
    case CaseClassCons(head, CaseClassCons(subhead, subtail)) =>  // case classes also can be nested
  }

  // 5 - list patterns

  val aStandardList = List(1,2,3,42)
  val starndarListMatching = aStandardList match {
    case List(1,_,_,_) => // extractor, List is not case class but list exist extractor
    case List(1,_*) => // *_ -> underscore start => Varg pattern -> list of arbitrary length - advanced
    case 1 :: List(_) => // infix pattern(::), _ -> something
    case List(1,2,3) :+ 42 => // infix pattern(:+)
  }

  // 6 - type specifiers

  val unknown: Any = 2
  val unknowMatch = unknown match {
    case list: List[Int] => // : -> type specifiers -> explicit type specifier
    case _ =>
  }

  // 7 - name binding
  val nameBindingMatch = aList match {
    case nonEmptyList @ CaseClassCons(_,_) => // nonEmptyList @ -> at, name binding => use the name later(here) in return
    case CaseClassCons(1, rest @ CaseClassCons(2, _)) => // name biding inside nested patterns
  }

  // 8 - multi-patterns, you can chain many pattern as you want using pipe operator

  val multipattern = aList match {
    case CaseObjectEmpty | CaseClassCons(0, _) =>    // | -> or(pipe operator)  , compound pattern(multipattern)
    case _ =>
  }

  // 9 - if guards
  val secondElementSpecial = aList match {
    case CaseClassCons(_, CaseClassCons(specialElement, _)) if specialElement % 2 == 0 => // if specialElement % 2 == 0
  }

  // All

  /*
  Questions
   */

  val numbers = List(1,2,3)
  val numbersMatch = numbers match {
    case listOfStrings: List[String] => "a list of strings"
    case listOfNumbers: List[Int] => "a list of numbers"
    case _ => ""
  }


  println(numbersMatch) // a list of strings  -> wtf
  // JVM trick question

  // java 1 don't have Generics, Generics introduced in java 5
  // java after type checking erase all genercis -> this issues also happen in scala
  /*
  after type checking, pattern matching looks like this
   case listOfStrings: List => "a list of strings"
    case listOfNumbers: List => "a list of numbers"

    this is call type erasure -> ide warns you about this issue
   */




}
