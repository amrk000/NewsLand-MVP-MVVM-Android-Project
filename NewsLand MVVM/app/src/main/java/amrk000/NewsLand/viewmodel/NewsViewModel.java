package amrk000.NewsLand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import amrk000.NewsLand.data.Repository;
import amrk000.NewsLand.data.local.CountryList;
import amrk000.NewsLand.data.local.LanguageList;
import amrk000.NewsLand.model.NewsItem;
import amrk000.NewsLand.model.RestApiStatus;

public class NewsViewModel extends AndroidViewModel {
    private Repository repository;
    private MutableLiveData<ArrayList<NewsItem>> news;
    private MutableLiveData<RestApiStatus> restApiStatus;

    private int categoryIndex = 0;

    public static int GENERAL = 0;
    public static int SCIENCE = 1;
    public static int BUSINESS = 2;
    public static int TECHNOLOGY = 3;
    public static int HEALTH = 4;
    public static int SPORTS = 5;
    public static int ENTERTAINMENT = 6;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        news = repository.getNews();
        restApiStatus = repository.getRestApiStatus();
    }

    public MutableLiveData<ArrayList<NewsItem>> getNews(){
        return news;
    }

    public MutableLiveData<RestApiStatus> getRestApiStatus() {
        return restApiStatus;
    }

    public CountryList getCountryList() { return repository.getCountryList();}
    public LanguageList getLanguageList() { return repository.getLanguageList();}

    public void setCategoryIndex(int categoryIndex){
        this.categoryIndex = categoryIndex;
    }

    public void initialRequest(){
        repository.initialRequest(categoryIndex);
    }

    public void loadData(){
        repository.loadData(categoryIndex);
    }

}
