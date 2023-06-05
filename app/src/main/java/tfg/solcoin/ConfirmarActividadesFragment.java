package tfg.solcoin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

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
                            tabla.removeAllViews(); //Borramos la tabla generada anteriormente
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject actividadObj = jsonArray.getJSONObject(i);
                                String correoAlumno = actividadObj.getString("Usuarios_correo");
                                String fecha = actividadObj.getString("fecha");
                                String trimestre = actividadObj.getString("Trimestre");
                                String sesEmp = actividadObj.getString("SesionesEmpleadas");
                                String numAl = actividadObj.getString("numeroAlumnos");
                                String tipo = actividadObj.getString("Tipo");
                                String remuneracion = actividadObj.getString("remuneracion");
                                String id = actividadObj.getString("idActividad");

                                TableRow row = new TableRow(getActivity());

                                TextView fechaTextView = new TextView(getActivity());
                                fechaTextView.setText(fecha);
                                TableRow.LayoutParams parametrosFecha = new TableRow.LayoutParams(
                                        0,
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        1 // Peso inicial
                                );
                                fechaTextView.setLayoutParams(parametrosFecha);

                                row.addView(fechaTextView);

                                TextView correoTextView = new TextView(getActivity());
                                correoTextView.setText(correoAlumno);
                                TableRow.LayoutParams parametrosCorreo = new TableRow.LayoutParams(
                                        0,
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        1 // Peso inicial
                                );
                                correoTextView.setLayoutParams(parametrosCorreo);
                                row.addView(correoTextView);

                                Button verButton = new Button(getActivity());
                                verButton.setText("Detalles");
                                verButton.setVisibility(View.VISIBLE);

                                verButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        verDetalles(correoAlumno,id,fecha,trimestre,sesEmp,numAl,tipo,remuneracion);

                                    }
                                });

                                TableRow.LayoutParams parametrosDetalles = new TableRow.LayoutParams(
                                        0,
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        1 // Peso inicial
                                );
                                verButton.setLayoutParams(parametrosDetalles);
                                verButton.setBackgroundResource(R.drawable.boton_redondeado);
                                row.addView(verButton);

                                TableRow.LayoutParams params = new TableRow.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        1 // Peso deseado
                                );

                                // Establece el peso en la fila
                                row.setLayoutParams(params);

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

    private void verDetalles(String correoAlumno, String id, String fecha, String trimestre, String sesiones, String alumnos, String tipo, String remuneracion){
        String mensaje = "Fecha de entrega: "+fecha+"\nTrimestre: "+trimestre+"\nNúmero de sesiones empleadas: "+sesiones+"\nNúmero de alumnos: "+alumnos+"\nTipo de mantenimiento: "+tipo;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actividad")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar entrega", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aceptarEntrega(id);
                        actualizarSaldoAlumno(correoAlumno,remuneracion);

                    }
                })
                .setNegativeButton("Rechazar entrega", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        rechazarEntrega(id);
                        // Acción al hacer clic en el botón "Cancelar"

                    }
                })
                .setNeutralButton("Atrás", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void aceptarEntrega(String id){
        String url = getString(R.string.url)+"aprobarActividad.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String estado = jsonObject.getString("estado");
                            String mensaje = jsonObject.getString("mensaje");
                            if (estado.equals("exito")) {
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String url = getString(R.string.url) + "selecActividades.php";
                        generarTabla(url); // Actualizar la tabla después de rechazar la entrega
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
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void rechazarEntrega(String id){
        String url = getString(R.string.url)+"rechazarActividad.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String estado = jsonObject.getString("estado");
                            String mensaje = jsonObject.getString("mensaje");
                            if (estado.equals("exito")) {
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String url = getString(R.string.url) + "selecActividades.php";
                        generarTabla(url); // Actualizar la tabla después de rechazar la entrega

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
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void actualizarSaldoAlumno(String correoAlumno, String remuneracion){
        String URL_OBTENER_SALDO = getString(R.string.url)+"consultarSaldo.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL_OBTENER_SALDO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("saldo")) {
                                double saldoActual = jsonObject.getDouble("saldo");
                                double nuevoSaldo = saldoActual + Double.parseDouble(remuneracion);

                                //jsonObject.put("saldo", nuevoSaldo); // Actualizar el saldo en el objeto JSON

                                String URL_MODIFICAR_SALDO = getString(R.string.url)+"modificarSaldo.php";
                                StringRequest modificarSaldoRequest = new StringRequest(Request.Method.POST, URL_MODIFICAR_SALDO,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String estado = jsonObject.getString("estado");
                                                    String mensaje = jsonObject.getString("mensaje");
                                                    if (estado.equals("exito")) {
                                                        // Saldo actualizado correctamente
                                                        // Aquí puedes realizar cualquier acción adicional necesaria
                                                    } else {
                                                        // Error al actualizar el saldo

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
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("correo", correoAlumno);
                                        params.put("nuevoSaldo", String.valueOf(nuevoSaldo)); // Enviar el nuevo saldo como número
                                        return params;
                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                requestQueue.add(modificarSaldoRequest);
                            } else {
                                // No se encontró el saldo del usuario

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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("correo", correoAlumno);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

}