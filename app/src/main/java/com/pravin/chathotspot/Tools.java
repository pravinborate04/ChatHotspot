package com.pravin.chathotspot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Saurabh Singh on 7/7/16.
 */

public class Tools {

   public static ProgressDialog progressDialog;


    public static void showSnackBar(View view, String message, boolean isError) {

        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });


        snackbar.setActionTextColor(Color.WHITE);

        View sbView = snackbar.getView();

/*
        if (isError)
            sbView.setBackgroundColor(ContextCompat.getColor(AustralianApp.get(), R.color.redColor));
        else
            sbView.setBackgroundColor(ContextCompat.getColor(AustralianApp.get(), R.color.greenColor));
*/

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();

    }

    public static void showSnackBar(View view, String message, boolean isError, String actionString,
                                    View.OnClickListener onClickListener, int duration) {


        Snackbar snackbar = Snackbar
                .make(view, message, duration)
                .setAction(actionString, onClickListener);


        snackbar.setActionTextColor(Color.WHITE);

        View sbView = snackbar.getView();

/*
        if (isError)
            sbView.setBackgroundColor(ContextCompat.getColor(AustralianApp.get(), R.color.redColor));
        else
            sbView.setBackgroundColor(ContextCompat.getColor(AustralianApp.get(), R.color.greenColor));
*/

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();

    }

    public static void showProgress(Context context) {
        if (progressDialog == null && context!=null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
        }

        try {
            if (progressDialog!=null &&!progressDialog.isShowing())
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog = null;
            Log.e("Progress dialog", " issue in progress dialog " + e.toString());
        }

    }





    public static void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog=null;
        }
        // LoadingDialog.dismissLast();
    }



    public static void getAlertDialog(final Context mContext,
                                      final String title, final String msg,
                                      final String positiveBtnString, DialogInterface.OnClickListener OnPositiveClick,
                                      final String negativeBtnString, DialogInterface.OnClickListener onNegativeClick,
                                      final boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setTitle(Html.fromHtml("<h1><b>" + title + "</b></h1>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            builder.setTitle(Html.fromHtml(title));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setMessage(Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY));
        } else {
            builder.setMessage(Html.fromHtml(msg));
        }

        builder.setPositiveButton(positiveBtnString, OnPositiveClick);
        builder.setNegativeButton(negativeBtnString, onNegativeClick);
        builder.setCancelable(isCancelable);
        builder.show();

    }

    /*public static boolean checkNetworkAvailable( Context context ) {
        ConnectivityManager conMgr = ( ConnectivityManager ) context
                .getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if ( i == null ) {
            return false;
        }
        if ( !i.isConnected() ) {
            return false;
        }
        if ( !i.isAvailable() ) {
            return false;
        }
        return true;
    }*/




    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += (listItem.getMeasuredHeight() + 15);
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        //params.height += VIEW_Height;
        listView.setLayoutParams(params);
    }

    public static boolean isNetConnected(Context context) {
        final ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;

    }

	public static String compareToCurrentTime(String sDate ) {
		String sTimeAgo = "";
		Date dToday, dCompare;
		Calendar today = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'");
		String stodayDate = dateFormat.format( today.getTime() );
		try {
			dToday = dateFormat.parse( stodayDate );
			dCompare = dateFormat.parse(sDate);
			sTimeAgo = getDateDifference(dCompare, dToday);

		} catch (ParseException ex) {
			//Logger.getLogger(Prime.class.getName()).log(Level.SEVERE, null, ex);
		}
		return sTimeAgo;
	}



	public static String getDateDifference(Date startDate, Date endDate){

		String timeAgo="";
		//milliseconds
		long different = endDate.getTime() - startDate.getTime();

		Log.e("startDate : ", startDate+""); Log.e("endDate : ", endDate+""); Log.e("different : " , different+"");

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;

		Log.e( ">>",elapsedDays+ " "+ elapsedHours+" minutes "+elapsedMinutes+" seconds "+elapsedSeconds);
		if(elapsedDays>365){
			int y = (int)(elapsedDays/365);
			if(y==1){ timeAgo = String.valueOf(y) + " year ago";}
			else { timeAgo = String.valueOf(y) + " years ago";}
		}else {
			int m = (int) (elapsedDays/30);
			if(m>0 && m<=12){
				if(m==1){ timeAgo = String.valueOf(m) + " month ago";}
				else { timeAgo = String.valueOf(m) + " months ago";}
			}else {
				if(elapsedDays==0){timeAgo = " Today ";}
				else if(elapsedDays == 1){timeAgo = " Yesterday ";}
				else {timeAgo = String.valueOf(elapsedDays) + " days ago";}

			}
		}
		return timeAgo;

	}
}
