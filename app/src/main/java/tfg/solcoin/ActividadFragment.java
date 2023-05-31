package tfg.solcoin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActividadFragment extends Fragment {

    private Spinner spTrimestre,spTipoActividad ;
    private Button botonEntrega;
    private EditText numSesiones, numAlumnos;

    public ActividadFragment() {
        // Constructor público vacío requerido
    }

    public static ActividadFragment newInstance() {
        ActividadFragment fragment = new ActividadFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño para este fragmento
        View view = inflater.inflate(R.layout.fragment_actividad, container, false);
        spTrimestre = view.findViewById(R.id.spinnerTrimestre);
        spTipoActividad = view.findViewById(R.id.spinnerTipoActividad);

        ArrayList<String> opcionesTrimestre = new ArrayList<String>();
        opcionesTrimestre.add("Primer trimestre");
        opcionesTrimestre.add("Segundo trimestre");
        opcionesTrimestre.add("Tercer trimestre");

        ArrayAdapter<String> adapterTrimestre = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, opcionesTrimestre);
        adapterTrimestre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTrimestre.setAdapter(adapterTrimestre);

        ArrayList<String> opcionesTipoActividad = new ArrayList<String>();
        opcionesTipoActividad.add("Correctivo");
        opcionesTipoActividad.add("Obligatorio");
        opcionesTipoActividad.add("Preventivo");

        ArrayAdapter<String> adapterTipoActividad = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, opcionesTipoActividad);
        adapterTipoActividad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoActividad.setAdapter(adapterTipoActividad);

        numSesiones = view.findViewById(R.id.editTextNumSesiones);
        numAlumnos = view.findViewById(R.id.editTextNumAlumnos);

        botonEntrega = view.findViewById(R.id.button);
        botonEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trimestre = spTrimestre.getSelectedItem().toString();
                String tipoAct = spTipoActividad.getSelectedItem().toString();
                String numAl = numAlumnos.getText().toString();
                String numSes = numSesiones.getText().toString();

                if(trimestre.equals("") || tipoAct.equals("") || numAl.equals("") || numSes.equals("")){
                    Toast.makeText(getActivity(), "Debe rellenar todos los campos del formulario", Toast.LENGTH_SHORT);
                }else{
                    entregarActividad(getString(R.string.url)+"insertarActividad.php");
                }
            }
        });

        return view;
    }

    public void entregarActividad(String URL){
        SharedPreferences preferencias = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String correo = preferencias.getString("correo", "");

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "Entrega realizada. A la espera de confirmación", Toast.LENGTH_SHORT).show();
                //goToLogin();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("correo",correo);
                parametros.put("trimestre",verTrimestre());
                parametros.put("sesionesEmpleadas",numSesiones.getText().toString());
                parametros.put("numAlumnos",numAlumnos.getText().toString());
                parametros.put("tipo",spTipoActividad.getSelectedItem().toString());
                parametros.put("remuneracion",calcularRemuneración());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public String verTrimestre(){
        String trimestreElegido = spTrimestre.getSelectedItem().toString();
        switch (trimestreElegido){
            case "Primer trimestre":
                return "Primero";
            case "Segundo trimestre":
                return "Segundo";
            case "Tercer trimestre":
                return "Tercero";
            default:
                return null;
        }
    }

    public String calcularRemuneración(){
        int remuneracion=0;

        String trimestreElegido = spTrimestre.getSelectedItem().toString();
        switch (trimestreElegido){
            case "Primer trimestre":
                remuneracion+=1;
            case "Segundo trimestre":
                remuneracion+=3;
            case "Tercer trimestre":
                remuneracion+=5;
            default:
                remuneracion+=0;
        }

        String tipo = spTipoActividad.getSelectedItem().toString();
        switch (tipo){
            case "Preventivo":
                remuneracion+=1;
            case "Obligatorio":
                remuneracion+=3;
            case "Correctivo":
                remuneracion+=5;
            default:
                remuneracion+=0;
        }


        int alumnos = Integer.parseInt(numAlumnos.getText().toString());
        switch (alumnos){
            case 1:
                remuneracion+=5;
            case 2:
                remuneracion+=3;
            case 3:
                remuneracion+=1;
            default:
                remuneracion+=0;
        }

        int sesiones = Integer.parseInt(numSesiones.getText().toString());
        switch (sesiones){
            case 1:
                remuneracion+=5;
            case 2:
                remuneracion+=3;
            case 3:
                remuneracion+=1;
            default:
                remuneracion+=0;
        }
        //En caso de que las sesiones sean mayor que 3 la remuneración sera 0
        if (Integer.parseInt(numSesiones.getText().toString()) > 3) {
            remuneracion = 0;
        }



        return Integer.toString(remuneracion);

}

}