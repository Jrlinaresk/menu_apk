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
import com.palgao.menu.modules.Bussiness.Fragments.BusinessViewModel;
import com.palgao.menu.modules.Bussiness.entityes.Business;

public class BusinessFragment extends Fragment {
    private BusinessViewModel viewModel;
    private TextView textViewBusinessName;
    private TextView textViewBusinessDescription;
    private TextView textViewBusinessAddress;
    private TextView textViewBusinessLocation;
    private TextView textViewBusinessHorario;
    private TextView textViewBusinessEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BusinessViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bussiness, container, false);

        textViewBusinessName = view.findViewById(R.id.text_view_business_name);
        textViewBusinessDescription = view.findViewById(R.id.text_view_business_description);
        textViewBusinessAddress = view.findViewById(R.id.text_view_business_address);
        textViewBusinessLocation = view.findViewById(R.id.text_view_business_location);
        textViewBusinessHorario = view.findViewById(R.id.text_view_business_horario);
        textViewBusinessEmail = view.findViewById(R.id.text_view_business_email);

        String businessId = "66f9e0b210388fd00e51ec66";
        viewModel.getBusinessById(businessId).observe(getViewLifecycleOwner(), new Observer<Business>() {
            @Override
            public void onChanged(Business business) {
                if (business != null) {
                    // Actualizar las vistas con los datos del negocio
                    textViewBusinessName.setText(business.getName());
                    textViewBusinessDescription.setText(business.getDescription());
                    textViewBusinessAddress.setText(String.format("%s %s, %s, %s, %s, %s",
                            business.getAddress().getStreet(),
                            business.getAddress().getNumber(),
                            business.getAddress().getCity(),
                            business.getAddress().getState(),
                            business.getAddress().getCountry(),
                            business.getAddress().getZip()));
                    textViewBusinessLocation.setText(String.format("Lat: %.6f, Lng: %.6f",
                            business.getLocation().getLat(),
                            business.getLocation().getLng()));
                    textViewBusinessHorario.setText(String.format("Abierto de %d a %d",
                            business.getHorario().getHora_open(),
                            business.getHorario().getHora_close()));
                    textViewBusinessEmail.setText(business.getEmail());
                }
            }
        });

        return view;
    }
}
