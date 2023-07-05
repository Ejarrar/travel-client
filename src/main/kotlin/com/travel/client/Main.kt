package com.travel.client

import com.travel.client.response.TravelLocationDto
import com.travel.client.response.TravelLocationListDto
import org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient


fun main() {
    val baseUrl = "http://localhost:9090/v1/locations"
    val client = webClient(baseUrl)

    val response = client
        .get()
        .uri(baseUrl)
        .retrieve()
        .bodyToMono(TravelLocationListDto::class.java)
        .block()

    val usAirports = filterUsAirports(response)

    println(usAirports)
}

private fun webClient(
    baseUrl: String
): WebClient {
    val username = "someuser"
    val password = "psw"

    val size = 16 * 1024 * 1024
    val strategies = ExchangeStrategies.builder()
        .codecs { codecs: ClientCodecConfigurer ->
            codecs.defaultCodecs().maxInMemorySize(size)
        }
        .build()

    val client = WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(ACCEPT_LANGUAGE, "NL")
        .defaultHeaders { header ->
            header.setBasicAuth(username, password)
            header.contentType = APPLICATION_JSON
        }
        .exchangeStrategies(strategies)
        .build()
    return client
}

private fun filterUsAirports(response: TravelLocationListDto?): List<TravelLocationDto>? {
    val usCities = response?.travelLocations?.filter { location ->
        location.parentType == "country" && location.parentCode == "US"
    }?.map { travelLocationDto -> travelLocationDto.code }

    return response?.travelLocations?.filter { travelLocationDto -> usCities!!.contains(travelLocationDto.parentCode) }
}
