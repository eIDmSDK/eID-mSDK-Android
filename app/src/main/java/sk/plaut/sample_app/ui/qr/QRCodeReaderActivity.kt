package sk.plaut.sample_app.ui.qr

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import sk.plaut.sample_app.databinding.ActivityQrScannerBinding
import java.io.IOException

class QRCodeReaderActivity : AppCompatActivity() {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private lateinit var binding: ActivityQrScannerBinding

    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource

    /*-------------------------*/
    /*     STATIC METHODS      */
    /*-------------------------*/

    companion object {
        const val EXTRA_QR_DATA = "QR_DATA"

        fun createIntent(context: Context): Intent = Intent(context, QRCodeReaderActivity::class.java)
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMetrics = resources.displayMetrics
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(height, width)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setAutoFocusEnabled(true)
            .build()

        binding.cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(this@QRCodeReaderActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                    cameraSource.start(binding.cameraView.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
            override fun surfaceDestroyed(holder: SurfaceHolder) { cameraSource.stop() }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detections<Barcode>) {
                val qrCodes = detections.detectedItems
                if (qrCodes.size() != 0) {
                    setResultAndFinish(qrCodes.valueAt(0).displayValue)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.release()
        barcodeDetector.release()
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    private fun setResultAndFinish(data: String) {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_QR_DATA, data)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}