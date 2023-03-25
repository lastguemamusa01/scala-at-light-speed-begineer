package com.rockthejvm

object ObjectOrientation extends App {

  // class and instance
  class Animal {
    // define fields
    val age: Int = 0
    // define methods
    def eat() = println("I'm eating")
  }
  val anAnimal = new Animal

  class Anmal {
    val age: Int = 0
    def eat(): Unit = println("im eating")
  }

  // inheritance
  class Dog(val name: String) extends Animal // constructor definition
  val aDog = new Dog("Lassie")

  // constructor arguments (name) are Not fields
  // aDog.name   -> error, name argument is ephemeral, not visible outside the class definition
  // promete a constructor argument to filed use val ->  class Dog(val name: String)
  aDog.name

  // subtype polymorphism
  val aDeclaredAnimal: Animal = new Dog("Hachi")
  // the most derived moethod will be called at runtime
  aDeclaredAnimal.eat()  // the eat method will be called from Dog Class if Dog class overide the eat method

  // abstract class
  abstract class WalkingAnimal {
    // all fields or methods by default are public
    // scala there is not public reserved word
    // private or protected - restriction
    protected val hasLegs = true
    def walk(): Unit
  }

  // interface - ultimate abstract type in java
  // usually use traits to denote characteristics of objects that we can then later use
  // and implement in our concrete classes
  trait Carnivore {
    def eat(animal: Animal): Unit
  }

  trait Philosopher {
    def ?!(thought: String): Unit // ?! a method name, quirky as you like.....
  }

  // scala offers single class inheritance and multi-trait(called mixing) inheritance
  // class Crocodile extends Animal with Carnivore with OtherTrait
  class Crocodile extends Animal with Carnivore with Philosopher {
    // the abstract class or trait(interface) need to implemented in extended class as mandatory
    override def eat(animal: Animal): Unit = println("I am eating you, animal!")
    override def ?!(thought: String): Unit = println(s"I was thinking: $thought")
  }

  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog // infix notation(object method argument) -  only work for methods that have a single argument can be used
  aCroc ?! "What if we could fly?"

  // operators in Scala are acutally methods
  val basicMath = 1 + 2 // + is method
  val anohterBasicMath = 1.+(2) // equivalent  1 + 2

  // anonymous classes - c ++ and java - abstract type like abstract class or a unquote interface cannot be instantiated by
  // themself, and they need to be extended by concrete class
  val dinasaur = new Carnivore {
    override def eat(animal: Animal): Unit = println("I am a dinosaur so I can eat pretty much anything")
  }
  /* hey compile create me a new
    class Carnivore_Anonymous_35728 extends Carnivore {
      override def eat(animal: Animal): Unit = println("I am a dinosaur so I can eat pretty much anything")
    }

    val dinosaur = new Carnivore_Anonymous_35728
  */

  // singleton object in scala
  object MySingleton { // the only instance of the MySingleton type, the type you can add values and methods
    val mySpecialValue = 53467
    def mySpecialMethod(): Int = 2352
    // special method is scala apply
    // methods on objects are similar to java static methods
    def apply(x: Int): Int = x + 1  // can take any arguments, this can be present in any class, in any object, wherever you like
    // presence of an apply method in a class allows instances of that class to be invoked like functions -> MySingleton(56)
  }

  MySingleton.mySpecialMethod()
  MySingleton.apply(65)
  MySingleton(65) // equivalent  MySingleton.apply(65)


  // there is already class Animal and we are declaring object Animal
  object Animal { // companions - companion object - same name of class or trait
    // companions can access each other's private fields or methods
    // Animal singleton object and instances of the animal type(class) are different things
    // use the animal companion object to access things that do not depend on instances of the animal class
    val canLiveIndefinitely = false
  }

  val animalsCanLiveForever = Animal.canLiveIndefinitely // same way in java access static fields or methods
  /*
  case classes - lightweight data structures with some boilerplate
  compiler automatically generate
    - sensible equals and hash code
    - serialization - send instances over distributed application like akka
    - companion with apply
    - pattern matching -> functional programming in scala
   */

  case class Person(name: String, age: Int)

  val bob = new Person("Bob", 54)
  // may be constructed without new
  val sam = Person("Sam", 23) // can ommit new because of companion with apply, equivalent Person.apply("Sam", 23)

  // exceptions

  try {
    // code that can throw
    val x: String = null
    x.length
  } catch {
    case e: Exception => "some faulty error message"
  } finally {
    // execute some code no matter what
    // closing files, connections or releasing some resources
  }

  // generics -> java, c is templating
  // generics is to reuse the same functionality and to apply it to multiple types
  // any type is T -> generics
  // use T as method definitions or value definitions inside this class
  abstract  class  MyList[T] {
    def head: T
    def tail: MyList[T]
  }

  // using a generic with a concrete type
  val aList: List[Int] = List(1,2,3) // List.apply(1,2,3), List is companion Object
  val first = aList.head
  val rest = aList.tail

  val aStringList = List("hello", "scala")

  // 1 - in scala we usually operate with IMMUTABLE values/objects
  // modification to an instance of a class will normally result or
  // should result for best practice should result in another instance
  // any modification to an object must return another object
  /*
  Benefits
      1 - works miracles in multithreaded/distributed env (speed up development tremendously)
      2) helps making sense of the code("resoning about")
  */

  val reversedList = aList.reverse // returns a new list

  // 2 - Scala is closest to the Object oriented ideal -> truly OOP
  // all code and all the values that we operate with are inside an instance of some type
  // part of object or class




}
