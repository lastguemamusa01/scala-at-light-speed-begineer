package com.rockthejvm

object FunctionalProgramming extends App {

  //Scala is Object Oriented
  class Person(name: String) {
    def apply(age: Int) = println(s"I have aged $age years")
  }

  val bob = new Person("Bob")
  bob.apply(43)
  bob(43) // invoking bob as a function === bob.apply(43)
  // apply method, allows an instance of a class to be invoked like a function

  /*
  Scala runs on the JVM
  Functional programming - we want to work with functions as what we call
  first class elements of programming - work with function like any kind of values(like objects or plain values)
  - compose functions
  - pass functions as args
  - return functions as results

  scala invented FunctionX(traits) =  Function1, Function2 ... Function22 - 22 maximum argument you can pass to a function
   */

  val simpleIncrementer = new Function1[Int,Int] { // Function1 is plain trait with apply method
    override def apply(arg: Int): Int = arg+1
  }

  simpleIncrementer.apply(23) // 24
  simpleIncrementer(23) // same as simpleIncrementer.apply(23)
  // definied a function!

  // all scala functions are instances of these function_x types

  val stringConcatenator = new Function2[String, String, String] { // third type is the return type
    override def apply(arg1: String, arg2: String) :String = arg1+arg2
  }

  stringConcatenator("I love"," Scala") // "I love Scala"

  // syntax sugars - alternative syntax that will replace these much heavier boilerplate code
  val doubler2: Function1[Int,Int] = (x: Int) => 2*x
  val doubler: Int => Int = (x: Int) => 2*x  // you can ommit Int => Int
  val doubler3 = (x: Int) => 2*x
  /*
  Function1[Int,Int] is equivalent  Int => Int
  equivalent to much longer :
  val doubler: Function1[Int,Int] = new Function1[Int,Int] {
    override def apply(x: Int) = 2 * x
  }
   */

  doubler(4) // 8

  // higher-order functions : take functions as args/return functions as results or both
  val aMappedList: List[Int] = List(1,2,3).map(x => x+1)   //map special method, allows passing a function
  // x => x+1 anonymous functions is pass as arguments, map is higher-order functions
  println(aMappedList)  // another list

  val aFlatMappedList = List(1,2,3).flatMap(x => List(x, 2*x))  //HOF(high order functions) -> 3 Lists, flatmap concat 3 list to one big list
  val aFlatMappedListAlternativeSyntax = List(1,2,3).flatMap { x =>
    List(x, 2*x)
  }

  println(aFlatMappedList)  // List(1, 2, 2, 4, 3, 6)

  val aFilteredList = List(1,2,3,4,5).filter(x => x <= 3)
  val aFilteredListAlternativeSyntax = List(1,2,3,4,5).filter(_ <= 3) // _ equivalent x => x
  println(aFilteredList)

  // all pairs between the numbers 1,2,3 and the letters 'a','b','c'

  // s is interpolated string
  val allPairs = List(1,2,3).flatMap(number => List('a','b','c').map(letter => s"$number-$letter"))

  println(allPairs)

  // for comprehensions - for more human redable chains of maps, flatmaps, filters
  // for comprehension is not equal for

  val alternativeAllPairs = for{
    number <- List(1,2,3)
    letter <- List('a','b','c')
  } yield s"$number-$letter"
  // equivalent to map/flatmap chain above
  // collection - work parallel or distributed enviroment with spark or resilent distributed datasets
  // linear, multidimensional or parallel distributed collections

  println(alternativeAllPairs)

  /*
  Collections - work for collections like map, flatmap and filter
  */

  // lists
  val aLists = List(1,2,3,4,5)
  val firstElement = aLists.head // 1
  val restElement = aLists.tail // 2,3,4,5
  val aPrependedList = 0 :: aLists   // :: applicable to list, to prepend ::   -> 0, 1, 2, 3, 4, 5
  val anExtendedList = 0 +: aLists :+ 6 // 0,1,2,3,4,5,6

  //sequences
  val aSequence: Seq[Int] = Seq(1,2,3) // Seq.apply(1,2,3) - have companion object that has apply method,
  // Seq is treat a abstract type, Sequence is that you can access an element at a given index
  val accessedElement = aSequence(1) // index 1 -> value 2

  // Vectors - particular type of sequence, which is very fast for large data
  // same methods as sequence and lists
  val aVectors = Vector(1,2,3,4,5)


  // sets - collection with no duplicate
  // uset to if cotains element or not
  // unordered
  val aSet = Set(1,2,3,4,1,2,3) // 1,2,3,4
  val setHas5 = aSet.contains(5) // false
  val anAddedSet = aSet + 5 // 1,2,3,4,5
  val aRemovedSet = aSet - 3 // 1,2,4

  // ranges
  val aRange = 1 to 1000
  val twoByTwo = aRange.map(x => 2 * x).toList // List(2,4,6,8..., 2000) , toList to convert the sequence to list

  //tuples - groups of values under the same value
  val aTuple = ("Bon Jovi", "Rock", 1982) // bon jovi, rock, 1982 information is group by aTuple

  //maps
  val aPhoneBook: Map[String, Int] = Map(
    ("Daniel", 25325),
    "Jane" -> 235235   // ("Jane", 235235)  -> tuple
  )

}
