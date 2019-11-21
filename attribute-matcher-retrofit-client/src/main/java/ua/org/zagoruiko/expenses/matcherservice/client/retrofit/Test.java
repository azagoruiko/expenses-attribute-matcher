package ua.org.zagoruiko.expenses.matcherservice.client.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.org.zagoruiko.expenses.matcherservice.dto.MatcherSetDTO;

import java.io.IOException;

public class Test {
    static final String BASE_URL = "http://localhost:8080/";
    public static void main(String[] args) throws IOException {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MatcherServiceClient client = retrofit.create(MatcherServiceClient.class);
        MatcherSetDTO matcherSet = client.matchers("pb").execute().body();
        matcherSet.getCategoryMatcher();
    }
}
