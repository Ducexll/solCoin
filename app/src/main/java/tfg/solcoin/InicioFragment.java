package tfg.solcoin;

import static android.app.ProgressDialog.show;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InicioFragment extends Fragment {

    private double saldo;
    private TextView textViewSaldo;
    private SharedPreferences preferencias;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iniciofragment, container, false);
        textViewSaldo = rootView.findViewById(R.id.textViewSaldo);

        // Obtener la instancia de SharedPreferences
        //preferencias = getActivity().getSharedPreferences("nombre_preferencias", Context.MODE_PRIVATE);
        preferencias = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        actualizarSaldo();
    }

    private void actualizarSaldo() {
        // Obtener el saldo desde tu web-service o de donde sea necesario
        //SharedPreferences.Editor editor = preferencias.edit();
        String correo = preferencias.getString("correo", "");
        Toast.makeText(getActivity(), "Usando: " + correo, Toast.LENGTH_SHORT).show();

        obtenerSaldoDesdeWebService(correo);
    }

    private void obtenerSaldoDesdeWebService(String correo) {
        // Realiza la solicitud HTTP al web-service consultarSaldo
        String urlConsultaSaldo = R.string.url+"consultarSaldo.php";

        StringRequest solicitud = new StringRequest(Request.Method.POST, urlConsultaSaldo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String respuesta) {
                        try {
                            JSONObject jsonObjectResultado = new JSONObject(respuesta);
                            if (jsonObjectResultado.has("saldo")) {
                                double saldo = jsonObjectResultado.getDouble("saldo");
                                Toast.makeText(getActivity(), "saldo: " + saldo, Toast.LENGTH_SHORT).show();
                                InicioFragment.this.saldo = saldo;
                                textViewSaldo.setText(String.valueOf(saldo));
                            } else if (jsonObjectResultado.has("mensaje")) {
                                String mensaje = jsonObjectResultado.getString("mensaje");
                                Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Error al comunicarse con el servidor", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Agregar el correo como parámetro en el cuerpo de la solicitud
                Map<String, String> params = new HashMap<>();
                params.put("correo", correo);
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        RequestQueue cola = Volley.newRequestQueue(getActivity());
        cola.add(solicitud);
    }


}

