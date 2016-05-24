
package com.chuck.models.rest;

import com.chuck.models.entities.Categories;
import com.chuck.models.entities.Joke;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

import java.util.List;

/**
 * Created by nishant.pathak on 15/05/16.
 * http://www.icndb.com/api/
 */

public interface JokesApi {
    @GET("/jokes/{id}")
    Observable<Joke> getJoke (@Path("id") int id);

    @GET("/jokes/random/{count}")
    Observable<List<Joke>> getRandomJoke (@Path("count") int count);

    @GET("/categories")
    Observable<Categories> getCategories();
}
