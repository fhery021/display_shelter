package com.fhery021.displayshelter.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fhery021.displayshelter.R;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.ImagePrintable;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements PrintingCallback {

    // direct net path
    // private static final String IMAGE_PATH = "https://i.ya-webdesign.com/images/small-circle-png-9.png";
    // firebase png
     private static final String IMAGE_PATH = "https://firebasestorage.googleapis.com/v0/b/mysvgprovider.appspot.com/o/small-circle-png-9%5B1%5D.png?alt=media&token=efc981c6-4f1b-47f3-a7b8-8a256cc50db7";
    // firebase svg
//    private static final String IMAGE_PATH = "https://firebasestorage.googleapis.com/v0/b/mysvgprovider.appspot.com/o/small-circle-png-9_1_.svg?alt=media&token=ea7ec6a1-e529-4050-8719-e1d3ee4a56ee";

    Printing printing;
    Button btn_pair_unpair, btn_print, btn_print_image;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Picasso.get().setLoggingEnabled(true);

        if (Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();

        initView(view);

    }

    private void initView(View view) {
        btn_print = view.findViewById(R.id.btnPrint);
        btn_pair_unpair = view.findViewById(R.id.btnPairUnpair);
        btn_print_image = view.findViewById(R.id.btnPrintImage);

        if (printing != null) {
            printing.setPrintingCallback(this);
        }
        //Event
        btn_pair_unpair.setOnClickListener(v -> {
            if (Printooth.INSTANCE.hasPairedPrinter()) {
                Printooth.INSTANCE.removeCurrentPrinter();
            } else {
                startActivityForResult(new Intent(getActivity(), ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
                changePairAndUnpair();
            }
        });

        btn_print_image.setOnClickListener(v -> {
            if (!Printooth.INSTANCE.hasPairedPrinter()) {
                startActivityForResult(new Intent(getActivity(), ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
            } else {
                printImages();
            }
        });

        btn_print.setOnClickListener(v -> {
            if (!Printooth.INSTANCE.hasPairedPrinter()) {
                startActivityForResult(new Intent(getActivity(), ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
            } else {
                printText();
            }
        });

        changePairAndUnpair();
    }

    private void printText() {
        ArrayList<Printable> printables = new ArrayList<>();
        printables.add(
                new RawPrintable.Builder
                        (new byte[]{27, 100, 4})
                        .build());

        // add text
        printables.add(
                new TextPrintable.Builder()
                        .setText("Hello")
                        .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                        .setNewLinesAfter(1)
                        .build());

        // Custom text
        printables.add(
                new TextPrintable.Builder()
                        .setText("Hali")
                        .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
                        .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                        .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                        .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
                        .setNewLinesAfter(1)
                        .build()
        );
    }

    private void printImages() {
        ArrayList<Printable> printables = new ArrayList<>();

        // load image from the Internet
        Picasso.get()
                .load(IMAGE_PATH)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        printables.add(new ImagePrintable.Builder(bitmap).build());
//                        printing.print(printables);
                        // TODO TRY THIS:
                        Printooth.INSTANCE.printer().print(printables);

                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        showToast("Failed to load image");
                        e.printStackTrace();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        showToast("Loading image");
                    }
                });
    }

    private void changePairAndUnpair() {
        if (Printooth.INSTANCE.hasPairedPrinter()) {
            btn_pair_unpair.setText(
                    new StringBuilder("Unpair ")
                            .append(Printooth.INSTANCE.getPairedPrinter()
                                    .getName()
                                    .toString())
            );
        } else {
            btn_pair_unpair.setText("Pair with Printer");
        }
    }

    @Override
    public void connectingWithPrinter() {
        showToast("Connecting...");
    }

    @Override
    public void connectionFailed(String s) {
        showToast("Failure: " + s);
    }

    @Override
    public void onError(String s) {
        showToast("Error: " + s);
    }

    @Override
    public void onMessage(String s) {
        showToast(s);
    }

    @Override
    public void printingOrderSentSuccessfully() {
        showToast("Order sent to device.");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER &&
                resultCode == ScanningActivity.RESULT_OK) {
            initPrinting();
            changePairAndUnpair();
        }
    }

    private void initPrinting() {
        if (!Printooth.INSTANCE.hasPairedPrinter()) {
            printing = Printooth.INSTANCE.printer();
        }

        if (printing != null) {
            printing.setPrintingCallback(this);
        }
    }

    private void showToast(String msg) {
        Context context = getActivity().getApplicationContext();
        CharSequence text = msg;

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}