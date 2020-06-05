package edu.upc.login.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.upc.login.API;
import edu.upc.login.Adaptadores.AdapterRanking;
import edu.upc.login.Entidades.Ranking;
import edu.upc.login.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentEstadisticas extends Fragment {

    AdapterRanking adapterRanking;
    RecyclerView recyclerViewRanking;
    private API api;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.estadisticas_fragment, container, false);
        recyclerViewRanking = view.findViewById(R.id.recyclerId);

        //cargar la lista
        cargarLista();






        return view;

    }

    public void cargarLista() {
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
        Call<List<Ranking>> call = api.getRanking();

        call.enqueue(new Callback<List<Ranking>>() {
            @Override
            public void onResponse(Call<List<Ranking>> call, Response<List<Ranking>> response) {
                if(response.isSuccessful()) {
                    List<Ranking> rankingRespuesta = response.body();
                    //listaRanking.addAll(rankingRespuesta);
                   mostrarDatos(rankingRespuesta);

                }

                else {
                    Log.e("DSA","Error :"+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Ranking>> call, Throwable t) {
                Log.e("DSA","Error: No se pudo acceder a la API",t);

            }
        });


    }
    public void mostrarDatos(List<Ranking> listaRanking) {

        recyclerViewRanking.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRanking = new AdapterRanking(getContext(), listaRanking);
        recyclerViewRanking.setAdapter(adapterRanking);
    }



    }








