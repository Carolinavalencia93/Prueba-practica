package co.com.ceiba.mobile.pruebadeingreso.base;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

public class BaseApplication  extends Application {

	public ProgressDialog mProgressDialog;

	public void showProgressDialog(String titulo, Context context) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage(titulo);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCanceledOnTouchOutside(false);
		}

		mProgressDialog.show();
	}

	public void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}



}
