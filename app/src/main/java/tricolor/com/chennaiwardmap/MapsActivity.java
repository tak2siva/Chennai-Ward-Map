package tricolor.com.chennaiwardmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

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

import tricolor.com.chennaiwardmap.dao.WardInfoDao;
import tricolor.com.chennaiwardmap.db.DatabaseHandle;
import tricolor.com.chennaiwardmap.model.WardInfo;
import tricolor.com.chennaiwardmap.util.KmlUtil;

import static tricolor.com.chennaiwardmap.model.KmlAttribute.ZONE_NO;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int defaultZoom = 13;
    private static final LatLng chennai = new LatLng(13.082680, 80.270718);
    private List<Marker> markers = new ArrayList<>();
    private KmlLayer kmlLayer;
    private WardInfoDao wardInfoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.wardInfoDao = new WardInfoDao(DatabaseHandle.getInstance(getApplicationContext()));
    }

    public void updateText(WardInfo wardInfo) {
        TextView textView = findViewById(R.id.textView);
        textView.setText(String.format("\n\n" +
                        "ZONE: %s\n\n" +
                        "ZONAL OFFICE ADDRESS: %s\n\n" +
                        "ZONAL OFFICE EMAIL: %s\n\n" +
                        "ZONAL OFFICE LANDLINE: %s\n\n" +
                        "ZONAL OFFICER MOBILE: %s\n\n" +
                        "WARD OFFICE ADDRESS: %s\n\n" +
                        "CONTACT: %s\n\n",
                wardInfo.getZoneName().toUpperCase(),
                wardInfo.getZonalOfficeAddress().toUpperCase(),
                wardInfo.getZonalOfficeEmail().toUpperCase(),
                wardInfo.getZonalOfficeLandline().toUpperCase(),
                wardInfo.getZonalOfficeMobile().toUpperCase(),
                wardInfo.getZonalOfficeAddress().toUpperCase(),
                ""));
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

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                removeAllMarkers();
                KmlPlacemark kmlPlacemark = KmlUtil.containsInAnyPolygon(kmlLayer, latLng);
                if (kmlPlacemark != null) {
                    String zone_no = kmlPlacemark.getProperty(ZONE_NO);
                    WardInfo wardInfo = wardInfoDao.getWardInfo(zone_no);
                    Marker clickLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(wardInfo.getTitle()));
                    markers.add(clickLocationMarker);
                    updateText(wardInfo);
                }
            }
        });
    }

    private void setDefaultConfig() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chennai, defaultZoom));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
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
