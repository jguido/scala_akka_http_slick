package db.models

case class DbUser(id: UserId, principal: String, credential: Array[Byte])
case class UserId(value: String) extends Guid