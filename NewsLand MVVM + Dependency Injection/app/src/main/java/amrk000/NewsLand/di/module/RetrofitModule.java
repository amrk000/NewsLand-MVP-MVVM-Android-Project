package amrk000.NewsLand.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {
    String url;

    public RetrofitModule() {
        url = "https://newsapi.org/v2/";
    }

    public RetrofitModule(String url) {
        this.url = url;
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGson() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofitClient(GsonConverterFactory gson) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(gson)
                .build();
    }

}
