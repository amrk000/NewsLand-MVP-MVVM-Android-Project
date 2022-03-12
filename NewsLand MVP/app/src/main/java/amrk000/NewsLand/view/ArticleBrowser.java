package amrk000.NewsLand.view;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import amrk000.NewsLand.R;
import amrk000.NewsLand.databinding.ActivityArticleBrowserBinding;

public class ArticleBrowser extends AppCompatActivity {

    private ActivityArticleBrowserBinding binding;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_NewsLand);
        super.onCreate(savedInstanceState);
        binding = ActivityArticleBrowserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();

        url = getIntent().getStringExtra("url");
        binding.browserTitle.setText(getIntent().getStringExtra("title"));

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView,true);

        binding.webView.getSettings().setAllowContentAccess(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.getSettings().setSupportZoom(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && isDarkModeEnabled()) binding.webView.getSettings().setForceDark(WebSettings.FORCE_DARK_ON);

        binding.webView.loadUrl(url);

        binding.webView.setWebViewClient(new WebViewClient(){});

        binding.webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                ObjectAnimator animation = ObjectAnimator.ofInt(binding.webViewprogressBar, "progress", newProgress);
                animation.setDuration(2000);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.start();

            }

        });

        binding.refreshBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.webView.reload();
            }
        });

        binding.closeBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(binding.webView.canGoBack()) binding.webView.goBack();
        else super.onBackPressed();
    }

    public boolean isDarkModeEnabled() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                return false;
            case Configuration.UI_MODE_NIGHT_YES:
                return true;
        }

        return false;
    }
}