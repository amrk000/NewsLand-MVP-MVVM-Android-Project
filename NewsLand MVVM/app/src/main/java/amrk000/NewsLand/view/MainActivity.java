package amrk000.NewsLand.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.chip.ChipGroup;

import java.util.Objects;

import amrk000.NewsLand.R;
import amrk000.NewsLand.adapter.NewsRecyclerViewAdapter;
import amrk000.NewsLand.databinding.ActivityMainBinding;
import amrk000.NewsLand.model.RestApiStatus;
import amrk000.NewsLand.util.NetworkChecker;
import amrk000.NewsLand.util.SharedPrefManager;
import amrk000.NewsLand.viewmodel.NewsViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NewsViewModel viewModel;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;

    private ActivityResultLauncher<Intent> settingsCallBack;

    private NetworkChecker networkChecker;

    private String country, language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_NewsLand);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        country = viewModel.getCountryList().getCountryName(SharedPrefManager.get(this).getCountryCode());
        language = viewModel.getLanguageList().getLanguageName(SharedPrefManager.get(this).getLanguageCode());

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.actionbar_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //to show icon
        getSupportActionBar().setElevation(0); //to remove shadow in light mode
        getSupportActionBar().setTitle(country+" News");
        getSupportActionBar().setSubtitle(language);

        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(this);
        binding.NewsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.NewsRecyclerView.setAdapter(newsRecyclerViewAdapter);
        newsRecyclerViewAdapter.setLastItemReachedListener(new NewsRecyclerViewAdapter.LastItemReachedListener() {
            @Override
            public void onLoadMore() {
                viewModel.loadData();
            }
        });

        viewModel.getNews().observe(this, newsItems -> {
            newsRecyclerViewAdapter.setData(newsItems);
        });

        viewModel.getRestApiStatus().observe(this, restApiStatus -> {

            if(!restApiStatus.isLoading()) binding.swiperefresh.setRefreshing(false);

            binding.NewsRecyclerView.post(()-> {
                newsRecyclerViewAdapter.setLoading(restApiStatus.isLoading());
            });

            switch (restApiStatus.getRequestStatus()){
                case RestApiStatus.DATA_LOADED:
                    binding.NewsRecyclerView.post(()-> {
                        newsRecyclerViewAdapter.notifyItemRangeInserted(newsRecyclerViewAdapter.getItemCount(), restApiStatus.getLoadedDataCount());
                    });
                    break;

                case RestApiStatus.ALL_DONE:
                    binding.NewsRecyclerView.post(()-> {
                        newsRecyclerViewAdapter.setAllDone(true);
                    });
                    break;

                case RestApiStatus.API_ERROR_MESSAGE:
                    if(restApiStatus.getResponseErrorMessage().contains("rateLimited")) requestsLimitedDialog();
                    break;

                case RestApiStatus.API_REQUEST_WRONG_KEY:
                    wrongKeyDialog();
                    break;

                case RestApiStatus.API_REQUEST_NO_RESULTS:
                    noResultsDialog(country,language);
                    break;

                case RestApiStatus.API_REQUEST_FAILED:
                    restApiStatus.getFailException().printStackTrace();
                    break;
            }

        });

        settingsCallBack = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                    }
                });

        //first time settings
        if(SharedPrefManager.get(this).isFirstLaunch()) settingsCallBack.launch(new Intent(getApplicationContext(), Settings.class).putExtra("firstLaunch",true));

        //Network Checker Broadcast Receiver
        networkChecker = new NetworkChecker(this);
        registerReceiver(networkChecker, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //Category Selector
        binding.chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                switch (checkedId){

                    case R.id.chip_general:
                        viewModel.setCategoryIndex(NewsViewModel.GENERAL);
                        break;

                    case R.id.chip_science:
                        viewModel.setCategoryIndex(NewsViewModel.SCIENCE);
                        break;

                    case R.id.chip_business:
                        viewModel.setCategoryIndex(NewsViewModel.BUSINESS);
                        break;

                    case R.id.chip_technology:
                        viewModel.setCategoryIndex(NewsViewModel.TECHNOLOGY);
                        break;

                    case R.id.chip_health:
                        viewModel.setCategoryIndex(NewsViewModel.HEALTH);
                        break;
                    case R.id.chip_sports:
                        viewModel.setCategoryIndex(NewsViewModel.SPORTS);
                        break;

                    case R.id.chip_entertainment:
                        viewModel.setCategoryIndex(NewsViewModel.ENTERTAINMENT);
                        break;

                }
                newsRecyclerViewAdapter.clearItems();
                viewModel.initialRequest();

            }
        });

        binding.chipGroup.check(R.id.chip_general); //initialize category on launch

        //Swipe to refresh Code
        binding.swiperefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.primary,getTheme()));
        binding.swiperefresh.setColorSchemeColors(getResources().getColor(R.color.white,getTheme()));

        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectivityManager connectivityManager = connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                if(connectivityManager.getActiveNetworkInfo()==null || !connectivityManager.getActiveNetworkInfo().isConnected())
                {
                    binding.swiperefresh.setRefreshing(false);
                    return;
                }

                newsRecyclerViewAdapter.clearItems();
                viewModel.initialRequest();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChecker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getOrder()){
            case 1:
                settingsCallBack.launch(new Intent(getApplicationContext(),Settings.class));
                break;

            case 2:
                startActivity(new Intent(getApplicationContext(), About.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void requestsLimitedDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.AlertDialog).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Free Api 100 requests limit reached");
        alertDialog.setMessage("Please wait 24h or Change API key through App Settings or Project Code.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Use Another API Key", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settingsCallBack.launch(new Intent(getApplicationContext(), Settings.class).putExtra("apikey",true));
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void wrongKeyDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.AlertDialog).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Invalid API Key");
        alertDialog.setMessage("Please Check your API Key in Settings");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Edit API Key", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settingsCallBack.launch(new Intent(getApplicationContext(),Settings.class).putExtra("apikey",true));
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void noResultsDialog(String country, String language){

        AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.AlertDialog).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("There are no News about "+country+" in "+language+" language");
        alertDialog.setMessage("You can select default language in settings to get news.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Change Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settingsCallBack.launch(new Intent(getApplicationContext(),Settings.class));
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
}