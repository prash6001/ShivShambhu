package com.pjadhav.myapplication.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pjadhav.myapplication.R;
import com.pjadhav.myapplication.database.DailyQuotesDB;
import com.pjadhav.myapplication.events.FragmentEvent;
import com.pjadhav.myapplication.events.GlobalBus;
import com.pjadhav.myapplication.model.Quotes;
import com.pjadhav.myapplication.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import yalantis.com.sidemenu.interfaces.ScreenShotable;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyQuotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyQuotesFragment extends Fragment implements ScreenShotable{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    DailyQuotesDB dailyQuotesDB;

    Context context;

    public DailyQuotesFragment() {
        // Required empty public constructor
    }

    public static DailyQuotesFragment newInstance(int resId) {
        DailyQuotesFragment dailyQuoteFragment = new DailyQuotesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        dailyQuoteFragment.setArguments(bundle);
        return dailyQuoteFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyQuotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyQuotesFragment newInstance(String param1, String param2) {
        DailyQuotesFragment fragment = new DailyQuotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void getMessage(FragmentEvent.ActivityFragmentMessage activityFragmentMessage) {
        //Write code to perform action after event is received.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_daily_quotes, container, false);
        Quotes quotes = new Quotes ();

        dailyQuotesDB = new DailyQuotesDB(getActivity());
        quotes = dailyQuotesDB.getTodaysQuote();

        GlobalBus.getBus().register(this);
        context = getActivity();
        TextView txtQuote = (TextView) rootView.findViewById(R.id.txtQuote);
        txtQuote.setTypeface(Utils.setFontface(context));
        txtQuote.setText(quotes.getQuoteText());


        Button btnShare = (Button) rootView.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentEvent.ActivityFragmentMessage fragmentActivityMessageEvent =
                        new FragmentEvent.ActivityFragmentMessage(
                                String.valueOf("Hi Prashant"));
                GlobalBus.getBus().post(fragmentActivityMessageEvent);

            }
        });


        return rootView;
    }

    @Override
    public void takeScreenShot() {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

}
