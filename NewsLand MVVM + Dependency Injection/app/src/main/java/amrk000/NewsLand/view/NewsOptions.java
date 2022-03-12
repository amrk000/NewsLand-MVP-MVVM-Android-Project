package amrk000.NewsLand.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import amrk000.NewsLand.R;
import amrk000.NewsLand.adapter.NewsRecyclerViewAdapter;
import amrk000.NewsLand.databinding.FragmentNewsOptionsBinding;
import amrk000.NewsLand.model.NewsItem;

public class NewsOptions extends BottomSheetDialogFragment {
    private FragmentNewsOptionsBinding binding;
    private NewsRecyclerViewAdapter adapter;
    private NewsItem newsPost;

    public NewsOptions(NewsRecyclerViewAdapter adapter, NewsItem newsPost) {
        this.adapter = adapter;
        this.newsPost = newsPost;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsOptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Glide.with(getContext())
                .load(newsPost.getImage())
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(binding.optionsPostImage);

        binding.optionsPostTitle.setText(newsPost.getTitle());
        binding.optionsSourceName.setText(newsPost.getSource().getName());

        binding.optionsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, newsPost.getArticleUrl());
                startActivity(Intent.createChooser(share, "Share Link"));

                dismiss();
            }
        });

        binding.optionsBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri url = Uri.parse(newsPost.getArticleUrl());
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW,url));

                dismiss();
            }
        });

        binding.optionsRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removePost(newsPost);
                dismiss();
            }
        });

    }
}