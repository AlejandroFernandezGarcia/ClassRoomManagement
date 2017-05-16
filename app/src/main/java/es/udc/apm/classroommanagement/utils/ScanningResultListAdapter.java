package es.udc.apm.classroommanagement.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import es.udc.apm.classroommanagement.R;

/**
 * Created by Alejandro on 16/05/2017.
 */

public class ScanningResultListAdapter extends ArrayAdapter<ScanningResultListDataModel> {
    private ArrayList<ScanningResultListDataModel> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtHour;
        TextView txtDays;
        TextView txtEventName;
        TextView txtEventType;
        TextView txtSpeaker;
        TextView txtEventDesc;

    }

    public ScanningResultListAdapter(ArrayList<ScanningResultListDataModel> dataSet, Context context){
        super(context, R.layout.camera_overlay_scanning_result_row_list,dataSet);
        this.dataSet=dataSet;
        this.mContext=context;
    }

    private int lastPosition = -1;

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ScanningResultListDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.camera_overlay_scanning_result_row_list, parent, false);
            viewHolder.txtHour = (TextView) convertView.findViewById(R.id.hour);
            viewHolder.txtDays= (TextView) convertView.findViewById(R.id.days);
            viewHolder.txtEventName= (TextView) convertView.findViewById(R.id.event_name);
            viewHolder.txtEventType = (TextView) convertView.findViewById(R.id.event_type);
            viewHolder.txtSpeaker = (TextView) convertView.findViewById(R.id.speaker);
            viewHolder.txtEventDesc= (TextView) convertView.findViewById(R.id.event_desc);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtHour.setText(dataModel.getHour());
        viewHolder.txtDays.setText(dataModel.getDays());
        viewHolder.txtEventName.setText(dataModel.getEventName());
        viewHolder.txtEventType.setText(dataModel.getEventType());
        viewHolder.txtSpeaker.setText(dataModel.getSpeaker());
        viewHolder.txtEventDesc.setText(dataModel.getEventDesc());

//        viewHolder.txtType.setText(dataModel.getType());
//        viewHolder.txtVersion.setText(dataModel.getVersion_number());
//        viewHolder.info.setOnClickListener(this);
//        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
