package amrk000.NewsLand.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;

import amrk000.NewsLand.BuildConfig;
import amrk000.NewsLand.R;
import amrk000.NewsLand.databinding.ActivityAboutBinding;

public class About extends AppCompatActivity {
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_NewsLand);
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        overridePendingTransition(R.anim.about_activity, android.R.anim.fade_out);

        getSupportActionBar().setTitle("About App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.credit.setVisibility(View.INVISIBLE);

        binding.appversion.setText("Version "+ BuildConfig.VERSION_NAME);

        binding.moreapps.setOnClickListener((View v)->{
            Intent profilelink = new Intent(Intent.ACTION_VIEW);
            profilelink.setData(Uri.parse("https://play.google.com/store/apps/dev?id=5289896800613171020"));
            startActivity(profilelink);
        });

        binding.githubrepo.setOnClickListener((View v)->{
            Intent profilelink = new Intent(Intent.ACTION_VIEW);
            profilelink.setData(Uri.parse("https://github.com/amrk000/NewsLand"));
            startActivity(profilelink);
        });

        new Handler().postDelayed(()->{
            binding.credit.setVisibility(View.VISIBLE);
            binding.credit.startAnimation(AnimationUtils.loadAnimation(this,R.anim.about_section));
        },250);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}