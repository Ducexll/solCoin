package tfg.solcoin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PremiosFragment extends Fragment {

    private static final String URL_PREMIOS = R.string.url+"selecPremios.php";

    private List<Premio> listaPremios;
    private TableLayout tablePremios;
    private List<Boolean> listaSeleccionados; // Lista de los premios seleccionados para canjear

    private SharedPreferences preferencias;

    public PremiosFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaPremios = new ArrayList<>();
        listaSeleccionados = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_premios, container, false);
        tablePremios = rootView.findViewById(R.id.tablePremios);

        // Obtener los premios desde el web-service
        obtenerPremiosDesdeWebService();

        return rootView;
    }

    private void obtenerPremiosDesdeWebService() {
        StringRequest request = new StringRequest(Request.Method.GET, URL_PREMIOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject premioObj = jsonArray.getJSONObject(i);
                                int id = premioObj.getInt("idPremios");
                                String nombre = premioObj.getString("nombre");
                                double precio = premioObj.getDouble("precio");

                                Premio premio = new Premio(id, nombre, precio);
                                listaPremios.add(premio);
                                listaSeleccionados.add(false);

                                TableRow row = new TableRow(getActivity());
                                row.setClickable(true);
                                row.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int index = tablePremios.indexOfChild(row);

                                        for (int i = 0; i < row.getChildCount(); i++) {
                                            View child = row.getChildAt(i);
                                            if (child instanceof TextView) {
                                                TextView textView = (TextView) child;
                                                if (listaSeleccionados.get(index)) {
                                                    textView.setTextColor(Color.BLACK);
                                                } else {
                                                    textView.setTextColor(Color.RED);
                                                }
                                            }
                                        }

                                        listaSeleccionados.set(index, !listaSeleccionados.get(index));
                                    }
                                });

                                TextView nombreTextView = new TextView(getActivity());
                                nombreTextView.setText(nombre);
                                row.addView(nombreTextView);

                                TextView precioTextView = new TextView(getActivity());
                                precioTextView.setText(String.valueOf(precio));
                                row.addView(precioTextView);

                                Button canjearButton = new Button(getActivity());
                                canjearButton.setText("Canjear");
                                canjearButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int index = tablePremios.indexOfChild(row);
                                        Premio premioSeleccionado = listaPremios.get(index);

                                        if (listaSeleccionados.get(index)) {
                                            restarSaldo(premioSeleccionado.getPrecio());
                                        }
                                    }
                                });
                                row.addView(canjearButton);

                                tablePremios.addView(row);
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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void restarSaldo(double precio) {
        SharedPreferences preferencias = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String correo = preferencias.getString("correo", "");

        String URL_OBTENER_SALDO = R.string.url+"consultarSaldo.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL_OBTENER_SALDO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("saldo")) {
                                double saldoActual = jsonObject.getDouble("saldo");
                                double nuevoSaldo = saldoActual - precio;

                                jsonObject.put("saldo", nuevoSaldo); // Actualizar el saldo en el objeto JSON

                                String URL_MODIFICAR_SALDO = R.string.url+"modificarSaldo.php";
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
                                        params.put("correo", correo);
                                        params.put("nuevoSaldo", String.format(Locale.US, "%.2f", nuevoSaldo)); // Enviar el nuevo saldo como número
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
                params.put("correo", correo);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }





}
