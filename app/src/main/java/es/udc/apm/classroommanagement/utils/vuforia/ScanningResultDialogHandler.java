package es.udc.apm.classroommanagement.utils.vuforia;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.udc.apm.classroommanagement.R;
import es.udc.apm.classroommanagement.model.Event;
import es.udc.apm.classroommanagement.model.Room;
import es.udc.apm.classroommanagement.model.Schedule;
import es.udc.apm.classroommanagement.services.RoomService;
import es.udc.apm.classroommanagement.utils.ScanningResultListAdapter;
import es.udc.apm.classroommanagement.utils.ScanningResultListDataModel;

/**
 * Created by Alejandro on 13/05/2017.
 */

public final class ScanningResultDialogHandler extends Handler {
    private final Activity mActivity;
    // Constants for Hiding/Showing scanning result dialog
    public static final int HIDE_DIALOG = -1;
    public static final int SHOW_DIALOG = 1;

    public View mScanningResultContainer;


    public ScanningResultDialogHandler(Activity activity) {
        mActivity = activity;
    }


    public void handleMessage(Message msg) {

        if (msg.what == HIDE_DIALOG) {
            mScanningResultContainer.setVisibility(View.GONE);
        } else {
            if (mScanningResultContainer.getVisibility() != View.VISIBLE) {
                mScanningResultContainer.setVisibility(View.VISIBLE);
                try {
                    Room room = new RoomService().getRoomInfo((short) msg.what);
                    ((TextView) mScanningResultContainer.findViewById(R.id.build_name)).setText(room.getBuilding().getName());
                    ((TextView) mScanningResultContainer.findViewById(R.id.room_name)).setText(room.getName());
                    ListView list = (ListView) mScanningResultContainer.findViewById(R.id.schedule_list);
                    ScanningResultListAdapter adapter =
                            new ScanningResultListAdapter((ArrayList<ScanningResultListDataModel>) toDataModel(room),
                                    mActivity.getApplicationContext());
                    list.setAdapter(adapter);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<ScanningResultListDataModel> toDataModel(Room room) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        List<ScanningResultListDataModel> result = new ArrayList<ScanningResultListDataModel>();
        for (Event e : room.getEvents()) {
            for (Schedule sc : e.getScheduleList()) {
                String startHour = dateFormat.format(sc.getStartHour());
                String endHour = dateFormat.format(sc.getEndHour());
                String finalHour = startHour + " - " + endHour;
                String days = "";
                if (sc.isMonday()) days += mActivity.getString(R.string.MONDAY) + " ";
                if (sc.isTuesday()) days += mActivity.getString(R.string.TUESDAY) + " ";
                if (sc.isWednesday()) days += mActivity.getString(R.string.WEDNESDAY) + " ";
                if (sc.isThursday()) days += mActivity.getString(R.string.THURSDAY) + " ";
                if (sc.isFriday()) days += mActivity.getString(R.string.FRIDAY) + " ";
                if (sc.isSaturday()) days += mActivity.getString(R.string.SATURDAY) + " ";
                if (sc.isSunday()) days += mActivity.getString(R.string.SUNDAY) + " ";
                String eventName = e.getName();
                String eventType = e.getType().getName();
                String speaker = e.getSpeaker();
                String eventDesc = e.getDescription();
                result.add(new ScanningResultListDataModel(finalHour, days, eventName, eventType, speaker, eventDesc));
            }
        }
        return result;
    }
}
