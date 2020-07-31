package com.example.kenyanrides;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {

    CardView cardListVehicles, cardBookings;
    EditText editTextFirstName, editTextSecondName, editTextId, editTextMobileNumber, editTextEmail;
    TextView txtSignOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myview = inflater.inflate(R.layout.fragment_account, null);

        //initialize views
        editTextFirstName = myview.findViewById(R.id.edtxt_first_name);
        editTextSecondName = myview.findViewById(R.id.edtxt_second_name);
        editTextId = myview.findViewById(R.id.edtxt_id);
        editTextMobileNumber = myview.findViewById(R.id.edtxt_mobile_number);
        editTextEmail = myview.findViewById(R.id.edtxt_email_address);
        txtSignOut = myview.findViewById(R.id.txt_sign_out);


        //getting the current user
        user user = SharedPrefManager.getInstance(getActivity()).getUser();

        //setting the values to the textviews
        editTextId.setText(String.valueOf(user.getNational_id()));
        editTextFirstName.setText(user.getFirst_name());
        editTextSecondName.setText(user.getSecond_name());
        editTextMobileNumber.setText(user.getMobile_number());
        editTextEmail.setText(user.getEmail());

        cardListVehicles = myview.findViewById(R.id.card_list_vehicles);

        cardBookings = myview.findViewById(R.id.card_bookings);

        cardListVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ProfileSettings.class);
                startActivity(intent);

            }
        });

        cardBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ProfileSettings.class);
                startActivity(intent);

            }
        });


        //when the user presses signout text
        //calling the logout method
        txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getActivity()).logout();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        return myview;

    }
}
