package amrk000.NewsLand.presenter;

import android.content.Context;

import java.util.ArrayList;

import amrk000.NewsLand.data.local.CountryList;
import amrk000.NewsLand.data.local.DataBase;
import amrk000.NewsLand.data.local.LanguageList;
import amrk000.NewsLand.data.local.RoomDao;
import amrk000.NewsLand.data.remote.RetrofitClient;
import amrk000.NewsLand.data.remote.RetrofitInterface;
import amrk000.NewsLand.model.NewsApiResponse;
import amrk000.NewsLand.model.NewsItem;
import amrk000.NewsLand.util.SharedPrefManager;
import amrk000.NewsLand.view.INewsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsPresenter implements INewsPresenter {
    private INewsView view;
    private Context context;
    private ArrayList<NewsItem> news;
    private String country, language;

    //Data Providers
    private RoomDao roomDao;
    private Retrofit retrofit;

    //Rest Api Data
    private String[] categories = {"general", "science", "business", "technology", "health", "sports", "entertainment"};
    private String sortBy = "publishedAt";
    private int postsPerPage = 10;
    private int currentPage;
    private String apiKey;
    private String countryCode;
    private String languageCode;

    //Countris & Languages
    private CountryList countryList;
    private LanguageList languageList;

    private int categoryIndex = 0;

    public static int GENERAL = 0;
    public static int SCIENCE = 1;
    public static int BUSINESS = 2;
    public static int TECHNOLOGY = 3;
    public static int HEALTH = 4;
    public static int SPORTS = 5;
    public static int ENTERTAINMENT = 6;

    public NewsPresenter(Context context, INewsView newsView) {
        this.view = newsView;
        this.context = context;

        roomDao = DataBase.get(context).daoAccess();
        retrofit = RetrofitClient.getInstance();
        countryList = new CountryList(context);
        languageList = new LanguageList(context);

        apiKey = SharedPrefManager.get(context).getApiKey();
        countryCode = SharedPrefManager.get(context).getCountryCode();
        languageCode = SharedPrefManager.get(context).getLanguageCode();
        country = countryList.getCountryName(countryCode);
        language = languageList.getLanguageName(languageCode);

        news = new ArrayList<>();

        view.setActionBarData(country, language);
    }

    private boolean isNewsBlocked(NewsItem newsItem){
        return roomDao.itemExists(newsItem.getArticleUrl());
    }

    @Override
    public void setCategoryIndex(int categoryIndex){
        this.categoryIndex = categoryIndex;
    }

    @Override
    public void initialRequest(){
        currentPage = 1;
        loadData();
    }

    @Override
    public void loadData(){
        //Free Api Max 100 posts
        if(currentPage>(100/postsPerPage)){
            view.setAllDone(true);
            return;
        }

        view.setLoading(true);

        retrofit.create(RetrofitInterface.class).getData(apiKey,countryCode,languageCode,categories[categoryIndex],currentPage,postsPerPage,sortBy).enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {

                view.setLoading(false);

                if(!response.isSuccessful()) {
                    try {

                        if(response.code()==401) view.showWrongKeyDialog();
                        else{
                            if(response.message().contains("rateLimited"))
                                view.showRequestLimitedDialog();
                        }

                    } catch (Exception e){e.printStackTrace();}
                    return;
                }

                int responseCount = response.body().getArticles().size();

                if(responseCount==0){
                    if(currentPage==1) view.showNoResultsDialog(country,language);
                    else view.setAllDone(true);

                    return;
                }

                int newsCount = 0;

                for(int i=0;i<responseCount;i++){
                    NewsItem newsItem = response.body().getArticles().get(i);

                    //Load News Post only if it wasn't blocked (Removed by User) before
                    if(!isNewsBlocked(newsItem)){
                        news.add(newsItem);
                        newsCount++;
                    }

                }

                view.setAdapterData(news);
                view.notifyItemsInserted(newsCount);

                currentPage++;

            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

    @Override
    public boolean isFirstLaunch(){
        return SharedPrefManager.get(context).isFirstLaunch();
    }

    @Override
    public void onDestroy() {

    }

}
