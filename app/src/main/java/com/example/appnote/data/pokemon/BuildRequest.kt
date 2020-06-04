package com.example.appnote.data.pokemon

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class BuildRequest {

    var client: OkHttpClient = OkHttpClient()

    var request: Request = Request.Builder()
        .url("https://community-open-weather-map.p.rapidapi.com/weather?callback=test&id=2172797&units=%2522metric%2522%20or%20%2522imperial%2522&mode=xml%252C%20html&q=London%252Cuk")
        .get()
        .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
        .addHeader("x-rapidapi-key", "971edc2a5dmsh67218af3e55c9f3p17d2e4jsn4c174ba48683")
        .build()

    var response: Response = client.newCall(request).execute()
}