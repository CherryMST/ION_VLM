package com.nokia.vlm.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.nokia.vlm.json.response.BaseResponseJson;

/**
 * @创建者 DK-house
 * @创建时间 2017/11/16 0:21
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/11/16
 * @更新描述 ${TODO}
 */

public class BaseAssetItemInfo extends BaseResponseJson implements Parcelable {
//    public String asset_id         = "";
//    public String site             = "";
//    public String serialnumber    = "";
//    public String currentuser      = "";
//    public String workingcondition = "";

    public String area = "";
    public String description = "";
    public String currentuser = "";
    public String department = "";
    public String workingcondition = "";
    public String qr = "";

    public BaseAssetItemInfo(String area, String description, String currentuser, String department, String workingcondition, String qr) {
        this.area = area;
        this.description = description;
        this.currentuser = currentuser;
        this.department = department;
        this.workingcondition = workingcondition;
        this.qr = qr;
    }



    @Override
    public String toString() {
        return "BaseAssetItemInfo{" +
                "area='" + area + '\'' +
                ", description='" + description + '\'' +
                ", currentuser='" + currentuser + '\'' +
                ", department='" + department + '\'' +
                ", workingcondition='" + workingcondition + '\'' +
                ", qr='" + qr + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BaseAssetItemInfo))
            return false;

        BaseAssetItemInfo that = (BaseAssetItemInfo) o;

        if (area != null ? !area.equals(that.area) : that.area != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (currentuser != null ? !currentuser.equals(that.currentuser) : that.currentuser != null)
            return false;
        if (department != null ? !department.equals(that.department) : that.department != null)
            return false;
        if (workingcondition != null ? !workingcondition.equals(that.workingcondition) : that.workingcondition != null)
            return false;
        return qr != null ? qr.equals(that.qr) : that.qr == null;

    }

    @Override
    public int hashCode() {
        int result = area != null ? area.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (currentuser != null ? currentuser.hashCode() : 0);
        result = 31 * result + (department != null ? department.hashCode() : 0);
        result = 31 * result + (workingcondition != null ? workingcondition.hashCode() : 0);
        result = 31 * result + (qr != null ? qr.hashCode() : 0);
        return result;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }


    public BaseAssetItemInfo(){

    }


    public String getWorkingcondition() {
        return workingcondition;
    }

    public void setWorkingcondition(String workingcondition) {
        this.workingcondition = workingcondition;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrentuser() {
        return currentuser;
    }

    public void setCurrentuser(String currentuser) {
        this.currentuser = currentuser;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    protected void parseCustom(String jsonStr) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.area);
        dest.writeString(this.description);
        dest.writeString(this.currentuser);
        dest.writeString(this.department);
        dest.writeString(this.workingcondition);
    }

    protected BaseAssetItemInfo(Parcel in) {
        this.area = in.readString();
        this.description = in.readString();
        this.currentuser = in.readString();
        this.department = in.readString();
        this.workingcondition = in.readString();
    }

    public static final Parcelable.Creator<BaseAssetItemInfo> CREATOR = new Parcelable.Creator<BaseAssetItemInfo>() {
        @Override
        public BaseAssetItemInfo createFromParcel(Parcel source) {
            return new BaseAssetItemInfo(source);
        }

        @Override
        public BaseAssetItemInfo[] newArray(int size) {
            return new BaseAssetItemInfo[size];
        }
    };
}
