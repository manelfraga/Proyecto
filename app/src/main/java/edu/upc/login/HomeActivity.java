package edu.upc.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;

import edu.upc.login.Entidades.Item;
import edu.upc.login.Fragments.FragmentForo;
import edu.upc.login.Fragments.FragmentItemDetalle;
import edu.upc.login.Fragments.FragmentCamara;
import edu.upc.login.Fragments.FragmentInventario;
import edu.upc.login.Fragments.FragmentPerfil;
import edu.upc.login.Fragments.FragmentEstadisticas;
import edu.upc.login.Fragments.FragmentItems;
import edu.upc.login.Fragments.FragmentHome;
import edu.upc.login.Fragments.FragmentSignOut;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, iComunicaFragments {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView monedas;



    //variables para cargar fragment principal

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //variables del fragmentdetalle item
    FragmentItemDetalle detalleItemFragment;

    private int obtenerMonedas(){
        SharedPreferences preferences = getSharedPreferences("tokenUsuario", Context.MODE_PRIVATE);
        int monedas = preferences.getInt("monedas", 0);
        return monedas;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=findViewById(R.id.toolbar);
        monedas=findViewById(R.id.idmonedas);
        monedas.setText(String.valueOf(obtenerMonedas()));
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView);

        //establecer evento onclick al navigation view

        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open , R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //cargar fragment principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new FragmentHome());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.home){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new FragmentHome());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();

        }
        if(menuItem.getItemId() == R.id.inventario){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new FragmentInventario());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
        }

        if(menuItem.getItemId() == R.id.items){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new FragmentItems());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
        }
        if(menuItem.getItemId() == R.id.statistics){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new FragmentEstadisticas());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
        }
        if(menuItem.getItemId() == R.id.perfil){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new FragmentPerfil());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
        }
        if(menuItem.getItemId() == R.id.camara){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new FragmentCamara());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
        }

        if(menuItem.getItemId() == R.id.foro){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new FragmentForo());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
        }

        if(menuItem.getItemId() == R.id.signout) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new FragmentSignOut());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
        }

        return false;
    }

    @Override
    public void enviarObjeto(Item item) {
        // Aquí se realiza toda la logica necesaria para poder realizar el envío
        detalleItemFragment = new FragmentItemDetalle();
        //objeto de tipo bundle para transportar la información
        Bundle bundleEnvio = new Bundle();
        //enviar el objeto que está llegando con Serializable
        bundleEnvio.putSerializable("objeto", item);
        detalleItemFragment.setArguments(bundleEnvio);
        //abrir fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, detalleItemFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*public void enviarUser(User user) {
        // Aquí se realiza toda la logica necesaria para poder realizar el envío
        detalleItemFragment = new DetalleItemFragment();
        //objeto de tipo bundle para transportar la información
        Bundle bundleEnvio = new Bundle();
        //enviar el objeto que está llegando con Serializable
        bundleEnvio.putSerializable("user", user);
        detalleItemFragment.setArguments(bundleEnvio);
        //abrir fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, detalleItemFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }*/
}
