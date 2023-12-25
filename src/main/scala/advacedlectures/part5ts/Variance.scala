package advacedlectures.part5ts

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance ?
  // inheritance - type substitution of generics


  class Cage[T]
  // shoud a cage of cat inherit from a cage of animals ?

  // yes = covariance

  class CCage[+T]  // +T - covariance
  val ccage: CCage[Animal] = new CCage[Cat]  // replacing more general to specific thing

  // no  = invariance
  class ICage[T]
  // val icage: ICage[Animal] = new ICage[Cat]// wrong this is like val x: Int = "hello"

  // hell no - opposite = Contravariance
  class XCage[-T] // type substitution works in the opposite sense
  val xcage: XCage[Cat] = new XCage[Animal] // valid substitution

  class InvariantCage[T](val animal: T) // invariant

  class CovariantCage[+T](val animal: T) // covariant position

  // class ContravariantCage[-T](val animal: T)
  /*
  val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  // class CovariantVariableCage[+T](var animal: T) // types of vars are in contravariant position
  /*
  val ccage: CCage[Animal] = new CCage[Cat](new Cat)
  ccage.animal = new Crocodile
   */

  // class ContravariantVariableCage[-T](var animal: T) // also in COVARIANT POSITION
  /*
  val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  class InvariantVariableCage[T](var animal: T) // ok
  // covariant and contravariant positions are basically some compiler restrictions


}
