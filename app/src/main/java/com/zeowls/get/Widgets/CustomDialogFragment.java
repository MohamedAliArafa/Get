package com.zeowls.get.Widgets;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zeowls.get.MainActivity;
import com.zeowls.get.BackEnd.Core;
import com.zeowls.get.R;
import com.zeowls.get.Utility.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nezar on 5/16/16.
 */
public class CustomDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */

    Picasso picasso;
    String ImageUrl;
    Spinner dropdown;
    Button submit;
    EditText Qyt_EditText;
    ArrayList<Integer> items;
    ArrayAdapter<Integer> adapter;
    int itemID,
//            userId,
            QytSelected;
    Core core;
    ImageView Im_Full;
    RelativeLayout Item_Full_image_linear;

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_full_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        picasso = Picasso.with(getActivity());
        Bundle bundle = getArguments();

        String ImageName = "";

        if (bundle != null) {
            ImageName = bundle.getString("IMAGE_NAME");

        }

//        try {
//            userId = PrefUtils.getCurrentUser(getActivity()).getId();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        core = new Core(getActivity());
        items = new ArrayList<>();

        submit = (Button) view.findViewById(R.id.button1);
        Qyt_EditText = (EditText) view.findViewById(R.id.Shopping_Cart_Item_Qty);
        dropdown = (Spinner) view.findViewById(R.id.spinner1);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.string_dropdown_item, items);
        new getItemQy().execute(itemID);
        dropdown.setOnItemSelectedListener(this);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = "";
                try {
                    address = Qyt_EditText.getText().toString();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (!address.isEmpty()){
//                    new loadingData().execute(address);
                }else {
                    Toast.makeText(getActivity(), "please add your address", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Im_Full = (ImageView) view.findViewById(R.id.Im_Full);
//        Im_Full.setBackground(itemPic.getDrawable());
//        picasso.load(ImageName).into(Im_Full);
//        Item_Full_image_linear = (RelativeLayout) view.findViewById(R.id.Item_Full_image_linear);
//        Item_Full_image_linear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        picasso.load("http://bubble.zeowls.com/uploads/" + item_image).fit().centerCrop().into(Im_Full);



    }

    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        QytSelected = position + 1;
//        Toast.makeText(getActivity(), String.valueOf(QytSelected) + ":" + String.valueOf(id), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        QytSelected = 1;
//        Toast.makeText(getActivity(), String.valueOf(QytSelected), Toast.LENGTH_SHORT).show();
    }

    private class getItemQy extends AsyncTask<Integer, Void, Integer> {

        JSONObject item;

        @Override
        protected void onPostExecute(Integer integer) {
            for (int i = 1; i <= integer; i++) {
                items.add(i);
            }
            dropdown.setAdapter(adapter);
            super.onPostExecute(integer);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int QYT = 0;
            try {
                item = core.getItem(params[0]);
                QYT = item.getJSONArray("Items").getJSONObject(0).getInt("quantity");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return QYT;
        }
    }

//    private class loadingData extends AsyncTask<String, Void, Void> {
//
//        int response = 0;
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//        @Override
//        protected void onPostExecute(Void o) {
//            if (response == 1) {
//                Toast.makeText(getActivity(), "Order Sent Successfully", Toast.LENGTH_SHORT).show();
//                dismiss();
//            } else {
//                Toast.makeText(getActivity(), "please login first", Toast.LENGTH_SHORT).show();
//                ((MainActivity) getActivity()).mDrawerLayout.openDrawer(GravityCompat.START);
//                dismiss();
//            }
//        }
//
//        @Override
//        protected Void doInBackground(String[] params) {
//            response = core.makeOrder(itemID, userId, QytSelected, params[0]);
//            return null;
//        }
//    }
}
