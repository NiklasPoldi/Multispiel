package com.poldi.multispiel.data.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.poldi.multispiel.R;
import com.poldi.multispiel.data.ui.main.ui.gamefragmentmodel.GameFragmentModelFragment;

public class GameFragmentModel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_fragment_model_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, GameFragmentModelFragment.newInstance())
                    .commitNow();
        }
    }
}
