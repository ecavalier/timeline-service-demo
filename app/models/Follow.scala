package models

import java.time.LocalDateTime

/**
  * Created by jinwookim on 11/15/16.
  */
case class Follow(
  id: Option[Long],
  uid: Long,
  fid: Long,
  status: String,
  createdAt: Option[LocalDateTime]
)

object FollowAction extends Enumeration {
  type FollowAction = Value
  val Follow = "follow"
  val Unfollow = "unfollow"
  val Approve = "approve"
  val Ignore = "ignore"
}