package tricolor.com.chennaiwardmap.model;

import android.database.Cursor;

public class WardInfo {
    public static final String TABLE_NAME = "ward_info";
    public static final String COLUMN_WARD_NO = "ward_no";
    public static final String COLUMN_ZONE_NO = "zone_no";
    public static final String COLUMN_ZONE_NAME = "zone_name";
    public static final String COLUMN_ZONAL_OFFICE_ADDRESS = "zonal_office_address";
    public static final String COLUMN_ZONAL_OFFICE_EMAIL = "zonal_officer_email";
    public static final String COLUMN_ZONAL_OFFICE_LANDLINE = "zonal_office_landline";
    public static final String COLUMN_ZONAL_OFFICE_MOBILE = "zonal_officer_mobile";
    public static final String[] ALL_COLUMNS = new String[] {
            COLUMN_WARD_NO,
            COLUMN_ZONE_NO,
            COLUMN_ZONE_NAME,
            COLUMN_ZONAL_OFFICE_ADDRESS,
            COLUMN_ZONAL_OFFICE_EMAIL,
            COLUMN_ZONAL_OFFICE_LANDLINE,
            COLUMN_ZONAL_OFFICE_MOBILE
    };

    private int wardNo;
    private String zoneNo = "";
    private String zoneName = "";
    private String zonalOfficeAddress = "";
    private String zonalOfficeEmail = "";
    private String zonalOfficeLandline = "";
    private String zonalOfficeMobile = "";

    public WardInfo(Cursor cursor) {
        this(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
    }

    public WardInfo(int wardNo, String zoneNo, String zoneName, String zonalOfficeAddress, String zonalOfficeEmail, String zonalOfficeLandline, String zonalOfficeMobile) {
        this.wardNo = wardNo;
        this.zoneNo = zoneNo;
        this.zoneName = zoneName;
        this.zonalOfficeAddress = zonalOfficeAddress;
        this.zonalOfficeEmail = zonalOfficeEmail;
        this.zonalOfficeLandline = zonalOfficeLandline;
        this.zonalOfficeMobile = zonalOfficeMobile;
    }

    public WardInfo() {

    }

    public String getTitle() {
        return String.format("%s \n Ward No: %s", zoneName, wardNo);
    }

    public String getZonalOfficeMobile() {
        return zonalOfficeMobile;
    }

    public void setZonalOfficeMobile(String zonalOfficeMobile) {
        this.zonalOfficeMobile = zonalOfficeMobile;
    }

    public String getZoneNo() {
        return zoneNo;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getZonalOfficeAddress() {
        return zonalOfficeAddress;
    }

    public String getZonalOfficeEmail() {
        return zonalOfficeEmail;
    }

    public String getZonalOfficeLandline() {
        return zonalOfficeLandline;
    }
}
