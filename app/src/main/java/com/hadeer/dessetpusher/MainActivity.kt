package com.hadeer.dessetpusher

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.hadeer.dessetpusher.databinding.ActivityMainBinding
import timber.log.Timber

val AMOUNT_KEY = "amount"
val COST_KEY = "price"

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    var startItem  = 0
    var dessertCount = 0
    var dessertTotalCost = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Timber.i("onCreate called")
        enableEdgeToEdge()
        setContentView(binding.root)
        if(savedInstanceState != null){
            dessertCount = savedInstanceState.getInt(AMOUNT_KEY)
            dessertTotalCost = savedInstanceState.getInt(COST_KEY)
        }
        handleDisplayedNumber()
        handleDessertDisplay()
        binding.dessertImg.setOnClickListener {view->
            handleDessertClicking()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun handleDisplayedNumber(){
        binding.detailsSectionInclude.dessertTotalCount.text = dessertCount.toString()
        binding.detailsSectionInclude.priceTotalValue.text = "$ ${dessertTotalCost.toString()}"
    }


    private fun handleDessertClicking() {
        dessertCount++
        dessertTotalCost += Data.DessertData[startItem].price
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

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy called")
    }
}