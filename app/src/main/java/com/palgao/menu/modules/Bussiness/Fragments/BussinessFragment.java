package com.palgao.menu.modules.Bussiness.Fragments;

import static com.palgao.menu.tools.toolsUI.calculateNumberOfColumns;
import static com.palgao.menu.tools.toolsUI.setLayoutType;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.palgao.menu.R;
import com.palgao.menu.modules.Bussiness.Adapter.BussinessAdapter;
import com.palgao.menu.modules.Bussiness.BussineRepositoryImpl;
import com.palgao.menu.modules.Bussiness.BussinessViewModelFactory;
import com.palgao.menu.modules.Bussiness.entityes.Bussiness;
import com.palgao.menu.modules.ProgressDialog.SharedLoadingViewModel;
import com.palgao.menu.modules.ui.WaitingDialog;

import java.util.ArrayList;
import java.util.List;

public class BussinessFragment extends Fragment {
    private int isHorizontalLayout = 1;
    private BussinessViewModel bussinessViewModel;
    private RecyclerView rv_bussiness;
    private BussinessAdapter bussineAdapter;
    private SharedLoadingViewModel sharedLoadingViewModel;
    private WaitingDialog mWaitingDialog;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bussiness, container, false);

        InitViews();
        mWaitingDialog = WaitingDialog.show(requireContext(), "Cargando Negocios", true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), calculateNumberOfColumns(R.dimen.item_width, getResources()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_bussiness.setLayoutManager(linearLayoutManager);

        BussineRepositoryImpl bussineRepository = new BussineRepositoryImpl();
        sharedLoadingViewModel = new ViewModelProvider(requireActivity()).get(SharedLoadingViewModel.class);
        sharedLoadingViewModel.getLoadingState().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && !isLoading) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> mWaitingDialog.dismiss(), 1000);
            }
        });

        BussinessViewModelFactory factory = new BussinessViewModelFactory(sharedLoadingViewModel, bussineRepository);
        bussinessViewModel = new ViewModelProvider(this, factory).get(BussinessViewModel.class);

        bussineAdapter = new BussinessAdapter(requireContext(), new ArrayList<>(), isHorizontalLayout, bussinessViewModel, rv_bussiness, getChildFragmentManager());

        rv_bussiness.setAdapter(bussineAdapter);

        // Observar los negocios y actualizar el adaptador
        bussinessViewModel.getAllBusinesses().observe(getViewLifecycleOwner(), new Observer<List<Bussiness>>() {
            @Override
            public void onChanged(List<Bussiness> bussiness) {
                //
                // Actualiza el adaptador con los datos de los drivers
                if(bussiness.size() > 0 )
                {
                    sharedLoadingViewModel.setLoadingState(false);
                }
                bussineAdapter.setBussiness(bussiness);

                List<String> categorias = new ArrayList<>();
                categorias.add("Todos");
                for (Bussiness bussine: bussiness) {
                    if (!categorias.contains(bussine.getType()))
                        categorias.add(bussine.getType());
                }
            }
        });

        // Cargar los negocios
        bussinessViewModel.loadBussiness();

        return view;
    }

    private void InitViews() {
        rv_bussiness = view.findViewById(R.id.rv_bussiness);
    }
}
