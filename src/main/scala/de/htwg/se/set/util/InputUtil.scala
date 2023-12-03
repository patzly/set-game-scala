package de.htwg.se.set.util

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Success, Try}

object InputUtil:

  sealed trait UserInput

  private case class TextInput(input: String) extends UserInput

  case class NumberInput(input: Int) extends UserInput

  case class CoordinatesInput(coordinates: List[String]) extends UserInput

  case object UndoInput extends UserInput

  case object RedoInput extends UserInput

  case object FinishInput extends UserInput

  @tailrec
  final def stringInput(undo: Boolean, redo: Boolean, finish: Boolean): UserInput =
    val input = StdIn.readLine().trim
    input.toLowerCase match
      case "u" if undo => UndoInput
      case "u" =>
        println(PrintUtil.red("Undo not allowed. Try again:"))
        stringInput(undo, redo, finish)
      case "r" if redo => RedoInput
      case "r" =>
        println(PrintUtil.red("Redo not allowed. Try again:"))
        stringInput(undo, redo, finish)
      case "f" if finish => FinishInput
      case _ => TextInput(input)

  @tailrec
  final def intInput(undo: Boolean, redo: Boolean): UserInput =
    stringInput(undo, redo, false) match
      case TextInput(input) => Try(input.toInt) match
        case Success(value) => NumberInput(value)
        case _ =>
          println(PrintUtil.red("Invalid input. Try again:"))
          intInput(undo, redo)
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ => throw IllegalStateException("Unexpected UserInput type")

  @tailrec
  final def intInput(min: Int, max: Int, undo: Boolean, redo: Boolean): UserInput =
    intInput(undo, redo) match
      case NumberInput(value) if min <= value && value <= max => NumberInput(value)
      case NumberInput(_) =>
        println(PrintUtil.red(s"Only whole numbers from $min to $max allowed. Try again:"))
        intInput(min, max, undo, redo)
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ => throw IllegalStateException("Unexpected UserInput type")

  @tailrec
  final def coordinatesInput(undo: Boolean, redo: Boolean): UserInput =
    stringInput(undo, redo, false) match
      case TextInput(input) =>
        val coordinatesPattern = "^([A-Za-z][1-3] +){2}[A-Za-z][1-3]$".r
        input match
          case coordinatesPattern(_*) =>
            val coordinates = input.split(" +").toSet
            if coordinates.size == 3 then
              CoordinatesInput(coordinates.toList)
            else
              println(PrintUtil.red("Only different coordinates possible. Try again:"))
              coordinatesInput(undo, redo)
          case _ =>
            println(PrintUtil.red("Invalid input. Try again:"))
            coordinatesInput(undo, redo)
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ => throw IllegalStateException("Unexpected UserInput type")

  @tailrec
  final def finishInput(undo: Boolean, redo: Boolean): UserInput =
    stringInput(undo, redo, true) match
      case FinishInput => FinishInput
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ =>
        println(PrintUtil.red("Invalid input. Try again:"))
        finishInput(undo, redo)