package models.sm

import org.koala.sporm.jpa.BaseBuilder
import org.koala.sporm.jpa.CQModel

import models.jm.Game

object GameModel extends CQModel[Game] {
  implicit def extendModel(entity: Game) = new BaseBuilder(entity)
}
