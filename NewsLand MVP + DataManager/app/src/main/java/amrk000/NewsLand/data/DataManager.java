package amrk000.NewsLand.data;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

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
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DataManager {
    private RoomDao roomDao;
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private BehaviorSubject<ArrayList<NewsItem>> news;
    private PublishSubject<RestApiStatus> restApiStatus;

    //Rest Api Data
    private String[] categories = {"general", "science", "business", "technology", "health", "sports", "entertainment"};
    private String sortBy = "publishedAt";
    private int postsPerPage = 10;
    private int currentPage;
    private String apiKey;
    private String country;
    private String language;

    //Countris & Languages
    private CountryList countryList;
    private LanguageList languageList;

    //Shared Preferences Data
    // Keys
    private static final String FIRST_LAUNCH = "firstlaunch";
    private static final String API_KEY = "apikey";
    private static final String LANGUAGE_CODE ="language";
    private static final String COUNTRY_CODE ="country";

    // Defaults
    public static final String DEFAULT_APIKEY = "552044257f4c467bbe47079e5ffa83a9"; //Get an API Key From: https://newsapi.org/
    public static final String DEFAULT_LANGUAGE="en";
    public static final String DEFAULT_COUNTRY=""; //empty = world wide news

    public DataManager(Context context){
        roomDao = DataBase.get(context).daoAccess();
        retrofit = RetrofitClient.getInstance();
        sharedPreferences = context.getSharedPreferences("settings",MODE_PRIVATE);
        news = BehaviorSubject.createDefault(new ArrayList<>());
        restApiStatus = PublishSubject.create();

        apiKey = getApiKey();
        country = getCountryCode();
        language = getLanguageCode();

        countryList = new CountryList(context);
        languageList = new LanguageList(context);
    }

    public BehaviorSubject<ArrayList<NewsItem>> getNews(){
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

    public PublishSubject<RestApiStatus> getRestApiStatus(){
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
            restApiStatus.onNext(status);
            return;
        }

        status.setLoading(true);
        restApiStatus.onNext(status);

        retrofit.create(RetrofitInterface.class).getData(apiKey,country,language,categories[categoryIndex],currentPage,postsPerPage,sortBy).enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {

                status.setLoading(false);
                restApiStatus.onNext(status);

                if(!response.isSuccessful()) {
                    try {

                        if(response.code()==401) status.setRequestStatus(RestApiStatus.API_REQUEST_WRONG_KEY);
                        else {
                            status.setRequestStatus(RestApiStatus.API_ERROR_MESSAGE);
                            status.setResponseErrorMessage(response.errorBody().string());
                        }
                        restApiStatus.onNext(status);

                    } catch (Exception e){e.printStackTrace();}
                    return;
                }

                int responseCount = response.body().getArticles().size();

                if(responseCount==0){
                    if(currentPage==1) status.setRequestStatus(RestApiStatus.API_REQUEST_NO_RESULTS);
                    else status.setRequestStatus(RestApiStatus.ALL_DONE);

                    restApiStatus.onNext(status);
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

                status.setRequestStatus(RestApiStatus.DATA_LOADED);
                status.setLoadedDataCount(newsCount);
                restApiStatus.onNext(status);

                currentPage++;

            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                status.setLoading(false);
                status.setRequestStatus(RestApiStatus.API_REQUEST_FAILED);
                status.setFailException(t);
                restApiStatus.onNext(status);
            }

        });

    }

    public CountryList getCountryList(){ return countryList; }

    public LanguageList getLanguageList() { return languageList; }

    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(FIRST_LAUNCH,true);
    }

    public String getApiKey() {
        return sharedPreferences.getString(API_KEY,DEFAULT_APIKEY);
    }

    public String getLanguageCode() {
        return sharedPreferences.getString(LANGUAGE_CODE,DEFAULT_LANGUAGE);
    }

    public String getCountryCode() {
        return sharedPreferences.getString(COUNTRY_CODE,DEFAULT_COUNTRY);
    }

    public void setFirstLaunch(boolean value){
        sharedPreferences.edit()
                .putBoolean(FIRST_LAUNCH,value)
                .apply();
    }

    public void setApiKey(String value){
        sharedPreferences.edit()
                .putString(API_KEY,value)
                .apply();
    }

    public void setLanguageCode(String value){
        sharedPreferences.edit()
                .putString(LANGUAGE_CODE,value)
                .apply();
    }

    public void setCountryCode(String value){
        sharedPreferences.edit()
                .putString(COUNTRY_CODE,value)
                .apply();
    }
}
