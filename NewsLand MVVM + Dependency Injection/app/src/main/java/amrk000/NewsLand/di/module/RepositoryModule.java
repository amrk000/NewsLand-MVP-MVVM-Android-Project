package amrk000.NewsLand.di.module;

import dagger.Module;

@Module(includes = {RoomDbModule.class, RetrofitModule.class, XmlDataModule.class})
public class RepositoryModule {
}
