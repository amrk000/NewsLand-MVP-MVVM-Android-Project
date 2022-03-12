package amrk000.NewsLand.data.local;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

import amrk000.NewsLand.R;
import amrk000.NewsLand.model.Country;
import amrk000.NewsLand.model.Language;

public class LanguageList {
    private ArrayList<String> languagesNames;
    private ArrayList<String> languagesCodes;
    private ArrayList<Language> Languages;

    public LanguageList(Context context){
        languagesNames = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.languages)));
        languagesCodes = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.languagesCodes)));
        Languages = new ArrayList<>();

        for (int i=0; i<languagesCodes.size(); i++){
            String name = languagesNames.get(i);
            String code = languagesCodes.get(i);
            Languages.add(new Language(name,code));
        }

    }

    public ArrayList<Language> getList() {
        return Languages;
    }

    public Language getLanguage(int i) {
        return Languages.get(i);
    }

    public String getLanguageCode(String name) {
        return languagesCodes.get(languagesNames.indexOf(name));
    }

    public String getLanguageCode(int index) {
        return languagesCodes.get(index);
    }

    public String getLanguageName(String code) {
        return languagesNames.get(languagesCodes.indexOf(code));
    }

    public String getLanguageName(int index) {
        return languagesNames.get(index);
    }

    public int getIndexByCode(String code){
        return languagesCodes.indexOf(code);
    }

    public int getIndexByName(String name){
        return languagesNames.indexOf(name);
    }

}
