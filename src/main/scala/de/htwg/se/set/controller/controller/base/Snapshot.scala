package de.htwg.se.set.controller.controller.base

import de.htwg.se.set.controller.IState
import de.htwg.se.set.model.{IGame, ISettings}

case class Snapshot(settings: ISettings, game: IGame, state: IState)