package amrk000.NewsLand.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.Objects;

import amrk000.NewsLand.R;
import amrk000.NewsLand.adapter.NewsRecyclerViewAdapter;
import amrk000.NewsLand.databinding.ActivityMainBinding;
import amrk000.NewsLand.model.NewsItem;
import amrk000.NewsLand.presenter.INewsPresenter;
import amrk000.NewsLand.util.NetworkChecker;
import amrk000.NewsLand.presenter.NewsPresenter;

public class MainActivity extends AppCompatActivity implements INewsView {
    private ActivityMainBinding binding;
    private INewsPresenter presenter;

    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;

    private ActivityResultLauncher<Intent> settingsCallBack;

    private NetworkChecker networkChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_NewsLand);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new NewsPresenter(this,this);

        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(this);
        binding.NewsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.NewsRecyclerView.setAdapter(newsRecyclerViewAdapter);
        newsRecyclerViewAdapter.setLastItemReachedListener(new NewsRecyclerViewAdapter.LastItemReachedListener() {
            @Override
            public void onLoadMore() {
                presenter.loadData();
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
        if(presenter.isFirstLaunch()) settingsCallBack.launch(new Intent(getApplicationContext(), Settings.class).putExtra("firstLaunch",true));

        //Network Checker Broadcast Receiver
        networkChecker = new NetworkChecker(this);
        registerReceiver(networkChecker, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //Category Selector
        binding.chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                switch (checkedId){

                    case R.id.chip_general:
                        presenter.setCategoryIndex(NewsPresenter.GENERAL);
                        break;

                    case R.id.chip_science:
                        presenter.setCategoryIndex(NewsPresenter.SCIENCE);
                        break;

                    case R.id.chip_business:
                        presenter.setCategoryIndex(NewsPresenter.BUSINESS);
                        break;

                    case R.id.chip_technology:
                        presenter.setCategoryIndex(NewsPresenter.TECHNOLOGY);
                        break;

                    case R.id.chip_health:
                        presenter.setCategoryIndex(NewsPresenter.HEALTH);
                        break;
                    case R.id.chip_sports:
                        presenter.setCategoryIndex(NewsPresenter.SPORTS);
                        break;

                    case R.id.chip_entertainment:
                        presenter.setCategoryIndex(NewsPresenter.ENTERTAINMENT);
                        break;

                }
                newsRecyclerViewAdapter.clearItems();
                presenter.initialRequest();
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
                presenter.initialRequest();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChecker);
        presenter.onDestroy();
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

    @Override
    public void setActionBarData(String country, String language) {
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.actionbar_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //to show icon
        getSupportActionBar().setElevation(0); //to remove shadow in light mode
        getSupportActionBar().setTitle(country+" News");
        getSupportActionBar().setSubtitle(language);
    }

    @Override
    public void setAdapterData(ArrayList<NewsItem> news) {
        newsRecyclerViewAdapter.setData(news);
    }

    @Override
    public void notifyItemsInserted(int count) {
        binding.NewsRecyclerView.post(()-> {
            newsRecyclerViewAdapter.notifyItemRangeInserted(newsRecyclerViewAdapter.getItemCount(), count);
        });
    }

    @Override
    public void setLoading(boolean loading) {
        if(!loading) binding.swiperefresh.setRefreshing(false);

        binding.NewsRecyclerView.post(()-> {
            newsRecyclerViewAdapter.setLoading(loading);
        });
    }

    @Override
    public void setAllDone(boolean allDone) {
        binding.NewsRecyclerView.post(()-> {
            newsRecyclerViewAdapter.setAllDone(true);
        });
    }

    @Override
    public void showRequestLimitedDialog() {
        //if(restApiStatus.getResponseErrorMessage().contains("rateLimited")) requestsLimitedDialog();

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

    @Override
    public void showWrongKeyDialog() {
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

    @Override
    public void showNoResultsDialog(String country, String language) {
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