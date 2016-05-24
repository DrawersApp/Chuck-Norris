package com.chuck.models.rest;

import com.chuck.models.entities.Joke;
import com.chuck.models.repository.JokeRepository;
import com.chuck.models.utils.JokeResultDeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

import java.util.List;

/**
 * Created by nishant.pathak on 15/05/16.
 */

public class RestDataSource implements JokeRepository {
    private static final String END_POINT = "http://api.icndb.com";

    private final JokesApi mJokesApi;

    public RestDataSource() {

        Gson customGsonInstance = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<Joke>>() {}.getType(), new JokeResultDeSerializer<>())
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient();

        /*

        try {
            gsonBuilder.registerTypeAdapter(Class.forName("io.realm.UserRealmProxy"), new JokeResultDeSerializer<Joke>());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
*/

        Retrofit jokeApiAdapter = new Retrofit.Builder()
                .baseUrl(END_POINT)
                .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        mJokesApi = jokeApiAdapter.create(JokesApi.class);
    }

    @Override
    public Observable<Joke> getJoke(int jokeId) {
        return mJokesApi.getJoke(jokeId);
    }

    @Override
    public Observable<List<Joke>> getRandomJoke() {
        return mJokesApi.getRandomJoke(1);
    }
}
