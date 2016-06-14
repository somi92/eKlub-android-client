package rs.fon.eklubmobile.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by milos on 6/13/16.
 */
public class Attendance implements Parcelable {

    @SerializedName("id")
    private long id;
    @SerializedName("member")
    private Member member;
    @SerializedName("training")
    private Training training;
    @SerializedName("isAttendant")
    private boolean isAttendant;
    @SerializedName("lateMin")
    private int lateMin;

    public Attendance() {
    }

    public Attendance(long id, Member member, boolean isAttendant, int lateMin) {
        this.id = id;
        this.member = member;
        this.training = training;
        this.isAttendant = isAttendant;
        this.lateMin = lateMin;
    }

    protected Attendance(Parcel in) {
        id = in.readLong();
        member = in.readParcelable(Member.class.getClassLoader());
        isAttendant = in.readByte() != 0;
        lateMin = in.readInt();
    }

    public static final Creator<Attendance> CREATOR = new Creator<Attendance>() {
        @Override
        public Attendance createFromParcel(Parcel in) {
            return new Attendance(in);
        }

        @Override
        public Attendance[] newArray(int size) {
            return new Attendance[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public boolean isIsAttendant() {
        return isAttendant;
    }

    public void setIsAttendant(boolean isAttendant) {
        this.isAttendant = isAttendant;
    }

    public int getLateMin() {
        return lateMin;
    }

    public void setLateMin(int lateMin) {
        this.lateMin = lateMin;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Attendance other = (Attendance) obj;
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
        parcel.writeParcelable(member, i);
        parcel.writeByte((byte) (isAttendant ? 1 : 0));
        parcel.writeInt(lateMin);
    }
}

