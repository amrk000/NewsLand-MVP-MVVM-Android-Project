package amrk000.NewsLand.presenter;

import android.content.Context;

import java.util.ArrayList;

import amrk000.NewsLand.data.local.CountryList;
import amrk000.NewsLand.data.local.LanguageList;
import amrk000.NewsLand.model.SettingItem;
import amrk000.NewsLand.util.SharedPrefManager;
import amrk000.NewsLand.view.ISettingsView;

public class SettingsPresenter implements ISettingsPresenter{
    private ISettingsView view;
    private Context context;
    private ArrayList<SettingItem> settings;

    //Countris & Languages
    private CountryList countryList;
    private LanguageList languageList;

    public SettingsPresenter(Context context, ISettingsView settingsView) {
        this.view = settingsView;
        this.context = context;
        countryList = new CountryList(context);
        languageList = new LanguageList(context);

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
        return SharedPrefManager.get(context).isFirstLaunch();
    }

    @Override
    public void setFirstLaunch(boolean firstLaunch) {
        SharedPrefManager.get(context).setFirstLaunch(firstLaunch);
    }

    @Override
    public void setApiKeySetting(String key) {
        SharedPrefManager.get(context).setApiKey(key);
        settings.get(0).setValue(key);
        view.updateSettingsList(settings);
    }

    @Override
    public void setLanguageSetting(int index) {
        SharedPrefManager.get(context).setLanguageCode(getLanguageList().getLanguageCode(index));
        settings.get(1).setValue(getLanguageList().getLanguageName(index));
        view.updateSettingsList(settings);
    }

    @Override
    public void setCountrySetting(int index) {
        SharedPrefManager.get(context).setCountryCode(getCountryList().getCountryCode(index));
        settings.get(2).setValue(getCountryList().getCountryName(index));
        view.updateSettingsList(settings);
    }

    @Override
    public CountryList getCountryList() {
        return countryList;
    }

    @Override
    public LanguageList getLanguageList() {
        return languageList;
    }

    @Override
    public String getApiKey() {
        return SharedPrefManager.get(context).getApiKey();
    }

    @Override
    public String getLanguageCode() {
        return SharedPrefManager.get(context).getLanguageCode();
    }

    @Override
    public String getCountryCode() {
        return SharedPrefManager.get(context).getCountryCode();
    }

}
