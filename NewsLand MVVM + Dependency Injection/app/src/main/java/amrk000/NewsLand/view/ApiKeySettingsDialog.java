package amrk000.NewsLand.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import amrk000.NewsLand.databinding.FragmentApiKeySettingsBinding;


public class ApiKeySettingsDialog extends DialogFragment {
    private FragmentApiKeySettingsBinding binding;
    private String apiKey;
    private ApplyKeyListener applyKeyListener;

    public interface ApplyKeyListener{
        void onApply(String apiKey);
    }

    public ApiKeySettingsDialog(String apiKey, ApplyKeyListener applyKeyListener) {
        this.apiKey = apiKey;
        this.applyKeyListener = applyKeyListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApiKeySettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.apiSettingsKey.getEditText().setText(apiKey);
        binding.apiSettingsKey.getEditText().setShowSoftInputOnFocus(true);
        binding.apiSettingsKey.getEditText().requestFocus();

        binding.apiSettingsApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyKeyListener.onApply(binding.apiSettingsKey.getEditText().getText().toString());
                dismiss();
            }
        });

        binding.apiSettingsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.apiSettingsGetKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ArticleBrowser.class)
                        .putExtra("url","https://newsapi.org/")
                        .putExtra("title","Get API Key"));
            }
        });
    }
}