package tfg.solcoin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioProfesoresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioProfesoresFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView textoClases, textoCorreo, textoNombre;
    private Button btnCerrar;
    private SharedPreferences preferencias;

    public InicioProfesoresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioProfesoresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioProfesoresFragment newInstance(String param1, String param2) {
        InicioProfesoresFragment fragment = new InicioProfesoresFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inicio_profesores, container, false);

        textoClases = rootView.findViewById(R.id.textClases);
        textoNombre = rootView.findViewById(R.id.textNombre);
        textoCorreo = rootView.findViewById(R.id.textCorreo);
        preferencias = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String correo = preferencias.getString("correo", "");
        textoCorreo.setText(correo);
        verDatos(correo);

        btnCerrar = rootView.findViewById(R.id.btnEnviar);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("correo", "");
                Intent i = new Intent(getActivity(),LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void verDatos(String correo){
        String url = getString(R.string.url)+"verDatosUsuario.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject userObj = jsonArray.getJSONObject(i);
                                String clases = userObj.getString("curClase");
                                String nombre = userObj.getString("nombre");

                                textoClases.setText(clases);
                                textoNombre.setText(nombre);
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
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("correo", correo);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

}