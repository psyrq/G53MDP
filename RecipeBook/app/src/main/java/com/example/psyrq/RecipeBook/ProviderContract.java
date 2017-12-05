package com.example.psyrq.RecipeBook;

import android.net.Uri;

/**
 * Created by qianruofei on 2017/12/5.
 */

public class ProviderContract {

    //save uri info
    final static String AUTHORITY = "com.example.psyrq.myprovider";
    final static Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

//    public static final String RECIPE_ID = "recipeID";
//    public static final String TITLE = "title";
//    public static final String CONTENTS = "contents";

    //different uri for four kinds of actions
    public interface URI {
        Uri ID_INSERT = Uri.parse("content://" + AUTHORITY + "/recipe/insert");
        Uri ID_QUERY = Uri.parse("content://" + AUTHORITY + "/recipe/query");
        Uri ID_UPDATE = Uri.parse("content://" + AUTHORITY + "/recipe/update");
        Uri ID_DELETE = Uri.parse("content://" + AUTHORITY + "/recipe/delete");
    }
}
