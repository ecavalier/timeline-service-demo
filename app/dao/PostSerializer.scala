package dao

import models.Post
import org.json4s.JsonAST._
import org.json4s.{CustomSerializer, NoTypeHints}
import org.json4s.native.Serialization

/**
  * Created by jinwookim on 11/14/16.
  */
class PostSerializer extends CustomSerializer[Post](format => ({
  case JObject(_) => Post(None, -1, "", None)    // Deserialization 미구현
}, {
  case post: Post =>
    val jsonFormats = Serialization.formats(NoTypeHints) + new UserSerializer
    JObject(
      JField("id", JDouble(post.id.get)) ::
        JField("uid", JInt(post.uid)) ::
        JField("text", JString(post.text)) ::
        JField("createdAt", JString(post.createdAt.get.toString)) ::
        Nil
    )
})
)
