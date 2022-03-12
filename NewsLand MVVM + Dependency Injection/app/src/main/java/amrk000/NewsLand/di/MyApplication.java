package amrk000.NewsLand.di;

import android.app.Application;

import amrk000.NewsLand.di.component.AppComponent;

import amrk000.NewsLand.di.component.DaggerAppComponent;
import amrk000.NewsLand.di.module.AppModule;

public class MyApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
