package com.example.readyourresults.Help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.readyourresults.R;
import com.github.barteksc.pdfviewer.PDFView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InfoBrochureFragment extends Fragment {
    PDFView pdfView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_brochure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pdfView = view.findViewById(R.id.pdf_view);
        pdfView.fromAsset("SubjectInformationBrochure.pdf").load();
    }
}
