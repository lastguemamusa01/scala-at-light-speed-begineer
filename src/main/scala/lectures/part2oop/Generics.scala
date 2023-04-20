package lectures.part2oop

object Generics extends App {

  // generic - use the same code on many(potentially unrelated) types
  // type parameter [T] between square bracket

  //  A -> generic type
  // generic class and trait

  class MyList[A] { // reusable  -> generic class -> A is type of parameter
    // use the type A
  }

  trait MyListTrait[B] { // this also works for trait -> but cannot instantiated

  }

  class MyMap[Key,Value]  // multiple type of parameters

  val listOfIntegers = new MyList[Int]
  val listOfStrings = new MyList[String]

  // generic methods
  // companion object
  object MyList {  // object cannot be type parameterized
      // define method given a type parameter, construct an empty myList parameterized with that type
      def empty[A] : MyList[A] = ??? //  define a method signature type parameter with a generic type parameter
  }

  val emptyListOfIntegers = MyList.empty[Int]

  // variance problem
  // Variance -> if B extends A, shoud List[B] extend List[A] ?
  // yes -> covariance, no -> invariant(default) and hell no! (contravariant)

  class Animal
  class Cat extends Animal
  class Dog extends Animal

  // 1. - List[Cat] extends List[Animal] = Covariance
  class CovariantList[+A]     // +A means this is covariance list
  val animal: Animal = new Cat // polimorphic type substitution
  val animalList: CovariantList[Animal] = new CovariantList[Cat]


  // animalList.add(new Dog) ???  -> this going to pollute list of animals here. because animalList is Cat List.
  // you are adding Dog -> hard Question -> this covert to List of Animals(animals are dog and cat)
  // ansewer = we return a list of Animals


  // 2.-List[Cat] different List[Animal] = Invariance

  class InvariantList[A]
  val invariantAnimalList: InvariantList[Animal] = new InvariantList[Animal]

  // 3 - hell, no! contravariance
  class Trainer[-A]  // ContravariantList
  val trainer: Trainer[Cat] = new Trainer[Animal]  /// This trainer can train any animal, but particular specialize in cat

  // bounded types - allow you to use your generic classes only for certain types that are either a subclass
  // or a different type or a super class of a different type.

  // upper bounded type in animal
  class Cage[A <: Animal](animal: A) // Class cage only accepts type parameters A which are subtypes of animal

  val cage = new Cage(new Dog) // Dog is subclass of animal , so this is valid - upper bounded type in animal

  // lower bounded type
  class anotherCage[A >: Animal](animal: A) // anotherCage only accepts something which is a super type of animal

  // bounded type solve the variance problem

  class MyCovariantList[+A] {
    def add[B >: A](element: B): MyCovariantList[B] = ???

    /*
    A = Cat
    B = Dog = Animal
     */
  }

  // expand MyList to be generic -> create GenericMyList.scala


}
