package com.example.playpalapp;

import static com.example.playpalapp.FieldChecker.*;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.playpalapp.model.NewUserRequest;
import com.example.playpalapp.model.UserModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    EditText[] necessaryFields = new EditText[3];
    EditText[] pastProductionFields = new EditText[3];

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        necessaryFields[0] = view.findViewById(R.id.createUsernameField);
        necessaryFields[1] = view.findViewById(R.id.createPasswordField);
        necessaryFields[2] = view.findViewById(R.id.currentProductionField);

        pastProductionFields[0] = view.findViewById(R.id.pastProductionsField1);
        pastProductionFields[1] = view.findViewById(R.id.pastProductionsField2);
        pastProductionFields[2] = view.findViewById(R.id.pastProductionsField3);

        view.findViewById(R.id.letsGoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFieldsFilledOut(necessaryFields)) {
                    String username = necessaryFields[0].getText().toString();
                    String password = necessaryFields[1].getText().toString();
                    String currentProduction = necessaryFields[2].getText().toString();

                    StringBuilder pastProductions = new StringBuilder();
                    for (int i = 0; i < pastProductionFields.length; i++) {
                        String production = pastProductionFields[i].getText().toString();
                        if ((!production.equals("")) && i != 0) {
                            pastProductions.append(", " + production);
                        } else {
                            pastProductions.append(production);
                        }
                    }

                    NewUserRequest newUserRequestObject = new NewUserRequest(username, password, currentProduction, pastProductions.toString());

                    UserModel userModel = new UserModel();
                    userModel.createUser(newUserRequestObject, new UserModel.CreateUserResponseHandler() {
                        @Override
                        public void response(int status) {
                            if (status == 1) {
                                Toaster.showToast(getContext(), "Username already taken");
                            } else {
                                Navigation.findNavController(view).popBackStack();
                                Toaster.showToast(getContext(), "Account Created. Please Login");
                            }
                        }

                        @Override
                        public void error() {
                            Toaster.showToast(getContext(), "An error occurred");
                        }
                    });
                } else {
                    changeBorderColors(necessaryFields);
                    showIncompleteFieldsToast(getContext());
                }
            }
        });
        return view;
    }
}