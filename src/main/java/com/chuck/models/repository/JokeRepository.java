package com.chuck.models.repository;

import com.chuck.models.entities.Joke;
import rx.Observable;

import java.util.List;

public interface JokeRepository {
    Observable<Joke> getJoke(final int jokeId);
    Observable<List<Joke>> getRandomJoke();

}
