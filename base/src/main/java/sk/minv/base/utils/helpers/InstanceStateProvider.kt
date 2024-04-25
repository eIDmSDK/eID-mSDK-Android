package sk.minv.base.utils.helpers

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.KProperty

open class InstanceStateProvider<T>(private val args: Bundle) {

	private var cache: T? = null

	operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
		cache = value
		if (value == null) {
			args.remove(property.name)
			return
		}
		when (value) {
			is Int -> args.putInt(property.name, value)
			is Long -> args.putLong(property.name, value)
			is Float -> args.putFloat(property.name, value)
			is String -> args.putString(property.name, value)
			is Bundle -> args.putBundle(property.name, value)
			is Serializable -> args.putSerializable(property.name, value)
			is Parcelable -> args.putParcelable(property.name, value)
		}
	}

	@Suppress("UNCHECKED_CAST")
	protected fun getAndCache(key: String): T? = cache ?: (args.get(key) as T?).apply { cache = this }

	class Nullable<T>(savable: Bundle) : InstanceStateProvider<T>(savable) {
		operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = getAndCache(property.name)
	}

	class NotNull<T>(savable: Bundle) : InstanceStateProvider<T>(savable) {
		operator fun getValue(thisRef: Any?, property: KProperty<*>): T = getAndCache(property.name)!!
	}

	class NotNullWithDefault<T>(savable: Bundle, private val defaultValue: T) : InstanceStateProvider<T>(savable) {
		operator fun getValue(thisRef: Any?, property: KProperty<*>): T = getAndCache(property.name) ?: defaultValue
	}
}