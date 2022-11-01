package com.rockthejvm

object PatternMatching extends App{

  // pattern matching -> equivalent is java switch
  val anInteger = 55
  // instead of using if else
  // pattern match is an expression
  val order = anInteger match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => anInteger+"th"  // _ is default
  }

  println(order)

  // PM(pattern matching) - also deconstruct data structures into its constituent parts

  case class Person(name: String, age: Int) // case class dont need to be instantiated, becuase has
  // a companion object with an apply method

  val bob = Person("Bob", 43) // Person.apply("Bob",43)

  // patern matching available for case classes
  // case class decomposition
  val personGreeting = bob match {
      // s -> interpolated string
    case Person(name, age) => s"Hi, my name is $name and i am $age years old." // match the structure(Person) reuse name and age
    case _ => "Something else"
  }

  println(personGreeting)

  // deconstructing tuples
  val aTuple = ("Bon Jovi", "Rock")
  val bandDescription = aTuple match {
    case (band, genre) => s"$band belongs to the genre $genre"
    case _ => "I don't know what you're talking about"
  }


  // decomposing lists

  val aList= List(1,2,3)
  val listDescription = aList match {
    case List(_,2,_) => "List containing 2 on its second position"  // _ this mean anything
    case _ => "Unknown list"
  }

  // if PM doesn't match anything, it will throw a MatchError, so case _ is not mandatory but use it

  // Pm will try all cases in sequence -> this going from first and second, with order. case List(_,2,_) then case _

}
