package models.cards

import models.DerivedPlayerState
import models.GameState
import util.LeafyFormat

trait Card {
  def prettyName: String
  def generatePlayActionDerivedPlayerState(gameState:GameState): DerivedPlayerState
}

object Card {
  implicit val leafyFormat: LeafyFormat[Card] = LeafyFormat
    .root[Card]
    .leaf[Agriculture.type]
    .leaf[Bronze.type]
    .leaf[Iron.type]
    .leaf[Philosophy.type]
}
