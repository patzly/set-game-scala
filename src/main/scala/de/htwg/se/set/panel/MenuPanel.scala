package de.htwg.se.set.panel

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{AddColumnAction, GameMode, RedoAction, UndoAction}
import de.htwg.se.set.util.ResUtil

import java.awt.Color
import javax.swing.border.MatteBorder
import scala.swing.event.ButtonClicked
import scala.swing.{Button, FlowPanel}

case class MenuPanel(controller: Controller) extends FlowPanel(FlowPanel.Alignment.Center)():

  private val menuFont = ResUtil.customFont("jost_medium", 16)
  
  private val undoButton = new Button("UNDO"):
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(UndoAction)
        update()
    }
    font = menuFont
    foreground = Color.BLACK
    borderPainted = false
    focusPainted = false
    
  private val redoButton = new Button("REDO"):
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(RedoAction)
        update()
    }
    font = menuFont
    foreground = Color.BLACK
    borderPainted = false
    focusPainted = false

  private val addButton = new Button("ADD CARDS"):
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(AddColumnAction)
        update()
    }
    font = menuFont
    foreground = Color.BLACK
    borderPainted = false
    focusPainted = false

  background = ResUtil.COLOR_BG
  border = new MatteBorder(0, 0, 2, 0, Color.BLACK)
  contents += undoButton
  contents += redoButton
  contents += addButton
  
  def update(): Unit =
    undoButton.enabled = controller.canUndo
    redoButton.enabled = controller.canRedo
    addButton.visible = controller.settings.mode == GameMode.IN_GAME