package com.fhery021.displayshelter.bluetooth;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;

import androidx.fragment.app.FragmentActivity;

public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
    public MyPrintDocumentAdapter(FragmentActivity activity) {
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {

    }
}
