package amrk000.NewsLand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import amrk000.NewsLand.R;
import amrk000.NewsLand.model.SettingItem;

public class SettingsListViewAdapter extends ArrayAdapter<SettingItem> {
    private Context context;
    private ArrayList<SettingItem> settings;

    public SettingsListViewAdapter(@NonNull Context context, ArrayList<SettingItem> settings) {
        super(context, R.layout.list_view_item,settings);

        this.context = context;
        this.settings = settings;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.list_view_item, null,true);

        TextView name = convertView.findViewById(R.id.SettingName);
        TextView value = convertView.findViewById(R.id.SettingValue);

        name.setText(settings.get(position).getName());
        value.setText(settings.get(position).getValue());

        return convertView;
    }

    public void updateList(ArrayList<SettingItem> settings){
        this.settings = settings;
        notifyDataSetChanged();
    }

}
