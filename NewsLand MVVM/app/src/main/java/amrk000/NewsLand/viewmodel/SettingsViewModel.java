package amrk000.NewsLand.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import amrk000.NewsLand.data.Repository;
import amrk000.NewsLand.data.local.CountryList;
import amrk000.NewsLand.data.local.LanguageList;
import amrk000.NewsLand.model.NewsItem;
import amrk000.NewsLand.model.SettingItem;
import amrk000.NewsLand.util.SharedPrefManager;

public class SettingsViewModel extends AndroidViewModel {
    private Repository repository;
    private MutableLiveData<ArrayList<SettingItem>> settings;
    private Context context;

    public SettingsViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
        repository = new Repository(application);
        settings = new MutableLiveData<>(new ArrayList<>());
        settings.getValue().add(new SettingItem("API Key", SharedPrefManager.get(application).getApiKey()));
        settings.getValue().add(new SettingItem("Language",getLanguageList().getLanguageName(SharedPrefManager.get(application).getLanguageCode())));
        settings.getValue().add(new SettingItem("Country",getCountryList().getCountryName(SharedPrefManager.get(application).getCountryCode())));
    }

    public MutableLiveData<ArrayList<SettingItem>> getSettings(){
        return settings;
    }

    public void setApiKeySetting(String key) {
        SharedPrefManager.get(context).setApiKey(key);
        settings.getValue().get(0).setValue(key);
        settings.setValue(settings.getValue());
    }

    public void setLanguageSetting(int index) {
        SharedPrefManager.get(context).setLanguageCode(getLanguageList().getLanguageCode(index));
        settings.getValue().get(1).setValue(getLanguageList().getLanguageName(index));
        settings.setValue(settings.getValue());
    }

    public void setCountrySetting(int index) {
        SharedPrefManager.get(context).setCountryCode(getCountryList().getCountryCode(index));
        settings.getValue().get(2).setValue(getCountryList().getCountryName(index));
        settings.setValue(settings.getValue());
    }

    public CountryList getCountryList() { return repository.getCountryList();}
    public LanguageList getLanguageList() { return repository.getLanguageList();}

}
