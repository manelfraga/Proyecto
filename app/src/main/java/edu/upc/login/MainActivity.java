package edu.upc.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import edu.upc.login.Entidades.LoginCredentials;
import edu.upc.login.Entidades.Token;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends Activity {

    private API api;

    private void guardarToken(String token, int monedas){
        //Creamos objeto preferences que se guardara en un XML llamado tokenUsuario y es privado porque
        //solo podremos acceder mediante nuestra app. Creamos un editor y guardamos el valor que le pasamos
        //como parametro con la llave "token"

        //Para ver sharedPreferences: device file explorer->data->data->edu.upc.login->shared_prefs

        SharedPreferences preferences = getSharedPreferences("tokenUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.putInt("monedas", monedas);
        editor.commit();
    }

    //Funcion que lee SharedPreferences para obtener el token
    /*private String obtenerToken(){
        SharedPreferences preferences = getSharedPreferences("tokenUsuario", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "Login required");
        return token;
    }*/

    /*private int obtenerMonedas(){
        SharedPreferences preferences = getSharedPreferences("tokenUsuario", Context.MODE_PRIVATE);
        int monedas = preferences.getInt("monedas", 0);
        return monedas;
    }*/

    public boolean isToken() {
        SharedPreferences pref = getSharedPreferences("tokenUsuario", Context.MODE_PRIVATE);
        if(pref.contains("token")) {
            return true;
        }
        else return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(isToken()){
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            } else {
            setTheme(R.style.AppTheme);
            setContentView(R.layout.activity_main);

            //Creamos interceptor
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            //Creamos cliente
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            //Crear retrofit
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://147.83.7.203:8080/dsaApp/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            //Llamamos a servicios que hemos definido en la API
            api = retrofit.create(API.class);

            //Inicializamos el LayOut
            final TextView nombre = findViewById((R.id.nombreText));
            final TextView password = findViewById((R.id.passwordText));
            Button registrarseButton = findViewById(R.id.registrarseButton);
            registrarseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(i);
                }
            });


            Button loginButton = findViewById(R.id.loginButton);
            //Cuando clickamos en loginButton -> Creamos llamamos a los strings nombre y password, los metemos en variable login
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /********PROGRESS BAR******/
                    ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.login_container);
                    ProgressBar progressBar = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                    // Crea layout parameters para el ProgressBar
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    progressBar.setLayoutParams(lp);
                    //Define como indeterminado.
                    progressBar.setIndeterminate(true);
                    // Agrega el ProgressBar al Layout
                    cl.addView(progressBar);
                    /***************************/

                    LoginCredentials loginc = new LoginCredentials();
                    loginc.setNombre((String) nombre.getText().toString());
                    loginc.setPassword((String) password.getText().toString());

                    Call<Token> call = api.login(loginc);

                    call.enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            if (response.isSuccessful()) {
                                Token token = response.body();
                                guardarToken(token.getToken(), token.getMonedas());
                                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error " + response.code() + ": " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Error al acceder a la API", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }

            });

        }

    }
}