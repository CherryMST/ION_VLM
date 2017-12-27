package com.nokia.vlm.entity;

/**
 * @创建者 xuejinghan
 * @创建时间 2017/11/15 10:06
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/11/15
 * @更新描述 ${TODO}
 */

public class TestEquipAsset {
    public static final int TESTEQUIP_ID     = 0;
    public static final int QR               = 1;
    public static final int AREA            = 2;
    public static final int ASSET_ID         = 3;
    public static final int SITE             = 4;
    public static final int DEPARTMENT       = 5;
    public static final int SUBDEPARTMENT    = 6;
    public static final int TYPE             = 7;
    public static final int VENDOR           = 8;
    public static final int SERIRALNUMBER    = 9;
    public static final int MODULE           = 10;
    public static final int WORKINGCONDITION = 11;
    public static final int DESCRIPTION      = 12;
    public static final int REMARK           = 13;
    public static final int CURRENTUSER      = 14;
    public static final int DELIVERDATE      = 15;
    public static final int CALIBRATIONCYCLE = 16;
    public static final int CALIBRATIONREQ   = 17;

    public static final String[] keyArray=new String[]{
            "testequip_id",
            "qr",
            "area",
            "asset_id",
            "site",
            "department",
            "subdepartment",
            "type",
            "vendor",
            "serialnumber",
            "module",
            "workingcondition",
            "description",
            "remark",
            "currentuser",
            "deliverdate",
            "calibrationcycle",
            "calibrationreq",
    };

    public static final String[] arrayForDisplay=new String[]{
            "qr",
            "area",
            "description",
            "asset_id",
            "site",
            "department",
            "subdepartment",
            "currentuser",
            "module",
            "vendor",
            "type",
            "serialnumber",
            "testequip_id",
            "workingcondition",
            "remark",
            "deliverdate",
            "calibrationcycle",
            "calibrationreq"
    };
}
