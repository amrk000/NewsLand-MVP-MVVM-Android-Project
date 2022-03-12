package amrk000.NewsLand.di.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

//Custom Scope Sample (Not used)
@Scope
@Documented
@Retention(RUNTIME) //Runtime: works like Singleton
public @interface MyCustomScope {

}
