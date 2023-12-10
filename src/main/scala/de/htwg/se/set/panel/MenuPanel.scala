package de.htwg.se.set.panel

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{AddColumnAction, GameMode, RedoAction, UndoAction}
import de.htwg.se.set.util.PanelUtil.CompatButton
import de.htwg.se.set.util.{PanelUtil, ResUtil}

import java.awt.Color
import javax.swing.border.MatteBorder
import scala.swing.FlowPanel
import scala.swing.event.ButtonClicked

case class MenuPanel(controller: Controller) extends FlowPanel(FlowPanel.Alignment.Center)():

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

  background = ResUtil.COLOR_BG
  border = new MatteBorder(0, 0, 2, 0, Color.BLACK)
  contents += undoButton
  contents += redoButton
  contents += addButton
  update()
  
  def update(): Unit =
    undoButton.enabled = controller.canUndo
    redoButton.enabled = controller.canRedo
    addButton.enabled = controller.game.selectedPlayer.isEmpty
    addButton.visible = controller.settings.mode == GameMode.IN_GAME && !controller.settings.singlePlayer