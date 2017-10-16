package com.foodie.app.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.foodie.app.R;
import com.foodie.app.activities.AccountPaymentActivity;
import com.foodie.app.activities.ChangePasswordActivity;
import com.foodie.app.activities.FavouritesActivity;
import com.foodie.app.activities.ManageAddressActivity;
import com.foodie.app.activities.OrdersActivity;
import com.foodie.app.activities.OtherHelpActivity;
import com.foodie.app.adapter.HelpListAdapter;
import com.foodie.app.adapter.ProfileSettingsAdapter;
import com.foodie.app.utils.ListViewSizeHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHelpFragment extends Fragment {


    @BindView(R.id.help_list_item)
    ListView helpListItem;
    Unbinder unbinder;
    Context context;

    public OrderHelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_help, container, false);
        unbinder = ButterKnife.bind(this, view);

        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.help_array));
        HelpListAdapter adbPerson = new HelpListAdapter(context, list);
        helpListItem.setAdapter(adbPerson);
        helpListItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openSettingPage(position);
            }
        });



        return view;
    }

    private void openSettingPage(int position) {
        switch (position) {
//            case 0:
//                startActivity(new Intent(context, ManageAddressActivity.class));
//                break;
//            case 1:
//                startActivity(new Intent(context, FavouritesActivity.class));
//                break;
//            case 2:
//                startActivity(new Intent(context, AccountPaymentActivity.class));
//                break;
//            case 3:
//                startActivity(new Intent(context, OrdersActivity.class));
//                break;
            case 3:
                startActivity(new Intent(context, OtherHelpActivity.class).putExtra("type","Others"));
                break;
            default:

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
