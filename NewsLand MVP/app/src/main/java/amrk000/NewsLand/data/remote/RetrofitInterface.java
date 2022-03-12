package amrk000.NewsLand.data.remote;

import amrk000.NewsLand.model.NewsApiResponse;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("top-headlines?")
    Call<NewsApiResponse> getData(@Query("apiKey") String apiKey,
                           @Query("country") String country,
                           @Query("language") String language,
                           @Query("category") String category,
                           @Query("page") int page,
                           @Query("pageSize") int size,
                           @Query("sortBy") String sortBy);

}
