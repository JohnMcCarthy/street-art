package com.mccarthy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.cloudmine.api.CMSessionToken;
import com.mccarthy.R;
import roboguice.inject.ContentView;

import java.util.Arrays;
import java.util.List;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
@ContentView(R.layout.find_art)
public class FindArtActivity extends BaseLocationActivity {
    public static Intent newIntent(Context callingContext, CMSessionToken sessionToken) {
        Intent intent = new Intent(callingContext, FindArtActivity.class);
        addSessionTokenToIntent(intent, sessionToken);
        return intent;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected List<Integer> getMenuIds() {
        return Arrays.asList(R.menu.add_photos, R.menu.logout);
    }

    public void onAddPhotos(MenuItem item) {
        startActivity(AddPhotosActivity.newIntent(this, sessionToken));
    }
}
