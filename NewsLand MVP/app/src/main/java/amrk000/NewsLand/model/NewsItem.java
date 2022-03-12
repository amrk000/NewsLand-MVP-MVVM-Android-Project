package amrk000.NewsLand.model;

import com.google.gson.annotations.SerializedName;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="deletedPosts")
public class NewsItem {

    @PrimaryKey(autoGenerate = true) //Room
    private int pk;

    @ColumnInfo(name="url") //Room
    @SerializedName("url") //Retrofit
    private String articleUrl;

    @Ignore //Room
    @SerializedName("source") //Retrofit
    private NewsSource source;

    @ColumnInfo(name="author") //Room
    @SerializedName("author") //Retrofit
    private String author;

    @ColumnInfo(name="publishedAt") //Room
    @SerializedName("publishedAt") //Retrofit
    private String date;

    @ColumnInfo(name="title") //Room
    @SerializedName("title") //Retrofit
    private String title;

    @ColumnInfo(name="urlToImage") //Room
    @SerializedName("urlToImage") //Retrofit
    private String image;

    public NewsItem(){
        author="anonymous";
        title="No Title";
        image="";
        source = new NewsSource();
        articleUrl="";
    }

    public NewsItem(String source, String author, String title, String image){
        this.source = new NewsSource();
        this.source.setName(source);
        this.author=author;
        this.title=title;
        this.image=image;
        this.articleUrl="";
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSource(NewsSource source) {
        this.source = source;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public NewsSource getSource() {
        return source;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getDate() {
        return date;
    }

    public int getPk() {
        return pk;
    }
}
