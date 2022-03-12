package amrk000.NewsLand.di.module;

import android.content.Context;

import javax.inject.Singleton;

import amrk000.NewsLand.data.local.CountryList;
import amrk000.NewsLand.data.local.LanguageList;
import dagger.Module;
import dagger.Provides;

@Module
public class XmlDataModule {

    @Provides
    @Singleton
    CountryList provideCountryList(Context context){
        return new CountryList(context);
    }

    @Provides
    @Singleton
    LanguageList provideLanguageList(Context context){
        return new LanguageList(context);
    }

}
