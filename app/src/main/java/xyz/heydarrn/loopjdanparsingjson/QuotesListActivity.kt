package xyz.heydarrn.loopjdanparsingjson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import xyz.heydarrn.loopjdanparsingjson.databinding.ActivityQuotesListBinding

class QuotesListActivity : AppCompatActivity() {
    private lateinit var bindingQuote: ActivityQuotesListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes_list)

        bindingQuote= ActivityQuotesListBinding.inflate(layoutInflater)
        setContentView(bindingQuote.root)

        supportActionBar?.title="List Of Quotes"

        setRecylcerViewAdapter()
        showListQuote()
    }

    private fun setRecylcerViewAdapter(){
        val recyclerLayoutManager=LinearLayoutManager(this)
        bindingQuote.recyclerviewQuoteList.layoutManager=recyclerLayoutManager
        val itemDecoration=DividerItemDecoration(this,recyclerLayoutManager.orientation)
        bindingQuote.recyclerviewQuoteList.addItemDecoration(itemDecoration)
    }

    private fun showListQuote(){
        bindingQuote.progressBarQuoteList.visibility=View.VISIBLE
        val clientListActivity=AsyncHttpClient()
        clientListActivity.get(BASE_URL, object : AsyncHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                bindingQuote.progressBarQuoteList.visibility=View.INVISIBLE

                val listQuote = ArrayList<String>()
                val result = String(responseBody!!)
                Log.d(TAG, result)

                try {
                    val jsonArray = JSONArray(result)
                    for (receivedJsonObject in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(receivedJsonObject)
                        val quote = jsonObject.getString("en")
                        val author = jsonObject.getString("author")
                        listQuote.add("\n$quote\n — $author\n")
                    }
                    val adapter = QuotesAdapter(listQuote)
                    bindingQuote.recyclerviewQuoteList.adapter = adapter
                } catch (e: Exception) {
                    Toast.makeText(this@QuotesListActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            //ketika koneksi gagal
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                bindingQuote.progressBarQuoteList.visibility=View.INVISIBLE
                val errorMessage=when(statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@QuotesListActivity,errorMessage,Toast.LENGTH_SHORT).show()
            }

        })

    }

    companion object {
        private val TAG = QuotesListActivity::class.java.simpleName
        private const val BASE_URL="https://quote-api.dicoding.dev/list"
    }

}