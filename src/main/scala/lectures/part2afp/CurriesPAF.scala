package lectures.part2afp

object CurriesPAF extends App {

  // curried functions - functions returning other functions as results
  // functons that recieved multiple parameter lists
  // example
  val superAdder: Int => Int => Int = x => y => x + y
  val add3:       Int => Int        = superAdder(3) // Int => Int = y => 3 + y
  println(add3(5))
  println(superAdder(12)(3))

  // def i.e method - all parameter lists will need to be passed in. Need function values instead of methods to use higher order functionos
  // methods are a part of instances of classes on the JVM
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method
  val add4: Int => Int = curriedAdder(4) //method being turned into a function value
  //Behinds the scenes the compile does lifting - transformming a method into a function is called lifting AKA 'eta expansion'
  // functionos != methods JVM limiitatino
  def inc(x : Int) = x + 1
  List(1,2,3).map(x => inc(x)) //ETA -expansions

  // Partial functinos applications and manual ETA-expansioon
  val add5 = curriedAdder(5) _ // Int => Int

  //Ex
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  //implement add7: Int => Int = y => 7 + y in as many different ways as possible
  val add7 = (x: Int) => simpleAddFunction(7, x)
  val add7_2 = simpleAddFunction.curried(7)
  val add7_6 = simpleAddFunction(7, _: Int)
  val add7_3 = curriedAddMethod(7) _ //PAF - compiler forced ETA expansion
  val add7_4 = curriedAddMethod(7)(_) //PAF - compiler forced ETA expansion - alt syntax - '_' forces the compiler to do eta expansion
  val add7_5 = simpleAddMethod(7, _: Int) //Alternative syntax for turning methods into function values
  // y => simpleAddMethod(7,y)

  // ----- underscores are quite powerful
  def concatenator(a: String, b: String, c: String) = a + b + c

  val insertName = concatenator("Hello, I'm ", _: String, ", how are you?") // x: String => concatenator(hello, x, howarewyou)
  println(insertName("Daniel"))

  val fillInTheBlanks = concatenator("Hello, ", _: String, _: String) // (x, y) => concatenator("Hello, ", x, y)
  println(fillInTheBlanks("Daniel", " Scala is awesome!"))

  // EXERCISES
  /*
    1.  Process a list of numbers and return their string representations with different formats
        Use the %4.2f, %8.6f and %14.12f with a curried formatter function.
   */

  def curriedFormatter(formatter: String)(number: Double): String = formatter.format(number)

  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(curriedFormatter("%14.12f") _)) // compiler does eta-expansion for us see compiler warning
  println(numbers.map(simpleFormat))
  println(numbers.map(seriousFormat))
  println(numbers.map(preciseFormat))

  /*
    2.  difference between
        - functions vs methods
        - parameters: by-name vs 0-lambda
   */
  def byName(n: => Int) = n + 1

  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42

  def parenMethod(): Int = 42

  /*
    calling byName and byFunction - Note they are completely different
    - int
    - method
    - parenMethod
    - lambda
    - PAF
   */
  byName(23) // ok
  byName(method) // ok - method will be evaluated by its call
  byName(parenMethod())
  byName(parenMethod) // ok but beware ==> byName(parenMethod())
  //  byName(() => 42) // not ok because by name of value type is not the same as a function parameter
  byName((() => 42) ()) // ok -- You supply  a function and then you are calling it
  //  byName(parenMethod _) // not ok - a functioni value is noa substitute for a by name value

  //  byFunction(45) // not ok - lambda expected but an Int is provided
  //  byFunction(method) // not ok!!!!!! does not do ETA-expansion!
  byFunction(parenMethod) // compiler does ETA-expansion
  byFunction(() => 46) // works
  byFunction(parenMethod _) // also works, but warning- unnecessary



}
