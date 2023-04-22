package com.example.playpalapp;

import static com.example.playpalapp.FieldChecker.*;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.playpalapp.model.UpdateUserProductionsRequest;
import com.example.playpalapp.model.UserModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProductionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProductionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProductionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProductionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProductionsFragment newInstance(String param1, String param2) {
        EditProductionsFragment fragment = new EditProductionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_productions, container, false);
        EditText[] currentProductionEditField = new EditText[1];
        EditText[] pastProductionEditFields = new EditText[3];

        currentProductionEditField[0] = view.findViewById(R.id.currentProductionEditField);
        
        pastProductionEditFields[0] = view.findViewById(R.id.pastProductionsEditField1);
        pastProductionEditFields[1] = view.findViewById(R.id.pastProductionsEditField2);
        pastProductionEditFields[2] = view.findViewById(R.id.pastProductionsEditField3);
        
        int userId = getArguments().getInt("userId");
        String username = getArguments().getString("username");
        String password = getArguments().getString("password");
        
        view.findViewById(R.id.saveChangesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFieldsFilledOut(currentProductionEditField)) {
                    String currentProduction = currentProductionEditField[0].getText().toString();

                    StringBuilder pastProductions = new StringBuilder();
                    for (int i = 0; i < pastProductionEditFields.length; i++) {
                        String production = pastProductionEditFields[i].getText().toString();
                        if (!production.equals("") && i != 0) {
                            pastProductions.append(", " + production);
                        } else {
                            pastProductions.append(production);
                        }
                    }

                    UpdateUserProductionsRequest updateUserProductionsRequestObject = new UpdateUserProductionsRequest(currentProduction, pastProductions.toString());

                    UserModel userModel = new UserModel();
                    userModel.updateUserProductions(userId, username, password, updateUserProductionsRequestObject, new UserModel.UpdateUserProductionsResponseHandler() {
                        @Override
                        public void response() {
                            Bundle bundle = new Bundle();
                            bundle.putInt("userId", userId);
                            bundle.putString("username", username);
                            bundle.putString("password", password);
                            bundle.putString("currentProduction", currentProduction);
                            bundle.putString("pastProductions", pastProductions.toString());

                            Navigation.findNavController(view).navigate(R.id.action_editProductionsFragment_to_homePageFragment, bundle);
                            Toaster.showToast(getContext(), "Productions Successfully Updated");
                        }

                        @Override
                        public void error() {
                            Toaster.showToast(getContext(), "An error occurred");
                        }
                    });
                } else {
                    changeBorderColors(currentProductionEditField);
                    showIncompleteFieldsToast(getContext());
                }
            }
        });
        
        return view;
    }
}