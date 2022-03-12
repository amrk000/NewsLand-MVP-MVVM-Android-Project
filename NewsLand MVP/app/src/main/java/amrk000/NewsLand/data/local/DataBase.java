package amrk000.NewsLand.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import amrk000.NewsLand.model.NewsItem;

@Database(entities = {NewsItem.class}, version = 1, exportSchema = true)
public abstract class DataBase extends RoomDatabase {
    private static DataBase db;
    public abstract RoomDao daoAccess();

    public static DataBase get(Context context){
        if(db==null){
            db = Room.databaseBuilder(context, DataBase.class,"db") //database name
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        return db;
    }
    
}
