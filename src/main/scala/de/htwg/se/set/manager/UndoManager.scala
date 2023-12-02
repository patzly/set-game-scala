package de.htwg.se.set.manager

import de.htwg.se.set.model.{Command, Game, Settings, State}

case class Snapshot(settings: Settings, game: Game, state: State)

case class UndoManager():

  private var undoStack: List[Command] = List()
  private var redoStack: List[Command] = List()
  private var stateStack: List[State] = List()

  def doStep(command: Command): Command =
    undoStack = command :: undoStack
    redoStack = List()
    command.saveSnapshot()
    command.execute

  def canUndo: Boolean = undoStack.nonEmpty

  def undoStep(state: State): State =
    stateStack = state :: stateStack
    undoStack match
      case head :: stack =>
        val state = head.undo()
        undoStack = stack
        redoStack = head :: redoStack
        state
      case _ => throw IllegalStateException("Undo stack is empty")

  def canRedo: Boolean = redoStack.nonEmpty

  def redoStep(): State =
    redoStack match
      case head :: stack =>
        head.execute
        redoStack = stack
        undoStack = head :: undoStack
        stateStack match
          case state :: rest =>
            stateStack = rest
            state
          case _ => throw IllegalStateException("State stack is empty")
      case _ => throw IllegalStateException("Redo stack is empty")