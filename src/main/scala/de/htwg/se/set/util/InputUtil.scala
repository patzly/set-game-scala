package de.htwg.se.set.util

import de.htwg.se.set.controller.IUserInput

import scala.util.{Success, Try}

object InputUtil:

  case class TextInput(text: String) extends IUserInput

  case class NumberInput(number: Int) extends IUserInput

  case class CoordinatesInput(coordinates: List[String]) extends IUserInput

  case object UndoInput extends IUserInput

  case object RedoInput extends IUserInput

  case object FinishInput extends IUserInput

  case object ExitInput extends IUserInput

  case class InvalidInput(msg: String = "Invalid input.") extends IUserInput

  final def stringInput(input: String, undo: Boolean, redo: Boolean, finish: Boolean, exit: Boolean): IUserInput =
    input.trim.toLowerCase match
      case "u" => if undo then UndoInput else InvalidInput("Undo not allowed.")
      case "r" => if redo then RedoInput else InvalidInput("Redo not allowed.")
      case "f" if finish => FinishInput
      case "e" if exit => ExitInput
      case _ => TextInput(input.trim)

  final def intInput(input: String, undo: Boolean, redo: Boolean, exit: Boolean): IUserInput =
    stringInput(input, undo, redo, false, exit) match
      case TextInput(text) => Try(text.toInt) match
        case Success(value) => NumberInput(value)
        case _ => InvalidInput()
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case ExitInput => ExitInput
      case InvalidInput(msg) => InvalidInput(msg)
      case _ => throw IllegalStateException("Unexpected UserInput type")

  final def intInput(input: String, min: Int, max: Int, undo: Boolean, redo: Boolean, exit: Boolean): IUserInput =
    intInput(input, undo, redo, exit) match
      case NumberInput(number) if min <= number && number <= max => NumberInput(number)
      case NumberInput(_) => InvalidInput(s"Only numbers from $min to $max allowed.")
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case ExitInput => ExitInput
      case InvalidInput(msg) => InvalidInput(msg)
      case _ => throw IllegalStateException("Unexpected UserInput type")

  final def coordinatesInput(input: String, undo: Boolean, redo: Boolean): IUserInput =
    stringInput(input, undo, redo, false, true) match
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
      case ExitInput => ExitInput
      case InvalidInput(msg) => InvalidInput(msg)
      case _ => throw IllegalStateException("Unexpected UserInput type")

  final def finishInput(input: String, undo: Boolean, redo: Boolean): IUserInput =
    stringInput(input, undo, redo, true, false) match
      case FinishInput => FinishInput
      case UndoInput => UndoInput
      case RedoInput => RedoInput
      case _ => InvalidInput()