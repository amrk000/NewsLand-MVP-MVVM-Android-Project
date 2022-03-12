package amrk000.NewsLand.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import amrk000.NewsLand.data.local.DataBase;
import amrk000.NewsLand.data.local.RoomDao;
import amrk000.NewsLand.view.ArticleBrowser;
import amrk000.NewsLand.view.MainActivity;
import amrk000.NewsLand.model.NewsItem;
import amrk000.NewsLand.view.NewsOptions;
import amrk000.NewsLand.R;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private ArrayList<NewsItem> news;
    private RoomDao roomDao;

    private LastItemReachedListener lastItemReachedListener;

    private final int TYPE_NEWS_HOLDER = 0;
    private final int TYPE_LOADING_HOLDER = 1;
    private final int TYPE_ALLDONE_HOLDER = 2;

    private final NewsItem loadingObject = new NewsItem(); //dummy NewsItem object for loading placeholder
    private final NewsItem allDoneObject = new NewsItem(); //dummy NewsItem object for all done placeholder

    public NewsRecyclerViewAdapter(Context context){
        this.context=context;
        news = new ArrayList<>();
        roomDao = DataBase.get(context).daoAccess();
    }

    //item view inner class
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView source, author, title,date;
        ImageView image;
        ImageButton options;

        public NewsViewHolder(View view) {
            super(view);

            source = view.findViewById(R.id.sourceName);
            author = view.findViewById(R.id.author);
            date = view.findViewById(R.id.date);
            title = view.findViewById(R.id.title);
            image = view.findViewById(R.id.newsImage);
            options = view.findViewById(R.id.options);
        }

    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View view) {
            super(view);

        }
    }

    public static class AllDoneViewHolder extends RecyclerView.ViewHolder {

        public AllDoneViewHolder(View view) {
            super(view);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_LOADING_HOLDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        else if (viewType == TYPE_ALLDONE_HOLDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_alldone_item, parent, false);
            return new AllDoneViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_news_item, parent, false);
            return new NewsViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(news.get(position)==loadingObject) return TYPE_LOADING_HOLDER;
        else if(news.get(position)==allDoneObject) return TYPE_ALLDONE_HOLDER;
        else return TYPE_NEWS_HOLDER;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int p) {

        int position = holder.getAdapterPosition();
        if(getItemViewType(position) == TYPE_NEWS_HOLDER){

            NewsViewHolder newsViewHolder = (NewsViewHolder) holder;

            newsViewHolder.title.setText(news.get(position).getTitle());
            newsViewHolder.source.setText(news.get(position).getSource().getName());

            if(news.get(position).getAuthor()!=null &&news.get(position).getAuthor().contains("https://")) newsViewHolder.author.setText(news.get(position).getSource().getName());
            else newsViewHolder.author.setText(news.get(position).getAuthor());

            String separator = newsViewHolder.author.getText().toString().isEmpty()? "" : " | ";
            newsViewHolder.date.setText(separator + dateTimeConvert(news.get(position).getDate(),"yyyy-MM-dd'T'HH:mm:ssX",TimeZone.getTimeZone("UTC"),"dd-MM-yyyy hh:mm a",TimeZone.getDefault() ));

            Glide.with(context)
                    .load(news.get(position).getImage())
                    .error(R.drawable.placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade(350))
                    .centerCrop()
                    .into(newsViewHolder.image);

            newsViewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ArticleBrowser.class)
                            .putExtra("url",news.get(position).getArticleUrl())
                            .putExtra("title",news.get(position).getTitle())
                    );
                }
            });

            final NewsRecyclerViewAdapter adapter = this;
            newsViewHolder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsOptions newsOptions = new NewsOptions(adapter,news.get(position));
                    newsOptions.show(((MainActivity)context).getSupportFragmentManager(),newsOptions.getTag());

                }
            });

        }

    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.post));

        if (holder.getItemViewType() == TYPE_NEWS_HOLDER && holder.getAdapterPosition() == getItemCount()-1 && lastItemReachedListener!=null)
            lastItemReachedListener.onLoadMore();

    }

    public interface LastItemReachedListener {
        void onLoadMore();
    }

    public void setLastItemReachedListener(LastItemReachedListener lastItemReachedListener) {
        this.lastItemReachedListener = lastItemReachedListener;
    }

    public void setLoading(boolean show){

        if(show){
            news.add(loadingObject);
            notifyItemInserted(getItemCount());
        }
        else{
            news.remove(loadingObject);
            notifyItemChanged(getItemCount());
        }

    }

    public void setAllDone(boolean show){

        if(show){
            news.add(allDoneObject);
            notifyItemInserted(getItemCount());

        }
        else{
            news.remove(allDoneObject);
            notifyItemRemoved(getItemCount());
        }
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void clearItems(){
        news.clear();
        notifyDataSetChanged();
    }

    public void setData(ArrayList<NewsItem> news){
        this.news = news;
    }

    public void removePost(NewsItem newsPost){
        roomDao.addRecord(newsPost);
        int position = news.indexOf(newsPost);
        news.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position);
    }

    public String dateTimeConvert(String dateTime,String inputPattern,TimeZone inputZone,String outputPattern,TimeZone outputZone){
        try {
            SimpleDateFormat input = new SimpleDateFormat(inputPattern);
            input.setTimeZone(inputZone);

            Date parsed = input.parse(dateTime);

            SimpleDateFormat destFormat = new SimpleDateFormat(outputPattern);
            destFormat.setTimeZone(outputZone);

            return destFormat.format(parsed);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "dateTime Error";
    }


}
