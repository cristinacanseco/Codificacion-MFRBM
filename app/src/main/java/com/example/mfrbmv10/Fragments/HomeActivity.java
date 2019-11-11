package com.example.mfrbmv10.Fragments;

import android.net.Uri;
import android.os.Bundle;

import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.FirebaseMotor.SesionesFirestore;
import com.example.mfrbmv10.Fragments.Bitacoras.BitacoraEditarFragment;
import com.example.mfrbmv10.Fragments.Bitacoras.BitacoraFragment;
import com.example.mfrbmv10.Fragments.Bitacoras.BitacoraMostrarFragment;
import com.example.mfrbmv10.Fragments.Bitacoras.BitacoraNuevaFragment;
import com.example.mfrbmv10.Fragments.Muestreos.MuestreoEditarFragment;
import com.example.mfrbmv10.Fragments.Muestreos.MuestreoFragment;
import com.example.mfrbmv10.Fragments.Muestreos.MuestreoMostrarFragment;
import com.example.mfrbmv10.Fragments.Muestreos.MuestreoNuevoFragment;
import com.example.mfrbmv10.Fragments.Usuario.UsuarioPerfilFragment;
import com.example.mfrbmv10.R;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    BitacoraFragment.OnFragmentInteractionListener,
                    BitacoraNuevaFragment.OnFragmentInteractionListener,
                    BitacoraEditarFragment.OnFragmentInteractionListener,
                    BitacoraMostrarFragment.OnFragmentInteractionListener,
                    MuestreoFragment.OnFragmentInteractionListener,
                    MuestreoNuevoFragment.OnFragmentInteractionListener,
                    MuestreoEditarFragment.OnFragmentInteractionListener,
                    MuestreoMostrarFragment.OnFragmentInteractionListener,
                    UsuarioPerfilFragment.OnFragmentInteractionListener{

   private SesionesFirestore sesionesFirestore;
   Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.sesionesFirestore = new SesionesFirestore(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        actualizarNavHeader();

        fragment = new BitacoraFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_main, fragment).commit();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        MenuItem item = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarBitacoras(query,true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarBitacoras(newText,false);
                return false;
            }
        });
        return true;
    }

    private void buscarBitacoras(String query, boolean b) {
        Crud crud = new Crud(fragment);
        if(b) {
            crud.buscar(query.trim());
        }else{
            crud.mostrarBitacoras();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        boolean fragmentSeleccionado=false;

        if (id == R.id.nav_bitacora) {
            fragment = new BitacoraFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_perfil) {
            fragment = new UsuarioPerfilFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_Salir) {
            Toast.makeText(this, "¡ Nos vemos luego ;) !", Toast.LENGTH_SHORT).show();
            sesionesFirestore.cerrarSesion();
        }

        if(fragmentSeleccionado)
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        onBackPressed();
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) { }

    public void actualizarNavHeader(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView tv_nombreUsuario = headerView.findViewById(R.id.tv_nombreUsuario);
        TextView tv_correoUsuario = headerView.findViewById(R.id.tv_correoUsuario);

        //Toast.makeText(this, ""+sesionesFirestore.getmUser().getDisplayName(), Toast.LENGTH_SHORT).show();
        tv_nombreUsuario.setText(sesionesFirestore.getmUser().getDisplayName());
        tv_correoUsuario.setText(sesionesFirestore.getmUser().getEmail());

    }
}
