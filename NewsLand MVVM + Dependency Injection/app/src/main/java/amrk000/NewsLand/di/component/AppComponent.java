package amrk000.NewsLand.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import amrk000.NewsLand.data.Repository;
import amrk000.NewsLand.di.module.AppModule;
import amrk000.NewsLand.di.module.RepositoryModule;
import amrk000.NewsLand.di.module.RetrofitModule;
import amrk000.NewsLand.di.module.RoomDbModule;
import amrk000.NewsLand.di.module.ViewModelModule;
import amrk000.NewsLand.di.module.XmlDataModule;
import amrk000.NewsLand.view.MainActivity;
import amrk000.NewsLand.view.Settings;
import amrk000.NewsLand.viewmodel.NewsViewModel;
import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {AppModule.class, RepositoryModule.class, ViewModelModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(Settings settingsActivity);
}
