package models

import java.net.URL

import slick.ast.BaseTypedType
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcType

object CustomColumnTypes {
  implicit val urlStringMapping: JdbcType[URL] with BaseTypedType[URL] = MappedColumnType.base[URL, String](
    url => url.toString,
    str => new URL(str)
  )
}
