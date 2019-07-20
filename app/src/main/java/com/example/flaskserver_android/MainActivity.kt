package com.example.flaskserver_android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    val TAG = "TAG_MainActivity"

    lateinit var mRetrofit :Retrofit
    lateinit var mRetrofitAPI: RetrofitAPI
    lateinit var mCallTodoList : retrofit2.Call<JsonObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRetrofit()

        //버튼 클릭하면 가져오기
        button1.setOnClickListener {
            button1.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            callTodoList()
        }
    }


    // 리스트를 불러온다.
    private fun callTodoList() {
        mCallTodoList = mRetrofitAPI.getTodoList()
        mCallTodoList.enqueue(mRetrofitCallback)//응답을 큐 대기열에 넣는다.
    }

    //http요청을 보냈고 이건 응답을 받을 콜벡메서드
    private val mRetrofitCallback  = (object : retrofit2.Callback<JsonObject>{
        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            t.printStackTrace()
            Log.d(TAG, "에러입니다. => ${t.message.toString()}")
            textView.text = "에러\n" + t.message.toString()

            progressBar.visibility = View.GONE
            button1.visibility = View.VISIBLE
        }

        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            val result = response.body()
            Log.d(TAG, "결과는 => $result")


            var mGson = Gson()
            val dataParsed1 = mGson.fromJson(result, DataModel.TodoInfo1::class.java)
            val dataParsed2 = mGson.fromJson(result, DataModel.TodoInfo2::class.java)
            val dataParsed3 = mGson.fromJson(result, DataModel.TodoInfo3::class.java)

            textView.text = "해야할 일\n" + dataParsed1.todo1.task+"\n"+dataParsed2.todo2.task +"\n"+dataParsed3.todo3.task

            progressBar.visibility = View.GONE
            button1.visibility = View.VISIBLE
        }
    })

    private fun setRetrofit(){
        //레트로핏으로 가져올 url설정하고 세팅
        mRetrofit = Retrofit
            .Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //인터페이스로 만든 레트로핏 api요청 받는 것 변수로 등록
        mRetrofitAPI = mRetrofit.create(RetrofitAPI::class.java)
    }
//    fun POST(){
//        /**
//         * 포스트부분
//         * **/
//        val jsonObj = JsonObject()
//        jsonObj.apply {
//            addProperty("title", "빨간맛")
//            addProperty("singer", "레드벨벳")
//            addProperty("text", "여름에 나온 노래입니다.")
//        }
//
//        APIKindaStuff
//            .service
//            .getVectors(jsonObj)
//            .enqueue(object: retrofit2.Callback<ResponseBody> {
//
//                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable){
//                    println("에러입니다 => ${t.message.toString()}")
//                }
//
//                override fun onResponse(call: retrofit2.Call<ResponseBody>, response: Response<ResponseBody>) {
//                    if(response.isSuccessful){
//                        val msg = response.body()?.string()
//                        println("플라스크 서버로부터 온 POST 메시지 => ${msg.toString()}")
//
//                        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            })
//    }
//    fun GET(){
//        /**
//         * GET하는 부분
//         * **/
//
//        APIKindaStuff
//            .service
//            .greetUser("태완")
//            .enqueue(object: retrofit2.Callback<ResponseBody>{
//
//                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable){
//                    println("에러입니다 => ${t.message.toString()}")
//                }
//
//                override fun onResponse(call: retrofit2.Call<ResponseBody>, response: Response<ResponseBody>) {
//                    if(response.isSuccessful){
//                        val msg = response.body()?.string()
//                        println("플라스크 서버로부터 온 GET 메시지 => ${msg.toString()}")
//
//                        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//            })
//    }
}
