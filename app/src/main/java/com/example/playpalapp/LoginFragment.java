package com.example.playpalapp;

import static com.example.playpalapp.FieldChecker.*;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.playpalapp.model.AuthRequest;
import com.example.playpalapp.model.User;
import com.example.playpalapp.model.UserModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private final EditText[] fields = new EditText[2];

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ServiceClient serviceClient = ServiceClient.sharedServiceClient(getActivity().getApplicationContext());
        fields[0] = view.findViewById(R.id.usernameField);
        fields[1] = view.findViewById(R.id.passwordField);

        view.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFieldsFilledOut(fields)) {
                    String username = fields[0].getText().toString();
                    String password = fields[1].getText().toString();

                    UserModel userModel = new UserModel();
                    userModel.getUser(username, password, new UserModel.GetUserResponseHandler() {
                        @Override
                        public void response(User user) {
                            Bundle bundle = new Bundle();
                            bundle.putString("username", user.username);
                            bundle.putString("currentProduction", user.currentProduction);
                            bundle.putString("pastProductions", user.pastProductions);
                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homePageFragment, bundle);
                        }

                        @Override
                        public void error() {
                            Toaster.showToast(getContext(), "Invalid Login");
                        }
                    });
                } else {
                    changeBorderColors(fields);
                    showIncompleteFieldsToast(getContext());
                }
            }
        });
//        view.findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment);
//            }
//        });
//        view.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homePageFragment);
//            }
//        });
        return view;

    }
}