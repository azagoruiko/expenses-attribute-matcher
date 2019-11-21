package ua.org.zagoruiko.expenses.matcherservice.client.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ua.org.zagoruiko.expenses.matcherservice.dto.MatcherSetDTO;

public interface MatcherServiceClient {

    @GET("matchers/{provider}")
    Call<MatcherSetDTO> matchers(@Path("provider") String provider);


}
