package es.udc.apm.classroommanagement.utils;

/**
 * Created by david on 13/05/17.
 */

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import es.udc.apm.classroommanagement.R;
import android.content.Context;

import java.net.MalformedURLException;
import java.net.URL;

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
        if (marker.getTitle() != "Usted está aquí") {
            UrlImageView urlImg = ((UrlImageView) popup.findViewById(R.id.thumbnail));
            try {

                urlImg.setImageURL(new URL("http://www.iearobotics.com/personal/juan/conferencias/conf16/images/facultad.png"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            tv = (TextView) popup.findViewById(R.id.snippet);
            tv.setText(marker.getSnippet());
        }
        return(popup);
    }
}