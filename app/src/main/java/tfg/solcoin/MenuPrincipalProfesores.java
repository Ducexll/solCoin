package tfg.solcoin;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;



public class MenuPrincipalProfesores extends AppCompatActivity {

    //Declaramos los 3 fragmentos, es decir las 3 vistas
    ConfirmarActividadesFragment firstFragment = new ConfirmarActividadesFragment();
    VerPremiosCanjeadosFragment secondFragment = new VerPremiosCanjeadosFragment();
    AnadirPremioFragment thirdFragment = new AnadirPremioFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_profesores);

        //Referenciamos el bottom navigation y le ponemos y listener
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            //Dependiendo del fragmento seleccionado mostramos uno u otro
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.firstFrangment:
                        loadFragment(firstFragment);
                        return true;
                    case R.id.secondFrangment:
                        loadFragment(secondFragment);
                        return true;
                    case R.id.thirdFrangment:
                        loadFragment(thirdFragment);
                        return true;
                }

                return false;
            }
        });
        loadFragment(firstFragment);
    }
    //LLega el nuevo fragmento y se reemplaza
    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
}

}