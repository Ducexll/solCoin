package tfg.solcoin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void iniciarSesion (View view) {
        Intent i = new Intent(this, MenuPrincipal.class);
        startActivity(i);
    }

    public void irCrearUsuario (View view) {
        Intent i = new Intent(this, MenuCreacionUsuario.class);
        startActivity(i);
    }
}