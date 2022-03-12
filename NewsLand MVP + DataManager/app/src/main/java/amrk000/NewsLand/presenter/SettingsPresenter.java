package amrk000.NewsLand.presenter;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import amrk000.NewsLand.data.DataManager;
import amrk000.NewsLand.data.local.CountryList;
import amrk000.NewsLand.data.local.LanguageList;
import amrk000.NewsLand.model.SettingItem;
import amrk000.NewsLand.view.ISettingsView;

public class SettingsPresenter implements ISettingsPresenter{
    private ISettingsView view;
    private DataManager dataManager;
    private ArrayList<SettingItem> settings;

    public SettingsPresenter(Context context, ISettingsView settingsView) {
        this.view = settingsView;
        dataManager = new DataManager(context);
        settings = new ArrayList<>();
        settings.add(new SettingItem("API Key", getApiKey()));
        settings.add(new SettingItem("Language",getLanguageList().getLanguageName(getLanguageCode())));
        settings.add(new SettingItem("Country",getCountryList().getCountryName(getCountryCode())));
    }

    public ArrayList<SettingItem> getSettings(){
        return settings;
    }

    @Override
    public boolean isFirstLaunch() {
        return dataManager.isFirstLaunch();
    }

    @Override
    public void setFirstLaunch(boolean firstLaunch) {
        dataManager.setFirstLaunch(firstLaunch);
    }

    @Override
    public void setApiKeySetting(String key) {
        dataManager.setApiKey(key);
        settings.get(0).setValue(key);
        view.updateSettingsList(settings);
    }

    @Override
    public void setLanguageSetting(int index) {
        dataManager.setLanguageCode(getLanguageList().getLanguageCode(index));
        settings.get(1).setValue(getLanguageList().getLanguageName(index));
        view.updateSettingsList(settings);
    }

    @Override
    public void setCountrySetting(int index) {
        dataManager.setCountryCode(getCountryList().getCountryCode(index));
        settings.get(2).setValue(getCountryList().getCountryName(index));
        view.updateSettingsList(settings);
    }

    @Override
    public CountryList getCountryList() {
        return dataManager.getCountryList();
    }

    @Override
    public LanguageList getLanguageList() {
        return dataManager.getLanguageList();
    }

    @Override
    public String getApiKey() {
        return dataManager.getApiKey();
    }

    @Override
    public String getLanguageCode() {
        return dataManager.getLanguageCode();
    }

    @Override
    public String getCountryCode() {
        return dataManager.getCountryCode();
    }

}
