package es.udc.apm.classroommanagement.utils;

/**
 * Created by david on 13/05/17.
 */


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
        TextView title=(TextView)popup.findViewById(R.id.title);
        title.setText(marker.getTitle());
        ImageView img_view = ((ImageView) popup.findViewById(R.id.thumbnail));
        TextView snippet_tv = (TextView) popup.findViewById(R.id.snippet);
        if (marker.getTitle().compareTo(context.getString(R.string.popup_your_location)) != 0) {
            String snippet = marker.getSnippet();
            String[] tmp = snippet.split(";");
            String address = tmp[0];
            String phone = tmp[1];
            String web_url = tmp[2];
            String img_url = tmp[3];
            img_view.setVisibility(View.VISIBLE);
            snippet_tv.setVisibility(View.VISIBLE);
            Picasso.with(context).load(img_url).into(img_view);
            snippet_tv.setText(context.getString(R.string.popup_address)+ " " + address + "\n" +
                    context.getString(R.string.popup_phone) + " " + phone + "\n" +
                    context.getString(R.string.popup_web) + " " + web_url);
        }
        else{
            img_view.setVisibility(View.GONE);
            snippet_tv.setVisibility(View.GONE);
            snippet_tv.setText("");
        }
        return(popup);
    }

}



