/*
 * Copyright (c) 2016 Jason <jasonlinkinbright@gmail.com>
 * All Rights Reserved
 */

package com.jason.trip.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {

    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .generateNonExecutableJson()
            // TODO
            //.registerTypeAdapter()
            .create();

    private GsonHelper() {}

    public static Gson get() {
        return GSON;
    }
}
