package de.htwg.se.set.util

trait Observer:
  def update(event: Event): Unit

trait Observable:
  var subscribers: Vector[Observer] = Vector()
  def add(s: Observer): Unit = subscribers = subscribers :+ s
  def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)
  def notifyObservers(event: Event): Unit = subscribers.foreach(o => o.update(event))

enum Event:
  case SETTINGS_CHANGED
  case CARDS_CHANGED
  case PLAYERS_CHANGED
  case COLUMNS_CHANGED