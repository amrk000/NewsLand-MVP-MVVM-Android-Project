package amrk000.NewsLand.presenter;

public interface INewsPresenter {
    void setCategoryIndex(int index);
    void initialRequest();
    void loadData();
    boolean isFirstLaunch();
    void onDestroy();
}
