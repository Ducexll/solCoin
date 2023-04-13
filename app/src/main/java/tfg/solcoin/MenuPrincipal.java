package tfg.solcoin;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.fragment.app.Fragment;



public class MenuPrincipal extends AppCompatActivity {

    //Declaramos los 3 fragmentos, es decir las 3 vistas
    FirstFragment firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment thirdFragment = new ThirdFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

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
    }
        //LLega el nuevo fragmento y se reemplaza
        public void loadFragment(Fragment fragment){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.commit();
        }

}