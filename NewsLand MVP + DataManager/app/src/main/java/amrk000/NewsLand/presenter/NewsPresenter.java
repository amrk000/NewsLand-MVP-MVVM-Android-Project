package amrk000.NewsLand.presenter;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import amrk000.NewsLand.data.DataManager;
import amrk000.NewsLand.model.NewsItem;
import amrk000.NewsLand.model.RestApiStatus;
import amrk000.NewsLand.view.INewsView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class NewsPresenter implements INewsPresenter {
    private INewsView view;
    private DataManager dataManager;
    private String country, language;

    private Disposable newsDisposable, responseDisposable;
    private BehaviorSubject<ArrayList<NewsItem>> news;
    private PublishSubject<RestApiStatus> restApiStatus;

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
        dataManager = new DataManager(context);
        country = dataManager.getCountryList().getCountryName(dataManager.getCountryCode());
        language = dataManager.getLanguageList().getLanguageName(dataManager.getLanguageCode());
        news = dataManager.getNews();
        restApiStatus = dataManager.getRestApiStatus();

        view.setActionBarData(country, language);

        news.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<NewsItem>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        newsDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<NewsItem> newsItems) {
                        view.setAdapterData(newsItems);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        restApiStatus.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Observer<RestApiStatus>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        responseDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull RestApiStatus restApiStatus) {

                        view.setLoading(restApiStatus.isLoading());

                        switch (restApiStatus.getRequestStatus()){
                            case RestApiStatus.DATA_LOADED:
                                view.notifyItemsInserted(restApiStatus.getLoadedDataCount());
                                break;

                            case RestApiStatus.ALL_DONE:
                                view.setAllDone(true);
                                break;

                            case RestApiStatus.API_ERROR_MESSAGE:
                                if(restApiStatus.getResponseErrorMessage().contains("rateLimited")) view.showRequestLimitedDialog();
                                break;

                            case RestApiStatus.API_REQUEST_WRONG_KEY:
                                view.showWrongKeyDialog();
                                break;

                            case RestApiStatus.API_REQUEST_NO_RESULTS:
                                view.showNoResultsDialog(country,language);
                                break;

                            case RestApiStatus.API_REQUEST_FAILED:
                                restApiStatus.getFailException().printStackTrace();
                                break;
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void setCategoryIndex(int categoryIndex){
        this.categoryIndex = categoryIndex;
    }

    @Override
    public void initialRequest(){
        dataManager.initialRequest(categoryIndex);
    }

    @Override
    public void loadData(){
        dataManager.loadData(categoryIndex);
    }

    @Override
    public boolean isFirstLaunch(){
        return dataManager.isFirstLaunch();
    }

    @Override
    public void onDestroy() {
        newsDisposable.dispose();
        responseDisposable.dispose();
    }

}
