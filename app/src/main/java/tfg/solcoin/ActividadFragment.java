package tfg.solcoin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class ActividadFragment extends Fragment {

    private Spinner spTrimestre,spTipoActividad ;

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

        return view;
    }
}