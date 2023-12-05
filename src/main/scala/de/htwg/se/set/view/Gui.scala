package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{ChangePlayerCountAction, RedoAction, StartGameAction, SwitchEasyAction, UndoAction}
import de.htwg.se.set.util.{Event, Observer}

import java.awt.{Color, GraphicsEnvironment}
import javax.swing.ImageIcon
import javax.swing.border.{CompoundBorder, MatteBorder}
import scala.swing.*
import scala.swing.event.ButtonClicked

case class Gui(controller: Controller) extends Frame with Observer:

  controller.add(this)

  title = "SET Game"
  preferredSize = new Dimension(800, 600)
  resizable = false

  private val borderSize = 5
  private val menuFont = customFont("jost_medium", 16)
  private val settingsFont = customFont("jost_medium", 36)
  private val colorBg = Color.decode("#e9e3d3")
  private val colorRed = Color.decode("#da0100")
  private val colorOrange = Color.decode("#f9520a")
  private val colorYellow = Color.decode("#ffa800")
  private val colorBlue = Color.decode("#060488")
  private val colorLight = Color.decode("#f4f1b4")

  // MENU
  private val undoButton = new Button("UNDO"):
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(UndoAction)
        updateMenu()
    }
    font = menuFont
    foreground = colorLight
    borderPainted = false
    focusPainted = false
  private val redoButton = new Button("REDO"):
    reactions += {
      case ButtonClicked(_) =>
        controller.handleAction(RedoAction)
        updateMenu()
    }
    font = menuFont
    foreground = colorLight
    borderPainted = false
    focusPainted = false
  private val menuPanel = new FlowPanel(FlowPanel.Alignment.Center)():
    background = Color.BLACK
    contents += undoButton
    contents += redoButton

  // PLAYER COUNT
  private val playerCountLabelContainer = new GridPanel(1, 1):
    background = colorBg
    border = new MatteBorder(0, 0, 0, 1, Color.BLACK)
    contents += new Label("PLAYERS"):
      font = settingsFont
  private val playerCountLessButton = new Button():
    reactions += {
      case ButtonClicked(_) => controller.handleAction(ChangePlayerCountAction(controller.settings.playerCount - 1))
    }
    icon = ImageIcon("src/main/resources/ic_less.png")
    borderPainted = false
    focusPainted = false
  private val playerCountMoreButton = new Button():
    reactions += {
      case ButtonClicked(_) => controller.handleAction(ChangePlayerCountAction(controller.settings.playerCount + 1))
    }
    icon = ImageIcon("src/main/resources/ic_more.png")
    borderPainted = false
    focusPainted = false
  private val playerCountInputLabel = new Label(""):
    font = settingsFont
  private val playerCountInputContainer = new GridPanel(1, 3):
    background = colorBg
    private val innerBorder = Swing.EmptyBorder(0, 40, 0, 40)
    private val outerBorder = new MatteBorder(0, 1, 0, 0, Color.BLACK)
    border = new CompoundBorder(outerBorder, innerBorder)
    contents += playerCountLessButton
    contents += playerCountInputLabel
    contents += playerCountMoreButton
  private val playerCountPanel = new GridPanel(1, 2):
    contents += playerCountLabelContainer
    contents += playerCountInputContainer
    border = new MatteBorder(0, 0, 2, 0, Color.BLACK)

  private val easyLabelContainer = new GridPanel(1, 1):
    background = colorBg
    border = new MatteBorder(0, 0, 0, 1, Color.BLACK)
    contents += new Label("MODE"):
      font = settingsFont
  private val easyToggle: ToggleButton = new ToggleButton(""):
    reactions += {
      case ButtonClicked(_) => controller.handleAction(SwitchEasyAction)
    }
    font = settingsFont
    borderPainted = false
    focusPainted = false
  private val easyToggleContainer = new GridPanel(1, 1):
    border = new MatteBorder(0, 1, 0, 0, Color.BLACK)
    contents += easyToggle
  private val easyPanel = new GridPanel(1, 2):
    contents += easyLabelContainer
    contents += easyToggleContainer
    border = new MatteBorder(0, 0, 2, 0, Color.BLACK)

  // SETTINGS
  private val settingsPanel = new GridPanel(3, 1):
    background = colorLight
    contents += playerCountPanel
    contents += easyPanel
    contents += new Button("START GAME"):
      reactions += {
        case ButtonClicked(_) => controller.handleAction(StartGameAction)
      }
      foreground = colorBlue
      font = settingsFont
      borderPainted = false
      focusPainted = false

  // GAME
  private val gamePanel = new GridPanel(1, 1):
    background = colorBg
    // TODO: implement game panel

  updateMenu()
  updateSettings()
  updateFrame()

  centerOnScreen()
  open()

  override def update(event: Event): Unit =
    event match
      case Event.SETTINGS_CHANGED => updateSettings()
      case Event.CARDS_CHANGED => repaint()
      case Event.SETTINGS_OR_GAME_CHANGED => updateSettings()
      case Event.IN_GAME_CHANGED => updateFrame()
      case _ =>
    updateMenu()

  private def updateMenu(): Unit =
    undoButton.enabled = controller.canUndo
    redoButton.enabled = controller.canRedo

  private def updateSettings(): Unit =
    playerCountInputLabel.text = controller.settings.playerCount.toString
    playerCountMoreButton.enabled = controller.settings.playerCount < 10
    playerCountLessButton.enabled = controller.settings.playerCount > 1
    easyToggle.text = if controller.settings.easy then "EASY" else "NORMAL"
    easyToggleContainer.background = if controller.settings.easy then colorYellow else colorOrange

  private def updateFrame(): Unit =
    contents = new BorderPanel:
      layout(menuPanel) = BorderPanel.Position.North
      layout(if controller.settings.inGame then gamePanel else settingsPanel) = BorderPanel.Position.Center

  private def customFont(name: String, size: Int): Font =
    val stream = getClass.getResourceAsStream("/font/" + name + ".ttf")
    val font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, stream).deriveFont(java.awt.Font.PLAIN, size)
    GraphicsEnvironment.getLocalGraphicsEnvironment.registerFont(font)
    font