package com.chuck.models.rest;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by nishant.pathak on 22/05/16.
 */
public class RestDataSourceTest {


    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("setup");
    }

    @AfterMethod
    public void tearDown() throws Exception {
        System.out.println("teardown");

    }

    @Test
    public void testGetJoke() throws Exception {

    }

    @Test
    public void testGetRandomJoke() throws Exception {

    }
}