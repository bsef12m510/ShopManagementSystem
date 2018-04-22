package com.apptriangle.pos.Login.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.print.PrintHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.apptriangle.pos.Login.response.LoginResponse;
import com.apptriangle.pos.Login.restInterface.LoginService;
import com.apptriangle.pos.R;
import com.apptriangle.pos.api.ApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnLoginResponseListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {
    ScrollView container;
    File imageFile;
    Bitmap bitmap;
    private EditText email,password;
    private LoginResponse loginResponseData;
    private OnLoginResponseListener mListener;
    private ProgressDialog pd;
    private Button  loginButton;
    private TextView registerButton;
    private View contentView;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_login, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize(contentView);
        setClickListeners();
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Logging you in");
        pd.setCanceledOnTouchOutside(false);
 }

    public void initialize(View contentView){
        container = (ScrollView) contentView.findViewById(R.id.container);
        registerButton = (TextView) contentView.findViewById(R.id.registerTextView);
        underLineTetView(registerButton);
        loginButton = (Button)contentView.findViewById(R.id.loginButton);
        email = (EditText)contentView.findViewById(R.id.email);
        password = (EditText)contentView.findViewById(R.id.password);
        email.setText("hrauf");
        password.setText("123");
    }
    void underLineTetView(TextView textView)
    {
        textView.setPaintFlags(textView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }

    public void setClickListeners(){
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRegisterClick();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                takeScreenshot();
//                bitmap = getBitmapFromView(container, container.getChildAt(0).getHeight(), container.getChildAt(0).getWidth());
//                doPhotoPrint();
                callLoginService();
            }
        });
    }

    public void onSuccessfulLogin(LoginResponse loginResponseData,String userId) {
        if (mListener != null) {
            mListener.onLoginResponse(loginResponseData, userId);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLoginResponseListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void callLoginService() {
        if(email.getText().toString().trim().equalsIgnoreCase("")){
            email.setError("Username cannot be empty.");
        }else if(password.getText().toString().trim().equalsIgnoreCase("")){
            password.setError("Password cannot be empty");
        }else {
            LoginService loginService =
                    ApiClient.getClient().create(LoginService.class);
            RequestBody emailParam = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());
            RequestBody passParam = RequestBody.create(MediaType.parse("text/plain"), password.getText().toString());
            Call<LoginResponse> call = loginService.login( email.getText().toString(), password.getText().toString());
            pd.show();
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    pd.hide();
                    if (response != null) {
                        loginResponseData = (LoginResponse) response.body();

                        onSuccessfulLogin(loginResponseData,email.getText().toString());
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("failure", "failure");
                    pd.hide();
                    onSuccessfulLogin(null,email.getText().toString());

                }
            });
        }
    }

    //create bitmap from the ScrollView
    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
//            View v1 = getActivity().getWindow().getDecorView().getRootView();
            View v1 = getActivity().getWindow().getDecorView().findViewById(R.id.container);

            v1.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void doPhotoPrint() {
        PrintHelper photoPrinter = new PrintHelper(getActivity());
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                R.drawable.logo);
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLoginResponseListener {
        // TODO: Update argument type and name
        public void onLoginResponse(LoginResponse loginResponseData, String userId);
        public void onRegisterClick();
    }

}
