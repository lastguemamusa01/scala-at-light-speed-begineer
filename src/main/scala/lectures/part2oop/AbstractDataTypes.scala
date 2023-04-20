package lectures.part2oop

object AbstractDataTypes extends App {

  // there are situations where you need to leave some fields or methods blank or unimplemented -> abstract members

  // absctract class -> cannot be instanciated
  abstract class Animal {
    // can have abstract member and non abstract member
    val creatureType: String = "wild"  // abstract field
    def eat: Unit   // Abstract method
  }

  class Dog extends Animal {
    // need to mandatory implement abstract field and method of parent class
    override val creatureType: String = "Canine"
    def eat: Unit = println("crunch crunch") // override is not mandatory for abstract members
  }

  // traits
  trait Carnivore {
    // traits like abstract classes, also have abstract fields and methods
    // they can be inherited along classes
    def eat(animal: Animal) : Unit // abstract member
    val preferredMeal: String = "fresh meat" // non abstract member
  }

  class Crocodile extends Animal with Carnivore {
    override val creatureType: String = "Croc"  // you need to use override for non abstract member
    def eat: Unit = println("nomnomnom")
    def eat(animal: Animal): Unit = println(s"I'm a croc and I'm eating ${animal.creatureType}")
  }

  val dog = new Dog
  val croc = new Crocodile

  croc.eat(dog)

  // traits vs abstract classes
  // traits and abstract classes both could have abstract member and non abstract member
  // 1 - traits do not have contructor parameters   -> X  -> trait Carnivore(name: String) {
  // 2 - only extend one abstract class, but you can mix in multiple traits
  // 3 - traits = hehavior, abstract class = "thing"


  //Scala types
  // scala.Any -> Any is the mohter of all types
  // Scala.AnyRef(java.lang.Ojbect) extended from scala.Any
  // scala.AnyRef -> String, List, Set...
  // scala.Null is extended from scala.AnyRef -> inly instance is the null reference , wich means no reference
  // you can replace anything like person with no reference
  // scala.AnyVal extended from scala.Any -> contain all the primitive values -> Int, Unit, Boolean, Float
  // and classes that extend of AnyVal -> rarely used
  // Scala.Nothing extended scala.AnyVal and scala.Null  -> nothing can replace everything
  // nothing means no instance of anything at all

}
