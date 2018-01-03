package tricolor.com.chennaiwardmap.model;

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
    private String zoneNo;
    private String zoneName;

    public WardInfo(int wardNo, String zoneNo, String zoneName) {
        this.wardNo = wardNo;
        this.zoneNo = zoneNo;
        this.zoneName = zoneName;
    }

    public WardInfo() {
        zoneNo = "";
        zoneName = "";
    }

    public String getTitle() {
        return String.format("%s \n Ward No: %s", zoneName, wardNo);
    }
}
