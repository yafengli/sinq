package models.sm

import org.koala.sporm.jpa.BaseBuilder
import org.koala.sporm.jpa.CQModel

import models.jm.Game

object GameActiveRecord extends CQModel[Game] {
  implicit def gameExtend(entity: Game) = new BaseBuilder(entity)
}
