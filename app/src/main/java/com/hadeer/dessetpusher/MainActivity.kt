package com.hadeer.dessetpusher

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.hadeer.dessetpusher.databinding.ActivityMainBinding
import timber.log.Timber

val AMOUNT_KEY = "amount"
val COST_KEY = "price"

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var toolbar : MaterialToolbar
    private var receiptData  = mutableMapOf<String, Receipt>()
    val pdfCreator = PdfCreation()
    var startItem  = 0
    var dessertCount = 0
    var dessertTotalCost = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        toolbar = binding.appToolbar
        setSupportActionBar(toolbar)
        if(savedInstanceState != null){
            dessertCount = savedInstanceState.getInt(AMOUNT_KEY)
            dessertTotalCost = savedInstanceState.getInt(COST_KEY)
        }
//        binding.detailsSectionInclude.generateBtn.setOnClickListener {
//            pdfCreator.createPdf(it.context, dessertTotalCost, dessertCount, receiptData)
//        }
        handleDisplayedNumber()
        handleDessertDisplay()
        binding.dessertImg.setOnClickListener {view->
            handleDessertClicking()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share_receipt -> {
                successStartActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun successStartActivity() {
        val intent = createSharedIntent()
        if(intent != null){
            startActivity(Intent.createChooser(intent , "Share PDF via...") )
        }
    }

    private fun createSharedIntent():Intent?{
        if(receiptData.isNotEmpty()){
            pdfCreator.createPdf(this, dessertTotalCost, dessertCount, receiptData)
            val file_uri = pdfCreator.getFileUrl(this)
            if(file_uri != null){
                val shardIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM , file_uri)
                    type = "application/pdf"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                return shardIntent
            }
        }
        Toast.makeText(this, "please select items from shop" , Toast.LENGTH_SHORT).show()
      return null
    }


    @SuppressLint("SetTextI18n")
    private fun handleDisplayedNumber(){
        binding.detailsSectionInclude.dessertTotalCount.text = dessertCount.toString()
        binding.detailsSectionInclude.priceTotalValue.text = "$ $dessertTotalCost"
    }


    private fun handleDessertClicking() {
        dessertCount++
        dessertTotalCost += Data.DessertData[startItem].price
        val item = Data.DessertData[startItem]
        val name = item.name
        val newCount = (receiptData[name]?.count ?: 0) + 1
        receiptData[name] = Receipt(newCount, item.price)
        handleDisplayedNumber()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(AMOUNT_KEY , dessertCount)
        outState.putInt(COST_KEY, dessertTotalCost)
        Timber.i("on state instant state called")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_share, menu)
        return super.onCreateOptionsMenu(menu)
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun handleDessertDisplay() {
        binding.root.setOnTouchListener{ view, motionEvent ->
            if(motionEvent.action == MotionEvent.ACTION_DOWN){
                val screenWidth = view.width
                val touchX = motionEvent.x
                if(touchX < screenWidth/2){
                    if(startItem>0){
                        startItem--
                    }else{
                        showToast()
                    }
                }
                else{
                    if(startItem < Data.DessertData.size-1){
                        startItem++
                    }else{
                        showToast()
                    }
                }
                binding.dessertImg.setImageResource(Data.DessertData[startItem].image)
            }
            true
        }
    }

    private fun showToast(){
        Toast.makeText(this, "Sorry there is no more dessert to display" , Toast.LENGTH_SHORT).show()
    }
}

