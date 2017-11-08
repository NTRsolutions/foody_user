package com.foodie.app.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.foodie.app.R;
import com.foodie.app.adapter.DisputeMessageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


import static com.foodie.app.helper.GlobalData.disputeMessageList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHelpFragment extends Fragment {


    Unbinder unbinder;
    Context context;
    DisputeMessageAdapter disputeMessageAdapter;
    @BindView(R.id.help_rv)
    RecyclerView helpRv;
    @BindView(R.id.other_help_layout)
    LinearLayout otherHelpLayout;
    @BindView(R.id.dispute)
    Button dispute;
    @BindView(R.id.chat_us)
    Button chatUs;


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
        helpRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        helpRv.setItemAnimator(new DefaultItemAnimator());
        helpRv.setHasFixedSize(true);
        disputeMessageAdapter = new DisputeMessageAdapter(disputeMessageList, context,getActivity());
        helpRv.setAdapter(disputeMessageAdapter);

        if(disputeMessageList.size() > 0){
            otherHelpLayout.setVisibility(View.GONE);
        }else{
            otherHelpLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
