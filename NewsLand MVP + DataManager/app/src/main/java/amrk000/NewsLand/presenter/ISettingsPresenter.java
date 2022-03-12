package amrk000.NewsLand.presenter;

import java.util.ArrayList;

import amrk000.NewsLand.data.local.CountryList;
import amrk000.NewsLand.data.local.LanguageList;
import amrk000.NewsLand.model.SettingItem;

public interface ISettingsPresenter {
    ArrayList<SettingItem> getSettings();
    boolean isFirstLaunch();
    void setFirstLaunch(boolean firstLaunch);
    void setApiKeySetting(String key);
    void setLanguageSetting(int index);
    void setCountrySetting(int index);
    CountryList getCountryList();
    LanguageList getLanguageList();
    String getApiKey();
    String getLanguageCode();
    String getCountryCode();
}
