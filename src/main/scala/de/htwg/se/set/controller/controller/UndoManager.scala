package de.htwg.se.set.controller.controller

class UndoManager:

  private var undoStack: List[Command] = List()
  private var redoStack: List[Command] = List()

  def executeCommand(command: Command): Unit =
    undoStack = command :: undoStack
    redoStack = List()
    command.saveSnapshot()
    command.execute()

  def canUndo: Boolean = undoStack.nonEmpty

  def undoCommand(): Unit =
    undoStack match
      case head :: stack =>
        head.undo()
        undoStack = stack
        redoStack = head :: redoStack
      case _ => throw IllegalStateException("Undo stack is empty")

  def canRedo: Boolean = redoStack.nonEmpty

  def redoCommand(): Unit =
    redoStack match
      case head :: stack =>
        head.execute()
        redoStack = stack
        undoStack = head :: undoStack
      case _ => throw IllegalStateException("Redo stack is empty")