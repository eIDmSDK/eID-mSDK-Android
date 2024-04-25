package sk.minv.base.base.activity

import android.os.Parcel
import android.os.Parcelable

interface AndroidParameters : Parcelable

@Suppress("UNUSED_PARAMETER")
class NoParameters() : AndroidParameters {

	constructor(source: Parcel) : this()

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) {}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<NoParameters> = object : Parcelable.Creator<NoParameters> {
			override fun createFromParcel(source: Parcel): NoParameters = NoParameters(source)
			override fun newArray(size: Int): Array<NoParameters?> = arrayOfNulls(size)
		}
	}
}