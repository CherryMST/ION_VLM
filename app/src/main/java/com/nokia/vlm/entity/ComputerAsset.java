package com.nokia.vlm.entity;

/**
 * @创建者 xuejinghan
 * @创建时间 2017/11/15 10:19
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/11/15
 * @更新描述 ${TODO}
 */

public class ComputerAsset {
    public static final int QR               = 0;
    public static final int AREA            = 1;
    public static final int ASSET_ID         = 2;
    public static final int SITE             = 3;
    public static final int DEPARTMENT       = 4;
    public static final int SUBDEPARTMENT    = 5;
    public static final int TYPE             = 6;
    public static final int SERIRALNUMBER    = 7;
    public static final int MODULE           = 8;
    public static final int WORKINGCONDITION = 9;
    public static final int DESCRIPTION      = 10;
    public static final int REMARK           = 11;
    public static final int CURRENTUSER      = 12;
    public static final int DELIVERDATE      = 13;
    public static final int BRAND            = 14;
    public static final int IP               = 15;

    public static final String[] keyArray=new String[]{
            "qr",
            "area",
            "asset_id",
            "site",
            "department",
            "subdepartment",
            "type",
            "serialnumber",
            "module",
            "workingcondition",
            "description",
            "remark",
            "currentuser",
            "deliverdate",
            "brand",
            "ip",
    };
}
