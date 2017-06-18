package models

import java.time.LocalDateTime

import io.swagger.annotations.ApiModelProperty

/**
  * Created by therootop on 16/11/2016.
  */
class Swagger {
}
case class UserResponse(
  @ApiModelProperty(example = "1", required = true) id: Long,
  @ApiModelProperty(example = "john.doe@example.com", required = true) email: String,
  @ApiModelProperty(example = "John Doe", required = true) name: String,
  @ApiModelProperty(example = "2016-11-16T11:04:20.528Z", required = true) createdAt: LocalDateTime
)

case class UserBody(
  @ApiModelProperty(example = "john.doe@example.com", required = true) email: String,
  @ApiModelProperty(example = "John Doe", required = true) name: String,
  @ApiModelProperty(example = "V3ry_$tr0n9_P@ssword", required = true) password: String
)

case class UserBodyOption(email: Option[String], name: Option[String], password: Option[String])

case class FollowStatus(incoming_status: Option[String], outgoing_status: Option[String])

case class PostResponse(
  @ApiModelProperty(example = "1", required = true) id: Long,
  @ApiModelProperty(example = "1", required = true) uid: String,
  @ApiModelProperty(example = "뜨고, 이상은 것이다.보라, 살았으며, 있는 싹이 이것은 사막이다. 같은 미인을 보내는 때문이다. 우리의 모래뿐일 그들을 내려온 뼈 산야에 오아이스도 돋고, 끓는다.", required = true) text: String,
  @ApiModelProperty(example = "2016-11-16T11:04:20.528Z", required = true) createdAt: LocalDateTime
)

case class PostBody(
  @ApiModelProperty(example = "1", required = true) uid: Long,
  @ApiModelProperty(example = "미묘한 소담스러운 뭇 모래뿐일 아름답고 것은 가장 ? 그들은 대고, 황금시대의 청춘 교향악이다. 어디 길을 전인 약동하다. 거친 역사를 이상의 황금시대다.", required = true) text: String
)

case class PostText(
  @ApiModelProperty(example = "돋고, 위하여서 자신과 이상이 가슴이 따뜻한 용기가 보라. 미인을 인간은 이것이야말로 동산에는 든 이것이다. 커다란 열매를 있는 그들을 이것이다. 우리는 천고에 꽃이 봄바람이다.", required = true) text: String
)