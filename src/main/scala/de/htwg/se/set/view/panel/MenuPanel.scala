package de.htwg.se.set.view.panel

import de.htwg.se.set.controller.IController
import de.htwg.se.set.controller.controller.{AddColumnAction, ExitAction, RedoAction, UndoAction}
import de.htwg.se.set.model.GameMode.IN_GAME
import de.htwg.se.set.util.PanelUtil.CompatButton
import de.htwg.se.set.util.{PanelUtil, ResUtil}

import java.awt.Color
import javax.swing.border.MatteBorder
import scala.swing.event.ButtonClicked
import scala.swing.{BoxPanel, Orientation}

case class MenuPanel(controller: IController) extends BoxPanel(Orientation.Horizontal):

  private val menuFont = ResUtil.customFont("jost_medium", 16)
  
  private val undoButton = new CompatButton("UNDO"):
    font = menuFont
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(UndoAction)
        update()
    }
    
  private val redoButton = new CompatButton("REDO"):
    font = menuFont
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(RedoAction)
        update()
    }
    
  private val addButton = new CompatButton("ADD CARDS"):
    font = menuFont
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(AddColumnAction)
        update()
    }

  private val exitButton = new CompatButton("EXIT"):
    font = menuFont
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(ExitAction)
        update()
    }

  background = ResUtil.COLOR_BG
  border = MatteBorder(0, 0, 2, 0, Color.BLACK)
  contents += undoButton
  contents += redoButton
  contents += addButton
  contents += exitButton
  update()
  
  def update(): Unit =
    undoButton.enabled = controller.canUndo
    redoButton.enabled = controller.canRedo
    addButton.enabled = controller.game.selectedPlayer.isEmpty
    addButton.visible = controller.settings.mode == IN_GAME && !controller.settings.singlePlayer
    exitButton.visible = controller.settings.mode == IN_GAME