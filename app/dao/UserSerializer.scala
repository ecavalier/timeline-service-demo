package dao

import models.User
import org.json4s.JsonAST.{JBool, JField, JInt, JObject, JString, _}
import org.json4s.native.Serialization
import org.json4s.{CustomSerializer, NoTypeHints}

/**
  * Created by therootop on 11/13/16.
  */
class UserSerializer extends CustomSerializer[User](format => ({
  case JObject(_) => User(None, "", "", "", None)    // Deserialization 미구현
}, {
  case user: User =>
    val jsonFormats = Serialization.formats(NoTypeHints) + new UserSerializer
    JObject(
      JField("id", JDouble(user.id.get)) ::
        JField("email", JString(user.email)) ::
        JField("name", JString(user.name)) ::
        JField("createdAt", JString(user.createdAt.get.toString)) ::
        Nil
    )
})
)

