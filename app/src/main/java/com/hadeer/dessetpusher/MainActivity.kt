package com.hadeer.dessetpusher

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
    var startItem  = 0
    var dessertCount = 0
    var dessertTotalCost = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Timber.i("onCreate called")
        enableEdgeToEdge()
        setContentView(binding.root)
        toolbar = binding.appToolbar
        if(savedInstanceState != null){
            dessertCount = savedInstanceState.getInt(AMOUNT_KEY)
            dessertTotalCost = savedInstanceState.getInt(COST_KEY)
        }
        binding.detailsSectionInclude.generateBtn.setOnClickListener {
            Timber.i("Hash Map $receiptData")
            PdfCreation().createPdf(it.context, dessertTotalCost, dessertCount, receiptData)
        }
        handleDisplayedNumber()
        handleDessertDisplay()
        binding.dessertImg.setOnClickListener {view->
            handleDessertClicking()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.share_receipt){
            successStartActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun successStartActivity() {
        startActivity(createSharedIntent())
    }

    private fun createSharedIntent():Intent{
        val shardIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/html"

        }

        return shardIntent
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

