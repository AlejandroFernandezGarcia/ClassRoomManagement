package es.udc.apm.classroommanagement.utils.vuforia;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
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

import android.support.v4.app.Fragment;

import es.udc.apm.classroommanagement.Manifest;
import es.udc.apm.classroommanagement.R;
import es.udc.apm.classroommanagement.model.Event;
import es.udc.apm.classroommanagement.model.Room;
import es.udc.apm.classroommanagement.model.Schedule;
import es.udc.apm.classroommanagement.services.RoomService;
import es.udc.apm.classroommanagement.utils.Constants;
import es.udc.apm.classroommanagement.utils.ScanningResultListAdapter;
import es.udc.apm.classroommanagement.utils.ScanningResultListDataModel;

/**
 * Created by Alejandro on 13/05/2017.
 */

public final class ScanningResultDialogHandler extends Handler {
    private final Fragment mFragment;
    // Constants for Hiding/Showing scanning result dialog
    public static final int HIDE_DIALOG = -1;
    public static final int SHOW_DIALOG = 1;

    public View mScanningResultContainer;


    public ScanningResultDialogHandler(Fragment fragment) {
        mFragment = fragment;
    }


    public void handleMessage(Message msg) {

        if (msg.what == HIDE_DIALOG) {
            mScanningResultContainer.setVisibility(View.GONE);
        } else {
            if (mScanningResultContainer.getVisibility() != View.VISIBLE) {
                if (ContextCompat.checkSelfPermission(mFragment.getActivity(), android.Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
                    mFragment.requestPermissions(new String[]{android.Manifest.permission.VIBRATE}, Constants.REQUEST_VIBRATOR_PERMISSION);
                }
                mScanningResultContainer.setVisibility(View.VISIBLE);
                try {
                    Room room = new RoomService().getRoomInfo((short) msg.what);//first query
                    if (ContextCompat.checkSelfPermission(mFragment.getActivity(), android.Manifest.permission.VIBRATE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Vibrator v = (Vibrator) mFragment.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        if (v.hasVibrator()) {
                            v.vibrate(50);
                        }
                    }
                    ((TextView) mScanningResultContainer.findViewById(R.id.build_name)).setText(room.getBuilding().getName());
                    ((TextView) mScanningResultContainer.findViewById(R.id.room_name)).setText(room.getName());
                    ListView list = (ListView) mScanningResultContainer.findViewById(R.id.schedule_list);
                    ScanningResultListAdapter adapter =
                            new ScanningResultListAdapter((ArrayList<ScanningResultListDataModel>) toDataModel(room),
                                    mFragment.getActivity().getApplicationContext());
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
                if (sc.isMonday()) days += mFragment.getString(R.string.MONDAY) + " ";
                if (sc.isTuesday()) days += mFragment.getString(R.string.TUESDAY) + " ";
                if (sc.isWednesday()) days += mFragment.getString(R.string.WEDNESDAY) + " ";
                if (sc.isThursday()) days += mFragment.getString(R.string.THURSDAY) + " ";
                if (sc.isFriday()) days += mFragment.getString(R.string.FRIDAY) + " ";
                if (sc.isSaturday()) days += mFragment.getString(R.string.SATURDAY) + " ";
                if (sc.isSunday()) days += mFragment.getString(R.string.SUNDAY) + " ";
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
