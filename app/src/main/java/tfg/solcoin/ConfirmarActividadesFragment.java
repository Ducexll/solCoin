package tfg.solcoin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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
 * Use the {@link ConfirmarActividadesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmarActividadesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TableLayout tabla;

    private String correo;

    public ConfirmarActividadesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmarActividadesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmarActividadesFragment newInstance(String param1, String param2) {
        ConfirmarActividadesFragment fragment = new ConfirmarActividadesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_confirmar_actividades, container, false);

        SharedPreferences preferencias = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        correo = preferencias.getString("correo", "");

        tabla=rootView.findViewById(R.id.tablaActEntregadas);

        String urlGenTabla = getString(R.string.url)+"selecActividades.php";
        generarTabla(urlGenTabla);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void generarTabla(String url){
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject actividadObj = jsonArray.getJSONObject(i);
                                String correoAlumno = actividadObj.getString("Usuarios_correo");
                                String fecha = actividadObj.getString("fecha");
                                //double precio = actividadObj.getDouble("precio");
                                /*
                                Premio premio = new Premio(correoAlumno, fecha, precio);
                                listaPremios.add(premio);
                                listaSeleccionados.add(false);

                                 */
                                TableRow row = new TableRow(getActivity());

                                TextView fechaTextView = new TextView(getActivity());
                                fechaTextView.setText(fecha);
                                row.addView(fechaTextView);

                                TextView correoTextView = new TextView(getActivity());
                                correoTextView.setText(correoAlumno);
                                row.addView(correoTextView);

                                Button verButton = new Button(getActivity());
                                verButton.setText("Detalles");
                                verButton.setVisibility(View.VISIBLE);

                                verButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });


                                row.addView(verButton);

                                tabla.addView(row);
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