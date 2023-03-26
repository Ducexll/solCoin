package tfg.solcoin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuCreacionUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_creacion_usuario);
    }

    public void CrearUsuario (View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}