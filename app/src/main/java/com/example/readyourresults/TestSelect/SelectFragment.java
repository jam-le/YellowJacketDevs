package com.example.readyourresults.TestSelect;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.readyourresults.Camera.CamActivity;
<<<<<<< HEAD
import com.example.readyourresults.Camera.CamFragment;
import com.example.readyourresults.Help.InfoBrochureFragment;
=======
>>>>>>> 86852c1c1b5651d5b841a556c7305b2c16a8b54e
import com.example.readyourresults.R;
import com.github.barteksc.pdfviewer.PDFView;

public class SelectFragment extends Fragment {
    PDFView selectPdfView;
    View v;
    String testType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(R.string.test_select);
        return inflater.inflate(R.layout.fragment_select, container, false);
    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        //Spinner
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.test_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        v = view;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                // Set Brochure PDF
//                selectPdfView = v.findViewById(R.id.select_pdf_view);
//                String selectedItem = parentView.getItemAtPosition(position).toString();
//
//                if (selectedItem.equals(getResources().getStringArray(R.array.test_array)[0])) {
//                    selectPdfView.fromAsset("InstiProductInsert.pdf").load();
//                } else {
//                    selectPdfView.fromAsset("SubjectInformationBrochure.pdf").load();
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        TextView instiProductInsertLink = getView().findViewById(R.id.insti_product_insert_link);
        instiProductInsertLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment packageInsertFragment = new PackageInsertFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, packageInsertFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Scan Results
        Button scan = getView().findViewById(R.id.scan_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Fragment camFragment = new CamFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, camFragment)
                        .addToBackStack(null)
                        .commit();

                */
                Intent intent = new Intent(getActivity(), CamActivity.class);
                intent.putExtra("Test Type", testType);
                startActivity(intent);
            }
        });
    }
}
