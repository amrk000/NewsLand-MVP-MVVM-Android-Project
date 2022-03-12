package amrk000.NewsLand.di.module;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import amrk000.NewsLand.data.local.DataBase;
import amrk000.NewsLand.data.local.RoomDao;
import dagger.Module;
import dagger.Provides;

@Module
public class RoomDbModule {

    @Provides
    @Singleton
    DataBase provideDatabase(Context context){
        return Room.databaseBuilder(context, DataBase.class,"db") //database name
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    RoomDao provideDao(DataBase dataBase){
        return dataBase.daoAccess();
    }

}
