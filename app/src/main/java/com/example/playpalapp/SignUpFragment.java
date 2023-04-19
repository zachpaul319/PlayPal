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
    EditText[] fields = new EditText[4];

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
        fields[0] = view.findViewById(R.id.createUsernameField);
        fields[1] = view.findViewById(R.id.createPasswordField);
        fields[2] = view.findViewById(R.id.currentProductionField);
        fields[3] = view.findViewById(R.id.pastProductionsField);

        view.findViewById(R.id.letsGoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFieldsFilledOut(fields)) {
                    String username = fields[0].getText().toString();
                    String password = fields[1].getText().toString();
                    String currentProduction = fields[2].getText().toString();
                    String pastProductions = fields[3].getText().toString();

                    NewUserRequest newUserRequestObject = new NewUserRequest(username, password, currentProduction, pastProductions);

                    UserModel userModel = new UserModel();
                    userModel.createUser(newUserRequestObject, new UserModel.CreateUserResponseHandler() {
                        @Override
                        public void response() {
                            Toaster.showToast(getContext(), "Account Created");
                        }

                        @Override
                        public void error() {
                            Toaster.showToast(getContext(), "An error occurred");
                        }
                    });
                } else {
                    changeBorderColors(fields);
                    showIncompleteFieldsToast(getContext());
                }
            }
        });


//        view.findViewById(R.id.letsGoButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_homePageFragment);
//            }
//        });
        return view;
    }
}