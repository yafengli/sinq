package models.cm

import org.koala.sporm.jpa.CQModel

import models.jm.Author

object AuthorModel extends CQModel[Author] {

  import org.koala.sporm.jpa.BaseBuilder

  implicit def authorExtend(entity: Author) = new BaseBuilder(entity)
}
