package com.fhery021.displayshelter.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fhery021.displayshelter.R;
import com.fhery021.displayshelter.bluetooth.MyPrintDocumentAdapter;

public class HomeFragment extends Fragment  {


    TextView lbl_status;
    Button btn_pair, btn_print;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }

    private void initView (View view) {
        btn_print = view.findViewById(R.id.btnPrint);
        btn_pair = view.findViewById(R.id.btnPair);

        btn_print.setOnClickListener(v -> {
            doPrint();
        });

        btn_pair.setOnClickListener(v -> {
            showToast("Pair selected...");
        });
    }
    private void doPrint() {
        showToast("Begin printing process");
        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) getActivity()
                .getSystemService(Context.PRINT_SERVICE);

        // Set job name, which will be displayed in the print queue
        String jobName = getActivity().getString(R.string.app_name) + " Document";

        // Start a print job, passing in a PrintDocumentAdapter implementation
        // to handle the generation of a print document
        printManager.print(jobName, new MyPrintDocumentAdapter(getActivity()),
                null); //
    }

    private void showToast(String msg) {
        Context context = getActivity().getApplicationContext();
        CharSequence text = msg;

        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Log.println(Log.INFO,"MyTOAST", msg);
    }



}