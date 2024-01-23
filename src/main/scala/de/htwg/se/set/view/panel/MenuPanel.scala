package de.htwg.se.set.view.panel

import de.htwg.se.set.controller.IController
import de.htwg.se.set.controller.controller.*
import de.htwg.se.set.model.GameMode.IN_GAME
import de.htwg.se.set.util.PanelUtil.CompatButton
import de.htwg.se.set.util.{PanelUtil, ResUtil}
import play.api.libs.json.Json

import java.awt.Color
import java.io.File
import javax.swing.border.MatteBorder
import javax.swing.filechooser.FileNameExtensionFilter
import scala.io.Source
import scala.swing.FileChooser.{Result, SelectionMode}
import scala.swing.event.ButtonClicked
import scala.swing.{BoxPanel, FileChooser, Orientation}
import scala.util.Using
import scala.xml.XML

case class MenuPanel(controller: IController) extends BoxPanel(Orientation.Horizontal):

  private val menuFont = ResUtil.customFont("jost_medium", 16)
  
  private val undoButton = new CompatButton("UNDO"):
    font = menuFont
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(UndoAction())
        update()
    }
    
  private val redoButton = new CompatButton("REDO"):
    font = menuFont
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(RedoAction())
        update()
    }
    
  private val addButton = new CompatButton("ADD CARDS"):
    font = menuFont
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(AddColumnAction())
        update()
    }

  private val exitButton = new CompatButton("EXIT"):
    font = menuFont
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(ExitAction())
        update()
    }

  private val saveButton = new CompatButton("SAVE"):
    font = menuFont
    reactions += {
      case ButtonClicked(_) => controller.save()
    }

  private val openButton = new CompatButton("OPEN"):
    font = menuFont
    reactions += {
      case ButtonClicked(_) => filePicker match
        case Some(file) =>
          try
            val extension = file.getName.split("\\.").lastOption.getOrElse("")
            extension.toLowerCase match
              case "xml" =>
                val xml = XML.loadFile(file)
                controller.handleAction(LoadXmlAction(xml))
              case "json" =>
                val content = Using(Source.fromFile(file))(_.mkString).getOrElse("")
                val json = Json.parse(content)
                controller.handleAction(LoadJsonAction(json))
              case _ =>
                println("Unsupported file format")
          catch case e: Exception => e.printStackTrace()
        case None =>
    }

  background = ResUtil.COLOR_BG
  border = MatteBorder(0, 0, 2, 0, Color.BLACK)
  contents += undoButton
  contents += redoButton
  contents += addButton
  contents += exitButton
  contents += saveButton
  contents += openButton
  update()
  
  def update(): Unit =
    undoButton.enabled = controller.canUndo
    redoButton.enabled = controller.canRedo
    val setsAvailable = controller.game.deck.findSets(controller.game.tableCards).nonEmpty
    addButton.enabled = controller.game.selectedPlayer.isEmpty && 
      (controller.game.columns < 6 || (controller.game.columns >= 6 && !setsAvailable))
    addButton.visible = controller.settings.mode == IN_GAME && !controller.settings.singlePlayer
    exitButton.visible = controller.settings.mode == IN_GAME

  private def filePicker: Option[File] =
    val fileChooser = FileChooser()
    fileChooser.fileSelectionMode = SelectionMode.FilesOnly
    fileChooser.fileFilter = FileNameExtensionFilter("XML/JSON files", "xml", "json")
    val result = fileChooser.showOpenDialog(null)
    if result == Result.Approve then Some(fileChooser.selectedFile) else None