package amrk000.NewsLand.data;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import amrk000.NewsLand.data.local.CountryList;
import amrk000.NewsLand.data.local.DataBase;
import amrk000.NewsLand.data.local.LanguageList;
import amrk000.NewsLand.data.local.RoomDao;
import amrk000.NewsLand.data.remote.RetrofitClient;
import amrk000.NewsLand.data.remote.RetrofitInterface;
import amrk000.NewsLand.model.NewsApiResponse;
import amrk000.NewsLand.model.NewsItem;
import amrk000.NewsLand.model.RestApiStatus;
import amrk000.NewsLand.util.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Repository {
    private RoomDao roomDao;
    private Retrofit retrofit;
    private MutableLiveData<ArrayList<NewsItem>> news;
    private MutableLiveData<RestApiStatus> restApiStatus;

    private String[] categories = {"general", "science", "business", "technology", "health", "sports", "entertainment"};
    private String sortBy = "publishedAt";
    private int postsPerPage = 10;
    private int currentPage;
    private String apiKey;
    private String country;
    private String language;

    private CountryList countryList;
    private LanguageList languageList;

    public Repository(Context context){
        roomDao = DataBase.get(context).daoAccess();
        retrofit = RetrofitClient.getInstance();
        news = new MutableLiveData<>(new ArrayList<>());
        restApiStatus = new MutableLiveData<>();

        apiKey = SharedPrefManager.get(context).getApiKey();
        country = SharedPrefManager.get(context).getCountryCode();
        language = SharedPrefManager.get(context).getLanguageCode();

        countryList = new CountryList(context);
        languageList = new LanguageList(context);
    }

    public MutableLiveData<ArrayList<NewsItem>> getNews(){
        return news;
    }

    public void blockNews(NewsItem newsItem){
        roomDao.addRecord(newsItem);
    }

    public boolean isNewsBlocked(NewsItem newsItem){
        return roomDao.itemExists(newsItem.getArticleUrl());
    }

    public List<NewsItem> getBlockedNews(){
        return roomDao.getData();
    }

    public MutableLiveData<RestApiStatus> getRestApiStatus(){
        return restApiStatus;
    }

    public void initialRequest(int categoryIndex){
        currentPage = 1;
        loadData(categoryIndex);
    }

    public void loadData(int categoryIndex){
        RestApiStatus status = new RestApiStatus();

        //Free Api Max 100 posts
        if(currentPage>(100/postsPerPage)){
            status.setRequestStatus(RestApiStatus.ALL_DONE);
            restApiStatus.setValue(status);
            return;
        }

        status.setLoading(true);
        restApiStatus.setValue(status);

        retrofit.create(RetrofitInterface.class).getData(apiKey,country,language,categories[categoryIndex],currentPage,postsPerPage,sortBy).enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {

                status.setLoading(false);
                restApiStatus.postValue(status);

                if(!response.isSuccessful()) {
                    try {

                        if(response.code()==401) status.setRequestStatus(RestApiStatus.API_REQUEST_WRONG_KEY);
                        else {
                            status.setRequestStatus(RestApiStatus.API_ERROR_MESSAGE);
                            status.setResponseErrorMessage(response.errorBody().string());
                        }
                        restApiStatus.postValue(status);

                    } catch (Exception e){e.printStackTrace();}
                    return;
                }

                int responseCount = response.body().getArticles().size();

                if(responseCount==0){
                    if(currentPage==1) status.setRequestStatus(RestApiStatus.API_REQUEST_NO_RESULTS);
                    else status.setRequestStatus(RestApiStatus.ALL_DONE);

                    restApiStatus.postValue(status);
                    return;
                }

                int newsCount = 0;

                for(int i=0;i<responseCount;i++){
                    NewsItem newsItem = response.body().getArticles().get(i);

                    //Load News Post only if it wasn't blocked (Removed by User) before
                    if(!isNewsBlocked(newsItem)){
                        news.getValue().add(newsItem);
                        newsCount++;
                    }

                }

                news.postValue(news.getValue());

                status.setRequestStatus(RestApiStatus.DATA_LOADED);
                status.setLoadedDataCount(newsCount);
                restApiStatus.postValue(status);

                currentPage++;

            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                status.setLoading(false);
                status.setRequestStatus(RestApiStatus.API_REQUEST_FAILED);
                status.setFailException(t);
                restApiStatus.postValue(status);
            }

        });

    }

    public CountryList getCountryList(){ return countryList; }

    public LanguageList getLanguageList() { return languageList; }
}
