package lectures.part2afp

object Monads extends App {
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
    Attempt(x).flatMap(f) = f(x) // Success case!
    Success(x).flatMap(f) = f(x) // proved.
    right-identity
    attempt.flatMap(unit) = attempt
    Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
    Fail(e).flatMap(...) = Fail(e)
    associativity
    attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
    Fail(e).flatMap(f).flatMap(g) = Fail(e)
    Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)
    Success(v).flatMap(f).flatMap(g) =
      f(v).flatMap(g) OR Fail(e)
    Success(v).flatMap(x =>  f(x).flatMap(g)) =
      f(v).flatMap(g) OR Fail(e)
   */

  val attempt = Attempt {
    throw new RuntimeException("My own monad, yes!")
  }

  println(attempt)

  /*
    EXERCISE:
    1) implement a Lazy[T] monad = computation which will only be executed when it's needed.
      unit/apply
      flatMap
    2) Monads = unit + flatMap
       Monads = unit + map + flatten
       Monad[T] {
        def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)
        def map[B](f: T => B): Monad[B] = ???
        def flatten(m: Monad[Monad[T]]): Monad[T] = ???
        (have List in mind)
   */
  //1- Lazy Monad - by name so it is only evaluated when it is needed
  class Lazy[+A](value: => A) {
    // avoid re evauating the value by using call by need
    private lazy val internalValue = value
    def use: A = value

    def flatMapLazee[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)

    def flatMapEager[B](f: A => Lazy[B]): Lazy[B] = f(internalValue)
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value) // Unit
  }

  //------------------ Lazy monad done
  val lazyInstance = Lazy {
    println("Maybe I will be printed when this is run\n")
    2
  }

  println(lazyInstance.use) // Line will be printed

  val flatMappedInstanceEager = lazyInstance.flatMapEager(x => Lazy {
    10 * x
  })

  val flatMappedInstanceLazy = lazyInstance.flatMapLazee(x => Lazy {
    10 * x
  })

  flatMappedInstanceEager.use
  flatMappedInstanceLazy.use

  //2 - Map and flatten in terms of flatten
  /*
    Monad[T] { // List
      def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)
      def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
      def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) => x)
      List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
      List(List(1, 2), List(3, 4)).flatten = List(List(1, 2), List(3, 4)).flatMap(x => x) = List(1,2,3,4)
    }
   */
}
