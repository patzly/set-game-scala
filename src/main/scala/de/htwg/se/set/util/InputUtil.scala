package de.htwg.se.set.util

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Success, Try}

object InputUtil:

  sealed trait UserInput

  private case class TextInput(text: String) extends UserInput

  case class NumberInput(number: Int) extends UserInput

  case class CoordinatesInput(coordinates: List[String]) extends UserInput

  case object UndoInput extends UserInput

  case object RedoInput extends UserInput

  case object FinishInput extends UserInput

  @tailrec
  final def stringInput(input: String, undo: Boolean, redo: Boolean, finish: Boolean): UserInput =
    input.trim.toLowerCase match
      case "u" if undo => UndoInput
      case "u" =>
        println(PrintUtil.red("Undo not allowed. Try again:"))
        stringInput(input, undo, redo, finish)
      case "r" if redo => RedoInput
      case "r" =>
        println(PrintUtil.red("Redo not allowed. Try again:"))
        stringInput(input, undo, redo, finish)
      case "f" if finish => FinishInput
      case _ => TextInput(input.trim)

  @tailrec
  final def intInput(input: String, undo: Boolean, redo: Boolean): UserInput =
    stringInput(input, undo, redo, false) match
      case TextInput(text) => Try(text.toInt) match
        case Success(value) => NumberInput(value)
        case _ =>
          println(PrintUtil.red("Invalid input. Try again:"))
          intInput(input, undo, redo)
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ => throw IllegalStateException("Unexpected UserInput type")

  @tailrec
  final def intInput(input: String, min: Int, max: Int, undo: Boolean, redo: Boolean): UserInput =
    intInput(input, undo, redo) match
      case NumberInput(number) if min <= number && number <= max => NumberInput(number)
      case NumberInput(_) =>
        println(PrintUtil.red(s"Only whole numbers from $min to $max allowed. Try again:"))
        intInput(input, min, max, undo, redo)
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ => throw IllegalStateException("Unexpected UserInput type")

  @tailrec
  final def coordinatesInput(input: String, undo: Boolean, redo: Boolean): UserInput =
    stringInput(input, undo, redo, false) match
      case TextInput(text) =>
        val coordinatesPattern = "^([A-Za-z][1-3] +){2}[A-Za-z][1-3]$".r
        text match
          case coordinatesPattern(_*) =>
            val coordinates = text.split(" +").toSet
            if coordinates.size == 3 then
              CoordinatesInput(coordinates.toList)
            else
              println(PrintUtil.red("Only different coordinates possible. Try again:"))
              coordinatesInput(input, undo, redo)
          case _ =>
            println(PrintUtil.red("Invalid input. Try again:"))
            coordinatesInput(input, undo, redo)
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ => throw IllegalStateException("Unexpected UserInput type")

  @tailrec
  final def finishInput(input: String, undo: Boolean, redo: Boolean): UserInput =
    stringInput(input, undo, redo, true) match
      case FinishInput => FinishInput
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ =>
        println(PrintUtil.red("Invalid input. Try again:"))
        finishInput(input, undo, redo)