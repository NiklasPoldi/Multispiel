package de.paulklubert.ms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.paulklubert.ms.data.login.LoginHandler;

public class LoginActivity extends AppCompatActivity {

    final LoginActivity root = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Fullscreen Mode
        //hideNavigationBar();

        final EditText user = (EditText) findViewById(R.id.benutzer);
        final EditText password = (EditText) findViewById(R.id.passwort);

        final Button button = findViewById(R.id.login_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String benutzer = user.getText().toString();
                final String passwort = password.getText().toString();
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if(LoginHandler.loginUser(benutzer, passwort) == true) {
                                        root.runOnUiThread(new Runnable() {
                                            public void run() {
                                                loginErfolgreich();
                                            }
                                        });
                                    }
                                } catch (Exception e)  {

                                    final Exception ex = e;

                                    root.runOnUiThread(new Runnable() {
                                        public void run() {

                                            new AlertDialog.Builder(root)
                                                .setTitle("Anmeldung fehlgeschlagen")
                                                .setMessage(ex.getMessage())
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Continue with delete operation
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        }
                                    });

                                }// Ignoriere andere Fehler
                            }
                        }
                ).start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Fullscreen Mode
        //hideNavigationBar();
    }

    private void loginErfolgreich() {

        Intent startNewActivity = new Intent(this, MainActivity.class);
        startActivity(startNewActivity);
    }

    private void hideNavigationBar() {

        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
}
