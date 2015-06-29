package com.servicecall.app.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
import com.servicecall.app.activity.SelectCategoryActivity;
import com.servicecall.app.activity.ViewDetailsActivity;
import com.servicecall.app.adapter.MyIssuesListAdapter;
import com.servicecall.app.helper.MyIssueDAO;
import com.servicecall.app.model.Complaint;
import com.servicecall.app.model.ServerComplaint;
import com.servicecall.app.services.InternetServiceManager;
import com.servicecall.app.util.Session;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.inject.Inject;

public class MyIssuesListFragment extends Fragment{

    public static final String ARG_ITEM_ID = "basket_complaint_list";

    Activity activity;
    ListView listViewComplaintsInBasket;
    ArrayList<ServerComplaint> myIssues = new ArrayList<>();
    MyIssuesListAdapter myIssueListAdapter;
    MyIssueDAO myIssueDAO;
    int myIssueSize = 0;

    Complaint myIssue;

    SharedPreferences complaintDate;
    public static String filename = "mySharedFile";
    TextView myIssuesCount;
    Boolean isInternetPresent = false;

    ProgressDialog progressDialog ;

    @Inject
    Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        isInternetPresent = new NetworkChangeReceiver().checkInternet(getActivity());
        if (isInternetPresent) {
            GetEmpTask task = new GetEmpTask(activity);
            task.execute((Void) null);
        } else {
            final Toast toast = Toast.makeText(getActivity(), "Internet Connection Unavailable. Please connect to Internet to proceed further", Toast.LENGTH_SHORT);
            toast.show();
            getActivity().registerReceiver(
                    new NetworkChangeReceiver(),
                    new IntentFilter(
                            ConnectivityManager.CONNECTIVITY_ACTION));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 2000);        }

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
        myIssuesCount = (TextView ) footer.findViewById(R.id.basketComplaintsCount);

        placeOrder.setText("New Complaint");
        placeOrder.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View view) {

                                              Intent myIntent = new Intent(view.getContext(), SelectCategoryActivity.class);
                                              startActivityForResult(myIntent, 0);

                                          }
                                      }
        );


    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private class GetEmpTask extends AsyncTask<Void, Void, ArrayList<ServerComplaint>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetEmpTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Retrieving Complaints, Please wait ..");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected ArrayList<ServerComplaint> doInBackground(Void... arg0) {
            myIssueDAO = new MyIssueDAO();
            ArrayList<ServerComplaint> myIssueList = myIssueDAO.getAllMyIssues();
            return myIssueList;
        }

        @Override
        protected void onPostExecute(final ArrayList<ServerComplaint> myIssueList) {
            progressDialog.cancel();
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
//                Log.d("myIssues", myIssueList.toString());
                myIssues = myIssueList;
//                Toast.makeText(getActivity(), "" + String.valueOf(myIssues), Toast.LENGTH_LONG).show();
                if (myIssueList != null) {
                    if (myIssueList.size() != 0) {
                        myIssuesCount.setText("Total Issues Raised : " + myIssues.size());
                        myIssueListAdapter = new MyIssuesListAdapter(activity,
                                myIssueList);
                        listViewComplaintsInBasket.setAdapter(myIssueListAdapter);

                        listViewComplaintsInBasket.setOnItemClickListener(new GridView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                try{
                                Intent myIntent = new Intent(getActivity(), ViewDetailsActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putParcelable("serverComplaint", myIssueList.get(pos));
                                myIntent.putExtras(mBundle);
                                getActivity().startActivity(myIntent);
                                } catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                } else {

                    final Toast toast = Toast.makeText(activity, "No Issues Raised Yet", Toast.LENGTH_LONG);
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

            } else {

                final Toast toast = Toast.makeText(activity, "No Issues Raised Yet", Toast.LENGTH_LONG);
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


    private class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            if(checkInternet(context))
            {
                GetEmpTask task = new GetEmpTask(activity);
                task.execute((Void) null);
            }
        }

        boolean checkInternet(Context context) {
            InternetServiceManager internetServiceManager = new InternetServiceManager(context);
            if (internetServiceManager.isNetworkAvailable()) {
                return true;
            } else {
                return false;
            }
        }

    }


}