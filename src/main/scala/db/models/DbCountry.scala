package db.models

case class DbCountry(id: CountryId, name: String, tax: Double, currency: String, shortCurrency: String, label: Option[String])

case class CountryId(value: Int) extends NumericalId

