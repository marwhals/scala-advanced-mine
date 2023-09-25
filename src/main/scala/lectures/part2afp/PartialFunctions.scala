package lectures.part2afp

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction: Function1[Int, Int] = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 3 => 999
  }
  // {1,2,5} => Int //'partial' function above based on domain and target

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } //This is a partial function value.

  // PF Utilities
  println(aPartialFunction.isDefinedAt(67))

  // lift - partial functions can be lifted to total functions returning options
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))

  val pfChain = aPartialFunction.orElse[Int, Int] { //orElse for chaining multiple partial functions
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal functions
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  /*
  Note - PF can only have one parameter type
   */

  //ex1 constrcut  a PF
  //ex2 implement a chatbot

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(x: Int): Int = x match {
      case 1 => 42
      case 2 => 65
      case 5 => 999
    }
    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 5
  }

  val chatbot: PartialFunction[String, String] = {
    case "hello" => "Hi, my name is HAL9000"
    case "goodbye" => "Once you start talking to me, there is no return, human"
    case "call mom" => "Unable to find your phone without your credit card"
  }

  scala.io.Source.stdin.getLines().map(chatbot).foreach(println) //Lol don't include braces


}
