package com.example.playpalapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.playpalapp.model.UserModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        int userId = getArguments().getInt("userId");
        String username = getArguments().getString("username");
        String password = getArguments().getString("password");

        String welcomeName = "Welcome\n" + username;
        TextView welcomeNameView = view.findViewById(R.id.welcomeNameView);
        welcomeNameView.setText(welcomeName);

        String currentProduction = "Current Production:\n" + getArguments().getString("currentProduction");
        TextView currentProductionView = view.findViewById(R.id.currentProductionView);
        currentProductionView.setText(currentProduction);

        String pastProductions = getArguments().getString("pastProductions").replaceAll(", ", "\n");
        TextView pastProductionsView = view.findViewById(R.id.pastProductionsView);
        pastProductionsView.setText(pastProductions);

        view.findViewById(R.id.deleteAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Account");
                builder.setMessage("Are you sure you want to delete your account?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserModel userModel = new UserModel();
                        userModel.deleteUser(userId, username, password, new UserModel.DeleteUserResponseHandler() {
                            @Override
                            public void response() {
                                Navigation.findNavController(view).navigate(R.id.action_homePageFragment_to_loginFragment);
                                Toaster.showToast(getContext(), "Account Deleted");
                            }

                            @Override
                            public void error() {
                                Toaster.showToast(getContext(), "An error occurred");
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        view.findViewById(R.id.editProductionsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId);
                bundle.putString("username", username);
                bundle.putString("password", password);

                Navigation.findNavController(view).navigate(R.id.action_homePageFragment_to_editProductionsFragment, bundle);
            }
        });

        return view;
    }
}