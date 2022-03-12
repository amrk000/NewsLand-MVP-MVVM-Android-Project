package amrk000.NewsLand.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import amrk000.NewsLand.model.NewsItem;

//Data Access object
@Dao
public interface RoomDao {
    //add news item to blocked list
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addRecord(NewsItem tableRecord);

    //show all blocked list
    @Query("select * from deletedPosts")
    public List<NewsItem> getData();

    //check if item is blocked
    @Query("SELECT EXISTS(SELECT * FROM deletedPosts WHERE url = :articleUrl)")
    public boolean itemExists(String articleUrl);

}