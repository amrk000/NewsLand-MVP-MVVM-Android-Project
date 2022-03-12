package amrk000.NewsLand.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import amrk000.NewsLand.model.NewsItem;

@Database(entities = {NewsItem.class}, version = 1, exportSchema = true)
public abstract class DataBase extends RoomDatabase {
    public abstract RoomDao daoAccess();
}
