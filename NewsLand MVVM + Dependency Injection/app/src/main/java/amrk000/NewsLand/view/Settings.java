package amrk000.NewsLand.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import javax.inject.Inject;

import amrk000.NewsLand.R;
import amrk000.NewsLand.adapter.SettingsListViewAdapter;
import amrk000.NewsLand.databinding.ActivitySettingsBinding;
import amrk000.NewsLand.di.MyApplication;
import amrk000.NewsLand.di.ViewModelFactory;
import amrk000.NewsLand.util.SharedPrefManager;
import amrk000.NewsLand.viewmodel.SettingsViewModel;

public class Settings extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    private SettingsViewModel viewModel;
    @Inject ViewModelFactory viewModelFactory;

    private SettingsListViewAdapter settingsListViewAdapter;
    private boolean firstLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_NewsLand);
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ((MyApplication) getApplication()).getAppComponent().inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(SettingsViewModel.class);

        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstLaunch = SharedPrefManager.get(this).isFirstLaunch();

        if(!firstLaunch){
            binding.firstSettingsTitle.setVisibility(View.GONE);
            binding.firstSettingsSubTitle.setVisibility(View.GONE);
            binding.firstSettingsButton.setVisibility(View.GONE);
        }

        settingsListViewAdapter = new SettingsListViewAdapter(this,viewModel.getSettings().getValue());
        binding.settingsListView.setAdapter(settingsListViewAdapter);

        viewModel.getSettings().observe(this, settingItems ->{
            settingsListViewAdapter.updateList(settingItems);
        });

        binding.settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){

                    case 0:
                        showKeySettingDialog();
                        break;
                    case 1:
                        showLanguageList();
                        break;
                    case 2:
                        showCountryList();
                        break;
                }

            }
        });

        binding.firstSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(getIntent().getBooleanExtra("apikey",false)) showKeySettingDialog();
        setResult(Activity.RESULT_OK,null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(firstLaunch) SharedPrefManager.get(this).setFirstLaunch(false);
    }

    void showKeySettingDialog(){
        ApiKeySettingsDialog apiKeySettingsDialog = new ApiKeySettingsDialog(SharedPrefManager.get(this).getApiKey(), new ApiKeySettingsDialog.ApplyKeyListener() {
            @Override
            public void onApply(String apiKey) {
                viewModel.setApiKeySetting(apiKey);
            }
        });
        apiKeySettingsDialog.setStyle(DialogFragment.STYLE_NORMAL,R.style.AlertDialog);
        apiKeySettingsDialog.show(getSupportFragmentManager(),apiKeySettingsDialog.getTag());
    }

    void showLanguageList(){
        AlertDialog languagesDialog = new AlertDialog.Builder(this,R.style.AlertDialog).setSingleChoiceItems(R.array.languages, viewModel.getLanguageList().getIndexByName(settingsListViewAdapter.getItem(1).getValue()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.setLanguageSetting(which);
                dialog.dismiss();
            }
        }).create();
        languagesDialog.setTitle("Language");
        languagesDialog.show();
    }

    void showCountryList(){
        AlertDialog countriesDialog = new AlertDialog.Builder(this,R.style.AlertDialog).setSingleChoiceItems(R.array.countries, viewModel.getCountryList().getIndexByName(settingsListViewAdapter.getItem(2).getValue()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.setCountrySetting(which);
                dialog.dismiss();
            }
        }).create();
        countriesDialog.setTitle("Country");
        countriesDialog.show();
    }

}