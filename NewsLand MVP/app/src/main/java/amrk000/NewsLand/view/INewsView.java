package amrk000.NewsLand.view;

import java.util.ArrayList;

import amrk000.NewsLand.model.NewsItem;

public interface INewsView {
    void setActionBarData(String country, String language);
    void setAdapterData(ArrayList<NewsItem> news);
    void notifyItemsInserted(int count);
    void setLoading(boolean loading);
    void setAllDone(boolean allDone);
    void showRequestLimitedDialog();
    void showWrongKeyDialog();
    void showNoResultsDialog(String country, String language);
}
