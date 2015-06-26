package com.servicecall.app.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.servicecall.app.R;
import com.servicecall.app.activity.AddressInfoActivity;
import com.servicecall.app.activity.EditDetailsActivity;
import com.servicecall.app.activity.SelectCategoryActivity;
import com.servicecall.app.adapter.BasketComplaintListAdapter;
import com.servicecall.app.helper.BasketComplaintDAO;
import com.servicecall.app.helper.MyIssueDAO;
import com.servicecall.app.model.BasketComplaint;
import com.servicecall.app.model.Complaint;
import com.servicecall.app.util.Session;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.inject.Inject;

public class BasketComplaintListFragment extends Fragment{

    public static final String ARG_ITEM_ID = "basket_complaint_list";

    Activity activity;
    ListView listViewComplaintsInBasket;
    ArrayList<BasketComplaint> basketComplaints = new ArrayList<>();
    ArrayList<Complaint> myIssues = new ArrayList<>();
    BasketComplaintListAdapter basketComplaintListAdapter;
    BasketComplaintDAO basketComplaintDAO;
    MyIssueDAO myIssueDAO;
    int basketComplaintSize = 0;

    Complaint myIssue;

    TextView basketComplaintsCount;

    @Inject
    Session session;
    private ProgressDialog progressDialog;
    Boolean pushedToServer = false;
    Boolean alreadyInServer = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview_complaint, container,
                false);
        findViewsById(view);

        return view;
    }

    private void findViewsById(View view) {
        listViewComplaintsInBasket = (ListView) view.findViewById(R.id.listViewComplaintsInQueue);
        View footer = getActivity().getLayoutInflater().inflate(R.layout.footer_basket_details, null);
        listViewComplaintsInBasket.addFooterView(footer);

        TextView placeOrder = (TextView) footer.findViewById(R.id.placeOrder);
        basketComplaintsCount = (TextView) footer.findViewById(R.id.basketComplaintsCount);

        placeOrder.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View view) {

                                                  Intent myIntent = new Intent(view.getContext(), AddressInfoActivity.class);
                                                  startActivityForResult(myIntent, 0);

                                          }
                                      }
        );


    }
    @Override
    public void onResume() {
        super.onResume();
        GetEmpTask task = new GetEmpTask(activity);
        task.execute((Void) null);
    }

    private class GetEmpTask extends AsyncTask<Void, Void, ArrayList<BasketComplaint>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetEmpTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<BasketComplaint> doInBackground(Void... arg0) {
            basketComplaintDAO = new BasketComplaintDAO(getActivity());
            ArrayList<BasketComplaint> basketComplaintList = basketComplaintDAO.getAllBasketComplaints();
            return basketComplaintList;
        }

        @Override
        protected void onPostExecute(final ArrayList<BasketComplaint> basketComplaintList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
//                Log.d("basketComplaints", basketComplaintList.toString());
                basketComplaints = basketComplaintList;
//                Toast.makeText(getActivity(), "" + String.valueOf(basketComplaints), Toast.LENGTH_LONG).show();
                if (basketComplaintList != null) {
                    if (basketComplaintList.size() != 0) {
                        basketComplaintsCount.setText("Total Complaints in Basket : " + basketComplaints.size());
                        basketComplaintListAdapter = new BasketComplaintListAdapter(activity,
                                basketComplaintList);
                        listViewComplaintsInBasket.setAdapter(basketComplaintListAdapter);

                        listViewComplaintsInBasket.setOnItemClickListener(new GridView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                try {
                                    Intent myIntent = new Intent(getActivity(), EditDetailsActivity.class);
                                    Bundle mBundle = new Bundle();
                                    mBundle.putParcelable("complaint", basketComplaintList.get(pos));
                                    myIntent.putExtras(mBundle);
                                    getActivity().startActivity(myIntent);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                } else {

                    final Toast toast = Toast.makeText(activity, "No Complaints in Basket", Toast.LENGTH_LONG);
                    toast.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                            Intent myIntent = new Intent(getActivity(), SelectCategoryActivity.class);
                            getActivity().startActivity(myIntent);
                        }
                    }, 500);

                }

            }
        }
    }


}