package advacedlectures.part5ts

object FBoundedPolymorphism extends App {

  /*
  i have hierarchy
  how do i force a method in the super type to accept a current type, quote unquote current type ?
   */

  // how make the compiler force this

//  trait Animal {
//    def breed: List[Animal]
//  }
//  class Cat extends Animal {
//    override def breed: List[Animal] = ??? // List[Cat] !!!
//  }
//  class Dog extends Animal {
//    override def breed: List[Animal] = ??? // List[Dog] !!!
//  }


  // solution 1 - naive
//    trait Animal {
//      def breed: List[Animal]
//    }
//    class Cat extends Animal {
//      override def breed: List[Cat] = ??? // List[Cat] !!! this is valid overwrite
//    }
//    class Dog extends Animal {
//      override def breed: List[Cat] = ??? // List[Dog] !!! // but human error, this is also valid List[Cat] ??? -> wrong
//    }

  // Solution 2 - F-Bounded polymorphism
  // this is used in ORM frameworks or APIs

//  trait Animal[A <: Animal[A]] { // Animal[A <: Animal[A]] -> recursive type: F-Bounded polymorphism
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Cat] = ??? // List[Dog] -> not compiler , force to be specific type
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Dog] = ??? // List[Dog]
//  }
//
//  trait Entity[E <: Entity[E]] // ORM
//  class Person extends Comparable[Person] {
//    override def compareTo(o: Person): Int = ???
//  } // java FBP
//
//  class Crocodile extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ??? // List[Dog] !! -> wrong, human error , this is Crocodile class
//  }

  // Solution 3 -  FBP + self type
  // how to force compiiler check the Animal[Dog] is the same of class that i declaring is Dog

//  trait Animal[A <: Animal[A]] { self: A =>  // Dog also need to be Animal[Dog]
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Cat] = ???
//  }
//
//  class Dog extends Animal[Dog] {  // class Crocodile error
//      override def breed: List[Animal[Dog]] = ??? // List[Dog] !! -> wrong, human error , this is Crocodile class
//  }

  //  class Crocodile extends Animal[Dog] {
  //    override def breed: List[Animal[Dog]] = ??? // List[Dog] !! -> wrong, human error , this is Crocodile class
  //  }


  // the problem - when you bring this to class hierarchy down one level
  // the f bounded polymorphism stops work when our class hierarchy down one level

//  trait Fish extends Animal[Fish]
//  class Shark extends Fish {
//    override def breed: List[Animal[Fish]] = List(new Cod) // wrong, shark is not cod
//  }
//
//  class Cod extends Fish {
//    override def breed: List[Animal[Fish]] = ???
//  }

  // Exercise
  // 4 - type classes

//  trait Animal
//  // type class description
//  trait CanBreed[A] {
//    def breed(a: A): List[A] // a will be cat or dog
//  }
//
//  class Dog extends Animal
//  object Dog {  // companion object
//    // create some implicit conversion
//    // type class instance
//    implicit object DogsCanBreed extends CanBreed[Dog] {
//      def breed(a: Dog): List[Dog] = List()
//    }
//  }
//
//  // converting dog to some implicit class which has a method which can receive
//  // argument of CanBreed[Dog] as implicit parameter
//
//  implicit class CanBreedOps[A](animal: A) {
//    def breed(implicit canBreed: CanBreed[A]): List[A] =
//      canBreed.breed(animal)
//  }
//
//  val dog = new Dog
//  dog.breed // breed is impliicit method, dog autoconverted to CanBreedOps -> this return List[Dog] !!
  /*
  compiler ->
  new CanBreedOps[Dog](dog).breed(Dog.DogsCanBreed)
  impilicit value to pass to breed: Dog.DogsCanBreed
   */

//  class Cat extends Animal
//  object Cat {
//    implicit object CatsCanBreed extends CanBreed[Dog] {
//      def breed(a: Dog): List[Dog] = List()
//    }
//  }
//
//  val cat = new Cat
//  cat.breed  // cat companion object does not have the right type class instance type

  // solution 5
  // trait of animal being the type class itself
  // much cleaner code

  trait Animal[A] { // pure type classes
    def breed(a: A): List[A]
  }
  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  class Cat
  object Cat {
    implicit object CatAnimal extends Animal[Dog] { // this is wrong, compile will catch it
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  implicit class AnimalOps[A](animal : A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed

  val cat = new Cat
  // cat.breed





}
