package sk.plaut.sample_app.data

import com.google.gson.annotations.SerializedName

class VerifyCertificateResult {
    @SerializedName("timestamp") lateinit var timestamp: String
    @SerializedName("result") val result = VerifyCertificateResultData()
}