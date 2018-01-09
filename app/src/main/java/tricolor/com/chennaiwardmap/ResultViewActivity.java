package tricolor.com.chennaiwardmap;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tricolor.com.chennaiwardmap.model.WardInfo;
import tricolor.com.chennaiwardmap.util.KmlUtil;

import static tricolor.com.chennaiwardmap.MapsActivity.SER_KEY;
import static tricolor.com.chennaiwardmap.MapsActivity.WARD_NAME_MESSAGE;
import static tricolor.com.chennaiwardmap.model.KmlAttribute.ZONE_NO;

public class ResultViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private KmlLayer kmlLayer;
    private  LatLng latLong;

    private List<Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);


        // Get intent value
        Intent intent = getIntent();
        latLong = intent.getParcelableExtra("latLong");
        Object wardObj = intent.getSerializableExtra(SER_KEY);

        if (wardObj == null)
        {
            Toast.makeText(this, "Unable to retrieve the ward location now", Toast.LENGTH_SHORT).show();
        }
        else {
            WardInfo wardInfo = (WardInfo)wardObj;
            populateUI(wardInfo);
        }



    }

    public void populateUI (WardInfo wardInfo) {

        TextView txtZoneName = (TextView)findViewById(R.id.txtZoneName);
        TextView txtZoneOfficeAddress = (TextView)findViewById(R.id.txtZonalOfficeAddress);
        TextView txtZoneNumber = (TextView)findViewById(R.id.txtZoneNum);
        TextView txtZoneOfficeNumber = (TextView)findViewById(R.id.txtZonalOfficeLandline);
        TextView txtZoneOfficerMobile = (TextView)findViewById(R.id.txtZonalOfficerMobile);

        TextView txtWardName = (TextView)findViewById(R.id.txtWardName);
        TextView txtWardOfficeAddress = (TextView)findViewById(R.id.txtWardOfficeAddress);
        TextView txtWardNumber = (TextView)findViewById(R.id.txtWardNum);
        TextView txtWardOfficeContactNumber = (TextView)findViewById(R.id.txtWardContact);

        txtZoneName.setText(wardInfo.getZoneName());
        txtZoneOfficeAddress.setText(wardInfo.getZonalOfficeAddress());
        txtZoneNumber.setText(wardInfo.getZoneNo());
        txtZoneOfficeNumber.setText(wardInfo.getZonalOfficeLandline());
        txtZoneOfficerMobile.setText(wardInfo.getZonalOfficeMobile());

        txtWardNumber.setText(wardInfo.getWardNo());
        txtWardName.setText("Not Available");
        txtWardOfficeAddress.setText("Not Available");
        txtWardOfficeContactNumber.setText("Not Available");


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setDefaultConfig();

        try {
            kmlLayer = new KmlLayer(mMap, R.raw.geo_chennai_wards, this);
            kmlLayer.addLayerToMap();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        removeAllMarkers();
        KmlPlacemark kmlPlacemark = KmlUtil.containsInAnyPolygon(kmlLayer, latLong);
        if (kmlPlacemark != null) {
            String zone_no = kmlPlacemark.getProperty(ZONE_NO);
            Marker clickLocationMarker = mMap.addMarker(new MarkerOptions().position(latLong));
            markers.add(clickLocationMarker);
        }
    }

    private void setDefaultConfig() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15.5f));
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            // reload activity
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        }
    }

    public void removeAllMarkers() {
        for (Marker m : markers) {
            m.remove();
        }
        markers.clear();
    }
}
