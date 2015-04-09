package models

import models.PlayerState

import collection.immutable

case class DeltaPlayerState(
    newBuildings: immutable.Seq[Building],
    newTechs: immutable.Seq[Tech],
    newCivilHand: immutable.Seq[Card],
    removedCivilHand: immutable.Seq[Card],
    population: Int,
    ore: Int,
    food: Int,
    science: Int) {

  def +(other: DeltaPlayerState): DeltaPlayerState = {
    DeltaPlayerState(
      newBuildings = newBuildings ++ other.newBuildings,
      newTechs = newTechs ++ other.newTechs,
      newCivilHand = newCivilHand ++ other.newCivilHand,
      removedCivilHand = removedCivilHand ++ other.removedCivilHand,
      population = population + other.population,
      ore = ore + other.ore,
      food = food + other.food,
      science = science + other.science)
  }
}

object DeltaPlayerState {

  def applyDeltaPlayerState(deltaPlayerState: DeltaPlayerState): (PlayerState => PlayerState) = {
    (playerState: PlayerState) => PlayerState(
      buildings = playerState.buildings ++ deltaPlayerState.newBuildings,
      techs = playerState.techs ++ deltaPlayerState.newTechs,
      civilHand = (playerState.civilHand ++ deltaPlayerState.newCivilHand) diff deltaPlayerState.removedCivilHand,
      population = playerState.population + deltaPlayerState.population,
      ore = playerState.ore + deltaPlayerState.ore,
      food = playerState.food + deltaPlayerState.food,
      science = playerState.science + deltaPlayerState.science)
  }

  val empty = DeltaPlayerState(
    newBuildings = List.empty,
    newTechs = List.empty,
    newCivilHand = List.empty,
    removedCivilHand = List.empty,
    population = 0,
    ore = 0,
    food = 0,
    science = 0)
}