package de.htwg.se.set.util

import scala.util.{Success, Try}

object InputUtil:

  sealed trait UserInput

  case class TextInput(text: String) extends UserInput

  case class NumberInput(number: Int) extends UserInput

  case class CoordinatesInput(coordinates: List[String]) extends UserInput

  case object UndoInput extends UserInput

  case object RedoInput extends UserInput

  case object FinishInput extends UserInput

  case class InvalidInput(msg: String = "Invalid input.") extends UserInput

  final def stringInput(input: String, undo: Boolean, redo: Boolean, finish: Boolean): UserInput =
    input.trim.toLowerCase match
      case "u" => if undo then UndoInput else InvalidInput("Undo not allowed.")
      case "r" => if redo then RedoInput else InvalidInput("Redo not allowed.")
      case "f" if finish => FinishInput
      case _ => TextInput(input.trim)

  final def intInput(input: String, undo: Boolean, redo: Boolean): UserInput =
    stringInput(input, undo, redo, false) match
      case TextInput(text) => Try(text.toInt) match
        case Success(value) => NumberInput(value)
        case _ => InvalidInput()
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ => throw IllegalStateException("Unexpected UserInput type")

  final def intInput(input: String, min: Int, max: Int, undo: Boolean, redo: Boolean): UserInput =
    intInput(input, undo, redo) match
      case NumberInput(number) if min <= number && number <= max => NumberInput(number)
      case NumberInput(_) => InvalidInput(s"Only numbers from $min to $max allowed.")
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ => throw IllegalStateException("Unexpected UserInput type")

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
              InvalidInput("Only different coordinates possible.")
          case _ => InvalidInput()
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ => throw IllegalStateException("Unexpected UserInput type")

  final def finishInput(input: String, undo: Boolean, redo: Boolean): UserInput =
    stringInput(input, undo, redo, true) match
      case FinishInput => FinishInput
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ => InvalidInput()