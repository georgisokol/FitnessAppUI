package com.example.fitnessapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fitnessapp.R
import com.example.fitnessapp.utils.FileUtil
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.Vision.Images.Annotate
import com.google.api.services.vision.v1.VisionRequest
import com.google.api.services.vision.v1.VisionRequestInitializer
import com.google.api.services.vision.v1.model.*
import com.googlecode.tesseract.android.TessBaseAPI
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_image_to_macros.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*


class ImageToMacrosActivity : AppCompatActivity() {
    private val STORAGE_REQUEST_CODE = 400
    private val IMAGE_GALLRY_REQUEST_CODE = 1001

    private val CLOUD_VISION_API_KEY = "AIzaSyBlNurPRF3cQthFrLT-414hMQnwj6lUWzQ"

    lateinit var storagePermission: Array<String>
    lateinit private var   bitmap :Bitmap




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_to_macros)


        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        openGallery.setOnClickListener {
            if (!checkStoragePermission()) {
                requestStoragePermission()
            } else {
                pickGallery()
            }
        }
    }

    private fun pickGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_GALLRY_REQUEST_CODE)

    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE)
    }

    private fun checkStoragePermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED)
    }

    // handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (writeStorageAccepted) {
                        pickGallery()
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_GALLRY_REQUEST_CODE) {
                //got image from gallery now crop
                CropImage.activity(data?.data).setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            }
        }
        // get cropped image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val result_uri: Uri = result.uri
                imagePreview.setImageURI(result_uri)
                val bitmapDrawable = imagePreview.drawable as BitmapDrawable
                 bitmap = bitmapDrawable.bitmap
               // uploadImage(bitmap)

                ConvertTask().execute()

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private inner class ConvertTask : AsyncTask<File, Void, String>() {
          var tesseract = TessBaseAPI()



        override fun onPreExecute() {
            super.onPreExecute()
            val datapath = "$filesDir/tesseract/"
            FileUtil.checkFile(
                this@ImageToMacrosActivity,
                datapath,
                File(datapath + "tessdata/")
            )

            tesseract.init(datapath, "eng")
        }

        override fun doInBackground(vararg files: File): String {

            tesseract.setImage(scaleBitmapDown(bitmap, 1200))
            val result = tesseract.utF8Text
            Log.i("REZULTAT", result)
            tesseract.end()
            return result
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            recognizedText.setText(result)
        }
    }



    private fun uploadImage(bitmap: Bitmap?) {

        try {
            callCloudVision(scaleBitmapDown(bitmap!!, 1200)!!)
        } catch (e: IOException) {
            Log.d("GEORGI", "Image picking failed because " + e.message)
        }

    }
    private fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap? {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension
        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth =
                (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension
            resizedHeight =
                (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = maxDimension
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }


    @Throws(IOException::class)
    private fun callCloudVision(bitmap: Bitmap) {
        try {
            val labelDetectionTask: AsyncTask<Any, Void, String> =
                requestTextRecognition(
                    this,
                    prepareAnnotationRequest(bitmap)!!
                )
            labelDetectionTask.execute()
        } catch (e: IOException) {
            Log.d(
                "GEORGI",
                "failed to make API request because of other IOException " +
                        e.message
            )
        }


    }

    @Throws(IOException::class)
    private fun prepareAnnotationRequest(bitmap: Bitmap): Annotate? {
        val httpTransport = AndroidHttp.newCompatibleTransport()
        val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()
        val requestInitializer: VisionRequestInitializer = object :
            VisionRequestInitializer(CLOUD_VISION_API_KEY) {
            @Throws(IOException::class)
            override fun initializeVisionRequest(visionRequest: VisionRequest<*>) {
                super.initializeVisionRequest(visionRequest)
            }
        }
        val builder = Vision.Builder(httpTransport, jsonFactory, null)
        builder.setVisionRequestInitializer(requestInitializer)
        val vision = builder.build()
        val batchAnnotateImagesRequest = BatchAnnotateImagesRequest()
        batchAnnotateImagesRequest.requests = object : ArrayList<AnnotateImageRequest?>() {
            init {
                val annotateImageRequest = AnnotateImageRequest()

                // Add the image
                val base64EncodedImage = Image()
                // Convert the bitmap to a JPEG
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()
                base64EncodedImage.encodeContent(imageBytes)
                annotateImageRequest.image = base64EncodedImage
                annotateImageRequest.features = object : ArrayList<Feature?>() {
                    init {
                        val labelDetection = Feature()
                        labelDetection.type = "DOCUMENT_TEXT_DETECTION"
                        labelDetection.maxResults = 1
                        add(labelDetection)
                    }
                }
                add(annotateImageRequest)
            }
        }
        val annotateRequest = vision.images().annotate(batchAnnotateImagesRequest)
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.disableGZipContent = true
        Log.d(
            "GEORGI",
            "created Cloud Vision request object, sending request"
        )
        return annotateRequest
    }


    class requestTextRecognition(activity: ImageToMacrosActivity, annotate: Annotate) :
        AsyncTask<Any, Void, String>() {
        private var mActivityWeakReference: WeakReference<ImageToMacrosActivity>? = null
        private var mRequest: Annotate? = null

        init {
            mActivityWeakReference = WeakReference(activity)
            mRequest = annotate
        }


        override fun doInBackground(vararg params: Any?): String {
            try {
                Log.d(
                    "Georgi",
                    "created Cloud Vision request object, sending request"
                )
                val response = mRequest!!.execute()
                return convertResponseToString(response)!!
            } catch (e: Exception) {
            }
            return "Request failed. Check your internet connection."
        }

        override fun onPostExecute(result: String?) {
            val activity: ImageToMacrosActivity = mActivityWeakReference?.get()!!

            if (!activity.isFinishing) {
                if (result.equals("")) {
                    activity.recognizedText.setText("Nothing Found")
                } else {
                    activity.recognizedText.setText(result)
                }
            }

        }

        fun convertResponseToString(response: BatchAnnotateImagesResponse): String? {
            var message: String? = String()
            val responses = response.responses
            return try {
                for (res in responses) {
                    val annotation = res.fullTextAnnotation
                    for (page in annotation.pages) {
                        var pageText = ""
                        for (block in page.blocks) {
                            var blockText = ""
                            for (para in block.paragraphs) {
                                var paraText = ""
                                for (word in para.words) {
                                    var wordText = ""
                                    for (symbol in word.symbols) {
                                        wordText = wordText + symbol.text
                                    }
                                    paraText += wordText
                                }
                                Log.i("ZBOROVI VO PARAGRAF", paraText)
                                blockText += paraText

                            }
                            pageText += blockText
                            message = pageText
                        }
                    }
                }
                message
            } catch (e: java.lang.Exception) {
                ""
            }
        }


    }

}

