package com.chuck;

import com.chuck.models.entities.Joke;
import com.chuck.models.rest.RestDataSource;
import rx.Observable;

import java.util.List;

/**
 * Created by nishant.pathak on 22/05/16.
 */
public class RetrofitTest {
    public static void main(String [] args) {
        RestDataSource restDataSource = new RestDataSource();
        Observable<List<Joke>> joke = restDataSource.getRandomJoke();

        joke.subscribe(jokes -> {
            System.out.println(jokes.toString());
        });
    }
}
