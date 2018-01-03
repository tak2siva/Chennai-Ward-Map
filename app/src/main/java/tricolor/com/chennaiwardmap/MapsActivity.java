package tricolor.com.chennaiwardmap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setDefaultConfig();

        try {
            kmlLayer = new KmlLayer(mMap, R.raw.geo_chennai_wards, this);
            kmlLayer.addLayerToMap();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                removeAllMarkers();
                KmlPlacemark kmlPlacemark = KmlUtil.containsInAnyPolygon(kmlLayer, latLng);
                String zone_no = kmlPlacemark.getProperty("ZONE_NO");
                WardInfo wardInfo = wardInfoDao.getWardInfo(zone_no);

                Marker clickLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(wardInfo.getTitle()));
                markers.add(clickLocationMarker);
            }
        });
    }

    private void setDefaultConfig() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chennai, defaultZoom));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        final Marker chennaiMarker = mMap.addMarker(new MarkerOptions().position(chennai).title("Marker in Chennai"));
        markers.add(chennaiMarker);
    }

    public void removeAllMarkers() {
        for (Marker m : markers) {
            m.remove();
        }
        markers.clear();
    }
}
