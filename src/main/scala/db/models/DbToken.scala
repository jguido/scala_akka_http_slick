package db.models

case class DbToken(tokenId: TokenId, userId: UserId)
case class TokenId(value: String) extends Guid
