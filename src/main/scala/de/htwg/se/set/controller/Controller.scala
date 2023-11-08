package de.htwg.se.set.controller

import de.htwg.se.set.model.Settings
import de.htwg.se.set.util.Observable

case class Controller(var settings: Settings) extends Observable:

  def setPlayerCount(count: Int): Unit =
    settings = settings.copy(playerCount = count)
    notifyObservers()
    
  def setEasy(easy: Boolean): Unit =
    settings = settings.copy(easy = easy)
    notifyObservers()

  def settingsToString: String = settings.toString