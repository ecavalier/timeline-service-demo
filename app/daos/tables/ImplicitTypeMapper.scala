package daos.tables

import java.sql.Timestamp
import java.time.LocalDateTime

/**
  * Created by rootop on 2017-06-19.
  */
trait ImplicitTypeMapper {
  this: SlickRepository => // has to be mixed in with SlickRepository

  import drivers.api._

  val localDateTimeToDate = MappedColumnType.base[LocalDateTime, Timestamp](
    l => Timestamp.valueOf(l),
    t => t.toLocalDateTime
  )

  val optionLocalDateTimeToDate = MappedColumnType.base[Option[LocalDateTime], Timestamp](
    l => Timestamp.valueOf(l.get),
    t => Some(t.toLocalDateTime)
  )
}
