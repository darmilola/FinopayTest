package com.nrs.finopaytest.domain.model

enum class City(val cityName: String, val countryCode: String) {
    LAGOS("Lagos", "NG"),
    ABUJA("Abuja", "NG"),
    IBADAN("Ibadan", "NG"),
    KANO("Kano", "NG"),
    PORT_HARCOURT("Port Harcourt", "NG"),
    LONDON("London", "GB"),
    NEW_YORK("New York", "US"),
    TOKYO("Tokyo", "JP"),
    PARIS("Paris", "FR"),
    BERLIN("Berlin", "DE"),
    DUBAI("Dubai", "AE"),
    SYDNEY("Sydney", "AU"),
    NAIROBI("Nairobi", "KE"),
    JOHANNESBURG("Johannesburg", "ZA"),
    CAIRO("Cairo", "EG");

    override fun toString(): String {
        return "$cityName,$countryCode"
    }
}
