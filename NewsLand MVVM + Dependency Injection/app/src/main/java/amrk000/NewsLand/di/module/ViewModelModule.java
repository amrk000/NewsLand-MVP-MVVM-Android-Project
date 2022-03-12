package amrk000.NewsLand.di.module;

import androidx.lifecycle.ViewModel;

import amrk000.NewsLand.di.annotation.ViewModelKey;
import amrk000.NewsLand.viewmodel.NewsViewModel;
import amrk000.NewsLand.viewmodel.SettingsViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel.class)
    abstract ViewModel bindNewsViewModel(NewsViewModel newsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel.class)
    abstract ViewModel bindSettingsViewModel(SettingsViewModel newsViewModel);

}