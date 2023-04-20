package lectures.part2oop

object Inheritannce extends App {

  // scala is single class inheritance
  // inheriting all the non private fileds and methods
  // Animal Super Class and Cat is Subclass

  class Animal {
    val creatureType = "wild"
    val creatureTypeSignature = "animal"
    // access modifiers -> public, private and protected
    // final prevent overriding from subclass
    final def eat = println("nomnom")  // by default is public
    // overloading example
    def eat(foodType: String) = println("eating " + foodType) // this eat method have a 1 argument
    private def eatPrivate = println("namnam") // no accessible from subclass
    protected def eatProtected = println("yamyyamy") // protected is only accessible in the subclass but not outside
    def eatPolymorphism = println("nom nom")
  }

  class Cat extends Animal {
    def crunch = {
      eatProtected
      println("crunch crunch")
    }
  }

  val cat = new Cat
  cat.eat
  cat.eat("fish")
  cat.crunch

  // constructors
  class Person(name:String, age: Int) // defining class like this with a class signature also defines its contructor
  // unspecified value parameters: name and age
  // class Adult(name:String, age: Int, idCard: String) extends Person   -> error
  class Adult(name:String, age: Int, idCard: String) extends Person(name,age) // correct way of extending constructors with parameters

  // auxiliary contructors
  class PersonAuxiliary(name:String, age: Int) {
    def this(name: String) = this(name, 0)
  }

  class AdultAuxiliary(name:String, age: Int, idCard: String) extends PersonAuxiliary(name) // this is valid

  // overriding
  class Dog(override val creatureTypeSignature:String) extends Animal {
    // override work for methods, as val. var is not applicable
    // also you can override passing in the parameter
    override val creatureType: String = "domestic"
    override def eatProtected = {
      // super
      // use to referencea method or a filed from parent class
      // super example
      super.eatProtected // call super class eatProtected
      println("war war")
    }

    override def eatPolymorphism = println("kui kui")

  }


  val dog = new Dog("pomeranian")
  dog.eatProtected
  println(dog.creatureType)
  println(dog.creatureTypeSignature)

  // type substitution -> broad : Polymorphism
  val unknownAnimal: Animal = new Dog("k9")
  // a method call will always go to the most overriden version whenever possible
  unknownAnimal.eatPolymorphism  // use dogs method

  // overriding(supplying a different implementation in derived casses) - vs overloading(supplying multiple methods
  // with different signatures, but with the same name and same class)

  // preventing overrrides
  // 1 - use keyword final
  // 2 - use final class -> entire class cannot be extended
  // 3 - seal the class -> softer restriction that you can extend in this file, prevent extension in other files.

  // use final class
  final class AnimalCannotExtended

  // class crocodile extends AnimalCannotExtended  // this will throw error, becuase final class cannot extended
  // numerical class and string type in scala ara final

  // seal the class
  sealed class AnimalOnlyExtendedInThisClass

  class crocodile extends AnimalOnlyExtendedInThisClass



}
