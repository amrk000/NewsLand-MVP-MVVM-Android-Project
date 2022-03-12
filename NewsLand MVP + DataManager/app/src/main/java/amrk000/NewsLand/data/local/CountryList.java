package amrk000.NewsLand.data.local;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

import amrk000.NewsLand.R;
import amrk000.NewsLand.model.Country;

public class CountryList {
    private ArrayList<String> countriesCodes;
    private ArrayList<String> countriesNames;
    private ArrayList<Country> countries;

    public CountryList(Context context){
        countriesNames = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.countries)));
        countriesCodes = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.countriesCodes)));
        countries = new ArrayList<>();

        for (int i=0; i<countriesCodes.size(); i++){
            String name = countriesNames.get(i);
            String code = countriesCodes.get(i);
            countries.add(new Country(name,code));
        }

    }

    public ArrayList<Country> getList() {
        return countries;
    }

    public Country getCountry(int i) {
        return countries.get(i);
    }

    public String getCountryCode(String name) {
       return countriesCodes.get(countriesNames.indexOf(name));
    }

    public String getCountryCode(int index) {
        return countriesCodes.get(index);
    }

    public String getCountryName(String code) {
        return countriesNames.get(countriesCodes.indexOf(code));
    }

    public String getCountryName(int index) {
        return countriesNames.get(index);
    }

    public int getIndexByCode(String code){
        return countriesCodes.indexOf(code);
    }

    public int getIndexByName(String name){
        return countriesNames.indexOf(name);
    }

}
