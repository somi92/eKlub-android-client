package rs.fon.eklubmobile.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by milos on 6/12/16.
 */
public class Training implements Parcelable {

    @SerializedName("id")
    private long id;
    @SerializedName("dateTime")
    private Date dateTime;
    @SerializedName("durationMinutes")
    private int durationMinutes;
    @SerializedName("description")
    private String description;
    @SerializedName("group")
    private Group group;
    @SerializedName("attendances")
    private List<Attendance> attendances;

    public Training() {
    }

    protected Training(Parcel in) {
        id = in.readLong();
        durationMinutes = in.readInt();
        description = in.readString();
        group = in.readParcelable(Group.class.getClassLoader());
        attendances = in.createTypedArrayList(Attendance.CREATOR);
    }

    public static final Creator<Training> CREATOR = new Creator<Training>() {
        @Override
        public Training createFromParcel(Parcel in) {
            return new Training(in);
        }

        @Override
        public Training[] newArray(int size) {
            return new Training[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Training other = (Training) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeInt(durationMinutes);
        parcel.writeString(description);
        parcel.writeParcelable(group, i);
        parcel.writeTypedList(attendances);
    }
}