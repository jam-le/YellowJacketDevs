package com.example.readyourresults.TestSelect;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readyourresults.R;
import com.github.barteksc.pdfviewer.PDFView;

public class PackageInsertFragment extends Fragment {
    PDFView pdfView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_package_insert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pdfView = view.findViewById(R.id.package_insert_pdf_view);
        pdfView.fromAsset("InstiProductInsert.pdf").load();
    }
}
