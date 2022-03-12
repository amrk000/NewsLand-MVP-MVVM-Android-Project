package amrk000.NewsLand.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewsApiResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private ArrayList<NewsItem> articles;


    public ArrayList<NewsItem> getArticles() {
        return articles;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public String getStatus() {
        return status;
    }

    public void setArticles(ArrayList<NewsItem> articles) {
        this.articles = articles;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
