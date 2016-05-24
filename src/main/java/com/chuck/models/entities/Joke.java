package com.chuck.models.entities;

import java.util.Arrays;

/**
 * Created by nishant.pathak on 15/05/16.
 */
@SuppressWarnings("unused")
public class Joke {
    Integer id;
    String joke;
    String [] categories;

    @Override
    public String toString() {
        return "Joke{" +
                "id=" + id +
                ", joke='" + joke + '\'' +
                ", categories=" + Arrays.toString(categories) +
                '}';
    }
}
