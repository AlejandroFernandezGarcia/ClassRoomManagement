package es.udc.apm.classroommanagement.utils;

/**
 * Created by david on 13/05/17.
 */


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import es.udc.apm.classroommanagement.R;
import android.content.Context;

public class PopupAdapter implements InfoWindowAdapter {
    private View popup=null;
    private LayoutInflater inflater=null;
    private Context context = null;

    public PopupAdapter(LayoutInflater inflater, Context context) {
        this.inflater=inflater;
        this.context = context;
    }



    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.popup, null);
        }
        TextView tv=(TextView)popup.findViewById(R.id.title);
        tv.setText(marker.getTitle());
        if (marker.getTitle().compareTo("Usted está aquí") != 0) {
            String snippet = marker.getSnippet();
            String[] tmp = snippet.split(";");
            String address = tmp[0];
            String phone = tmp[1];
            String web_url = tmp[2];
            String img_url = tmp[3];
            Log.i("POPUP", img_url);
            ImageView img_view = ((ImageView) popup.findViewById(R.id.thumbnail));
            Picasso.with(context).load(img_url).into(img_view);
            tv = (TextView) popup.findViewById(R.id.snippet);
            tv.setText("Dirección: " + address + "\n" + "Tlf: " + phone + "\n" + "Web: " + web_url);
        }
        else{
            tv = (TextView) popup.findViewById(R.id.snippet);
            tv.setText("");
        }
        return(popup);
    }

}



