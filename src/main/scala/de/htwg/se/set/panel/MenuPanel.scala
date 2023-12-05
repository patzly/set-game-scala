package de.htwg.se.set.panel

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{RedoAction, UndoAction}
import de.htwg.se.set.util.ResUtil

import java.awt.Color
import scala.swing.{Button, FlowPanel}
import scala.swing.event.ButtonClicked

case class MenuPanel(controller: Controller) extends FlowPanel(FlowPanel.Alignment.Center)():

  private val menuFont = ResUtil.customFont("jost_medium", 16)
  
  private val undoButton = new Button("UNDO"):
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(UndoAction)
        update()
    }
    font = menuFont
    foreground = ResUtil.COLOR_LIGHT
    borderPainted = false
    focusPainted = false
    
  private val redoButton = new Button("REDO"):
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(RedoAction)
        update()
    }
    font = menuFont
    foreground = ResUtil.COLOR_LIGHT
    borderPainted = false
    focusPainted = false

  background = Color.BLACK
  contents += undoButton
  contents += redoButton
  
  def update(): Unit =
    undoButton.enabled = controller.canUndo
    redoButton.enabled = controller.canRedo