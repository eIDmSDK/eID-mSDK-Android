package sk.minv.sample_app.data

import com.google.gson.annotations.SerializedName

class VerifyCertificateResultData {
    @SerializedName("expiration") lateinit var expiration: String
    @SerializedName("verification") lateinit var verification: String
}