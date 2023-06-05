package tfg.solcoin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Typeface;


public class VerPremiosCanjeadosFragment extends Fragment {

    private String URL_PREMIOS;
    private SharedPreferences preferencias;
    private String correo;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private List<Premio> listaPremios;
    private List<Boolean> listaSeleccionados;

    public VerPremiosCanjeadosFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static VerPremiosCanjeadosFragment newInstance(String param1, String param2) {
        VerPremiosCanjeadosFragment fragment = new VerPremiosCanjeadosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        listaPremios = new ArrayList<>();
        listaSeleccionados = new ArrayList<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ver_premios_canjeados, container, false);
        LinearLayout linearLayoutPremios = rootView.findViewById(R.id.linearPremiosCanjeados);

        preferencias = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
         correo = preferencias.getString("correo", "");

        URL_PREMIOS = getString(R.string.url) + "selectCanjea.php";

        // Obtener los premios desde el web-service
        obtenerPremiosDesdeWebService(linearLayoutPremios, correo);

        return rootView;
    }

    private void obtenerPremiosDesdeWebService(LinearLayout linearLayoutPremios, String correo) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_PREMIOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject canjeaObj = jsonArray.getJSONObject(i);
                                int idCompra = canjeaObj.getInt("idCompra");
                                String correoUsuario = canjeaObj.getString("Usuarios_correo");

                                String fecha = canjeaObj.getString("fecha");
                                String NombrePremio = canjeaObj.getString("nombre");



                                // Se crea un texview por cada usuario
                                TextView textView = new TextView(getActivity());
                                textView.setText("ID Compra: " + idCompra + "\n" +
                                                "Nombre Premio: " + NombrePremio + "\n" +
                                                "Correo Alumno: " + correoUsuario + "\n" +
                                                "Fecha: " + fecha + "\n" );

                                // Agrega el TextView al LinearLayout
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        linearLayoutPremios.addView(textView);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> parametros = new HashMap<String,String>();
            parametros.put("correo",correo);
            return parametros;
        }};

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

}