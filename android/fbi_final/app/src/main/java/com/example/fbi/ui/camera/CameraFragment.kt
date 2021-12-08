package com.example.fbi.ui.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.fbi.MainActivity
import com.example.fbi.R
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_camera.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {

    private lateinit var cameraViewModel: CameraViewModel
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String
    private lateinit var mCtx: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mCtx = inflater.context
        startCapture()
        cameraViewModel =
            ViewModelProviders.of(this).get(CameraViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_camera, container, false)

        //사진 클릭 시(임시) 팝업 창 띄움
        var camera_picture: ImageView = root.findViewById(R.id.camera_picture)
        camera_picture.setOnClickListener {
            var dialog = CheckBookDialogFragment();
            dialog.show(this.childFragmentManager!!,"tag");
        }
        return root
    }

    // 사진을 찍고 나서 이미지를 파일로 저장해주는 함수
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "user_" + (MainActivity.UserInfo.getUser())?.id,
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath

        }
    }

    fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        activity!!,
                        "com.example.fbi.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)

            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(mCtx.contentResolver, Uri.fromFile(file))

                //여기서 파일 서버로 전송해서 이미지 분석 하면 됨
                camera_picture?.setImageBitmap(bitmap)

                Logger.d("currentPhotoPath : " + currentPhotoPath)
                uploadfiles(currentPhotoPath)
            } else {
                val decode = ImageDecoder.createSource(
                    mCtx.contentResolver,
                    Uri.fromFile(file)
                )
                val bitmap = ImageDecoder.decodeBitmap(decode)
                camera_picture?.setImageBitmap(bitmap)
                Logger.d("currentPhotoPath : " + currentPhotoPath)
                uploadfiles(currentPhotoPath)
            }
        }
    }
    private fun uploadfiles(filePath : String){
        val retrofit = NetworkClient.getRetrofitClient(this.context)
        val uploadAPIs = retrofit!!.create(UploadAPIs::class.java)
        val file = File(filePath)

        val fileReqBody : RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val part = MultipartBody.Part.createFormData("upload", file.name, fileReqBody)
        val bookNum = RequestBody.create("text/plain".toMediaTypeOrNull(), (MainActivity.UserInfo.getUser())?.mindBook.toString())
        val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), (MainActivity.UserInfo.getUser())?.id.toString())

        val call: retrofit2.Call<ResponseBody>? = uploadAPIs.uploadImage(part, bookNum, userId)

        call!!.enqueue(object : Callback, retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                Logger.d("해당 도서가 검출되지 않았습니다.")
            }

            override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                Logger.d("업로드 : " + response.toString())
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Logger.d("해당 도서가 검출되지 않았습니다.")
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                Logger.d("업로드 : " + response.toString())
            }
        })

    }

}
object NetworkClient {
    private const val BASE_URL = "http://27.113.21.252:6400/"
    private var retrofit: Retrofit? = null
    fun getRetrofitClient(context: Context?): Retrofit? {
        if (retrofit == null) {
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .build()
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}

interface UploadAPIs {
    @Multipart
    @POST("/upload")
    fun uploadImage(
        @Part file: MultipartBody.Part?,
        @Part("book_num") book_num: RequestBody,
        @Part("user_id") user_id:RequestBody
    ): Call<ResponseBody>?
}
