package tricolor.com.chennaiwardmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
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

    public static final String WARD_NAME_MESSAGE = "tricolor.com.chennaiwardmap.WARD_NAME_MESSAGE";
    public static final String SER_KEY = "tricolor.com.chennaiwardmap.SER_KEY";

    private GoogleMap mMap;
    private static final int defaultZoom = 13;
    private static final LatLng chennai = new LatLng(13.0827, 80.2707);
    private static LatLng currentLocation = null;
    private List<Marker> markers = new ArrayList<>();
    private KmlLayer kmlLayer;
    private WardInfoDao wardInfoDao;
    private WardInfo currentWardInfo;

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

    public void updateSelectedWardInfo(WardInfo wardInfo) {
        currentWardInfo = wardInfo;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setDefaultConfig();
        try {
            kmlLayer = new KmlLayer(mMap, R.raw.geo_chennai_wards, this);
            kmlLayer.addLayerToMap();
            moveAndCreateMarkerInCurrentLocation(googleMap);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                onLocationSelected(latLng);
            }
        });
    }

    private void moveAndCreateMarkerInCurrentLocation(GoogleMap googleMap) {
        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria mCriteria = new Criteria();
        String bestProvider = String.valueOf(manager.getBestProvider(mCriteria, true));

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            Location mLocation = manager.getLastKnownLocation(bestProvider);
            if (mLocation != null) {
                LatLng currentLatLng = new LatLng(mLocation.getLatitude(),
                        mLocation.getLongitude());
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng,
                        defaultZoom);
                googleMap.moveCamera(update);
                onLocationSelected(currentLatLng);
            }
        }
    }

    public void onLocationSelected(LatLng latLng) {
        removeAllMarkers();
        KmlPlacemark kmlPlacemark = KmlUtil.containsInAnyPolygon(kmlLayer, latLng);
        currentLocation = latLng;
        if (kmlPlacemark != null) {
            String zone_no = kmlPlacemark.getProperty(ZONE_NO);
            WardInfo wardInfo = wardInfoDao.getWardInfo(zone_no);
            Marker clickLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(wardInfo.getTitle()));
            markers.add(clickLocationMarker);
            updateSelectedWardInfo(wardInfo);
        }
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
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    public void removeAllMarkers() {
        for (Marker m : markers) {
            m.remove();
        }
        markers.clear();
    }

    public void showWardDetails(View view) {
        Intent intent = new Intent(this, ResultViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SER_KEY, currentWardInfo);
        bundle.putParcelable("latLong", currentLocation);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                moveAndCreateMarkerInCurrentLocation(mMap);

            }
        }
    }
}
