package com.gigamog;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Repeter {
    public String handleRequest(Object input, Context context) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://scraptik.p.rapidapi.com/user-posts?user_id=6926645323385406469&count=10&max_cursor=0"))
                .header("x-rapidapi-key", "bd6be0c5b3mshcd1bc0d86f0b777p10398ejsn209d8672abe1")
                .header("x-rapidapi-host", "scraptik.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        Root root = gson.fromJson(response.body(), Root.class);
        String discordHook = "https://discord.com/api/webhooks/865978199199842355/UFM8YavocYz7UKMJgbZULtm4ry1NtR4VpLIMLiEY0eAIFIQMz8BcxAyZIOn2Fak75QHb";

        for (Aweme aweme : root.aweme_list) {
            String share_url = aweme.share_url;
            Discord d = new Discord();
            Embed e = new Embed();
            DAuthor author = new DAuthor();
            e.color = 16711680;
            author.name = "Crypto is Good";
            author.icon_url = "https://p16-sign-va.tiktokcdn.com/tos-maliva-avt-0068/126c8428c2c57353a2763bdbd3cbe3e4~c5_720x720.jpeg?x-expires=1626624000&x-signature=zNjp4bBZmIigil98TB5MHD6Y6yM%3D";
            e.author = author;
            e.title = "Crypto is Good";
            e.url = share_url;
            e.description = aweme.desc;
            Image image = new Image();
            image.url = aweme.video.cover.url_list.stream().findFirst().orElse(null);
            e.image = image;
            Footer footer = new Footer();
            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(aweme.create_time), TimeZone.getDefault().toZoneId());
            if (date.getDayOfYear() == LocalDateTime.now().getDayOfYear()) {
                footer.text = "Published at " + date.format(DateTimeFormatter.ISO_LOCAL_DATE);
                e.footer = footer;
                d.embeds = List.of(e);

                HttpRequest drequest = HttpRequest.newBuilder()
                        .uri(URI.create(discordHook))
                        .header("Content-Type", "application/json")
                        .method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(d)))
                        .build();
                HttpResponse<String> res = HttpClient.newHttpClient().send(drequest, HttpResponse.BodyHandlers.ofString());

            }

        }


        return "Hello World - " + input;
    }
}
