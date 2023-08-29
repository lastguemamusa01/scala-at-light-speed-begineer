package advacedlectures.part2afp

object Monads extends App {

  /*
   monads are a kind of types which have some fundamental operations -> abstract type
   monad trait have def unit -> also called pure(other languages) or apply(scala)
   monad trait have flatMap def -> also called Bind(other languages)

   trait MonadTemplate[A] {
    def unit(value: A) : MonadTemplate[A]
    def flatMap[B](f: A => MonadTemplate[B]): MonadTemplate[B]
   }

   List, Option, Try, Future, Stream, Set are all monads

   3 monads laws (operations must satisfy the monad laws) :
   left-identity -> unit(x).flatMap(f) == f(x) -> if you build a basic monad out of an element and you flatmap it with a function
   it should give you the function applied to that element

   List(x).flatMap(f) = f(x) ++ Nil.flatMap(f) = f(x)

   Option(x).flatMap(f) = f(x)
   Option of not null build - > Some(x).flatMap(f) = f(x)

   right-identity -> aMonadInstance.flatMap(unit) == aMonadInstance -> if you have a monad instance and you flatmap it with the unit
   function, then it should give you the same monad instance

   list.flatMap(x => List(x)) = list

   opt.flatMap(x => Option(x)) = opt
   Some(v).flatMap(x => Option(x)) =
   Option(v) =
   Some(v)

   associativity -> m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g)) -> if you have a monad instance and you flatmap it
   with 2 functions in sequence, then that should give you the same thing as flat ampping the monad instance with a composite function
   that does f flatMap g for every element

   [a b c].flatMap(f).flatMap(g) =
   (f(a) ++ f(b) ++ f(c)).flatMap(g) =
   f(a).flatMap(g) ++ f(b).flatMap(g) ++ f(c).flatMap(g) =
   [a b c].flatMap(f(_).flatMap(g)) =
   [a b c].flatMap(x => f(x).flatMap(g))

   o.flatMap(f).flatMap(g) = o.flatMap(x => f(x).flatMap(g)) -> composite function
   Some(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
   Some(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
   */

  // our own Try monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
  left-identity

  unit.flatMap(f) = f(x)
  Attempt(x).flatMap(x) = f(x) // success case!
  Success(x).flatMap(f) = f(x) // proved

  right-identity
  attempt.flatmap(unit) = attempt
  Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
  Fail(_).flatMap(...) = Fail(e)

  Associativity

  attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
  Fail(e).flatMap(f).flatMap(g) == Fail(e)
  Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)

  Success(v).flatMap(f).flatMap(g) =
  f(v).flatMap(g) or Fail(e)

  Sucess(v).flatMap(x => f(x).flatMap(g)) =
  f(v).flatMap(g) Or Fail(e)

   */

  val attempt = Attempt {
    throw new RuntimeException("My own monad, yes !")
  }

  println(attempt)

  /*
  Exercise :

  1) implement a Lazy[T] monad = computation which will only be executed when it's needed.

  unit/apply
  flatMap

  2) Monads = unit + flatMap
  Monads = unit + map + flatten

  Monad[T] { // think like List
    def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)
    def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
    def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) => x)
  }

  List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
  List(List(1,2), List(3,4)).flatten = List(List(1,2), List(3,4)).flatMap(x => x) =  List(1,2,3,4)
    (have List in mind)
   */

  // 1 - lazy monad

  class Lazy[+A](value: => A) { // call by name, prevents the value from being evaluated when the lazy object is being contructed
    // call by need
    private lazy val internalValue = value
    def use: A = internalValue
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)  // (=> A) receiver parameter by name as well
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value) // unit
  }

  val lazyInstance = Lazy {
    println("Today i don't feel like doing anythihng") // i dont see the println
    42
  }

  // println(lazyInstance.use) // this is printed only when i used
  /*
  Today i don't feel like doing anythihng
  42
   */

  val flatMapppedInstance = lazyInstance.flatMap(x => Lazy {
    10 * x // i dont see the println
  })

  val flatMapppedInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x // i dont see the println
  })

  // we just evaluate them once at the first use and then we use the already evaluated value

  flatMapppedInstance.use // this force to use lazy monads
  flatMapppedInstance2.use // this force to use lazy monads
  // only printed once -> Today i don't feel like doing anythihng
  // after lazy instance is evaluated at the first use of flatMap instance, then the same
  // value is reused in the later call to use for flatMap instance two

  /*
  left-identity
  unit.flatMap(f) = f(v)
  Lazy(v).flatMap(f) = f(v)

  right-identity
  l.flatMap(unit) = l
  Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

  associativity: l.flatMap(f).flatMap(g) = l.flatMap(x => f(x).flatMap(g))

  Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
  Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)

   */


}
