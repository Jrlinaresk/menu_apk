package com.palgao.menu.modules.Bussiness.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.palgao.menu.R;
import com.palgao.menu.modules.Bussiness.entityes.Bussiness;

public class BussinessDetailsFragment extends Fragment {
    private BussinessViewModel viewModel;
    private TextView textViewBusinessName;
    private TextView textViewBusinessDescription;
    private TextView textViewBusinessAddress;
    private TextView textViewBusinessLocation;
    private TextView textViewBusinessHorario;
    private TextView textViewBusinessEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BussinessViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bussiness_details, container, false);

        textViewBusinessName = view.findViewById(R.id.text_view_business_name);
        textViewBusinessDescription = view.findViewById(R.id.text_view_business_description);
        textViewBusinessAddress = view.findViewById(R.id.text_view_business_address);
        textViewBusinessLocation = view.findViewById(R.id.text_view_business_location);
        textViewBusinessHorario = view.findViewById(R.id.text_view_business_horario);
        textViewBusinessEmail = view.findViewById(R.id.text_view_business_email);

        String businessId = "66f9e0b210388fd00e51ec66";
        viewModel.getBusinessById(businessId).observe(getViewLifecycleOwner(), new Observer<Bussiness>() {
            @Override
            public void onChanged(Bussiness bussiness) {
                if (bussiness != null) {
                    // Actualizar las vistas con los datos del negocio
                    textViewBusinessName.setText(bussiness.getName());
                    textViewBusinessDescription.setText(bussiness.getDescription());
                    textViewBusinessAddress.setText(String.format("%s %s, %s, %s, %s, %s",
                            bussiness.getAddress().getStreet(),
                            bussiness.getAddress().getNumber(),
                            bussiness.getAddress().getCity(),
                            bussiness.getAddress().getState(),
                            bussiness.getAddress().getCountry(),
                            bussiness.getAddress().getZip()));
                    textViewBusinessLocation.setText(String.format("Lat: %.6f, Lng: %.6f",
                            bussiness.getLocation().getLat(),
                            bussiness.getLocation().getLng()));
                    textViewBusinessHorario.setText(String.format("Abierto de %d a %d",
                            bussiness.getHorario().getHora_open(),
                            bussiness.getHorario().getHora_close()));
                    textViewBusinessEmail.setText(bussiness.getEmail());
                }
            }
        });

        return view;
    }
}
