package com.xxf.activityresult

import android.app.Activity
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable

class ActivityResult(val requestCode: Int, val resultCode: Int, val data: Intent?) : Parcelable {
    val isOk: Boolean
        get() = resultCode == Activity.RESULT_OK
    val isCanceled: Boolean
        get() = resultCode == Activity.RESULT_CANCELED

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(Intent::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(requestCode)
        parcel.writeInt(resultCode)
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActivityResult> {
        override fun createFromParcel(parcel: Parcel): ActivityResult {
            return ActivityResult(parcel)
        }

        override fun newArray(size: Int): Array<ActivityResult?> {
            return arrayOfNulls(size)
        }
    }
}