package com.example.web_mysql

import android.R
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.web_mysql.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    //private val IP_ADDRESS = "172.30.40.6"
    private val IP_ADDRESS = "172.30.40.7"
    private val TAG = "phptest"
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addData.setOnClickListener {
            val Day : String = binding.addDay.text.toString()
            val Time: String = binding.addTime.text.toString()
            val task = InsertData()
            //task.execute("http://$IP_ADDRESS/input.php", Day, Time)
            task.execute("http://$IP_ADDRESS/insertData.php", Day, Time)
        }

    }

    /*Insert Data in mysql*/
    private class InsertData : AsyncTask<String, Void, String>() {


        override fun doInBackground(vararg params: String?): String {


            val serverURL: String? = params[0]
            val Day: String? = params[1]
            val Time: String? = params[2]
            val postParameters: String = "date=$Day&time=$Time"
            Log.d("day",postParameters.toString())
            try {
                val url = URL(serverURL)
                val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection


                httpURLConnection.readTimeout = 5000
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.connect()


                val outputStream: OutputStream = httpURLConnection.outputStream
                outputStream.write(postParameters.toByteArray(charset("UTF-8")))
                outputStream.flush()
                outputStream.close()

                val responseStatusCode: Int = httpURLConnection.responseCode


                val inputStream: InputStream
                inputStream = if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    httpURLConnection.inputStream
                } else {
                    httpURLConnection.errorStream
                }

                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                val sb = StringBuilder()
                var line: String? = null

                while (bufferedReader.readLine().also({ line = it }) != null) {
                    sb.append(line)
                }

                bufferedReader.close();
                Log.d("이거뭐임3",sb.toString())
                return sb.toString();

            } catch (e: Exception) {
                Log.d("안듀ㅙㅁ",e.message.toString())
                return "Error" + e.message
            }

        }

    }
}