package com.hadeer.dessetpusher

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

const val PDF_PAGE_WIDTH = 595
const val PDF_PAGE_HEIGHT = 842

class PdfCreation {

    //Generate Pdf document instance
    val doc = PdfDocument()
    //Generate page info for the Page
    val pageInfo = PdfDocument.PageInfo.Builder(
        PDF_PAGE_WIDTH, PDF_PAGE_HEIGHT , 1
    ).create()

    val page = doc.startPage(pageInfo)

    lateinit var file :File

     fun createPdf(context : Context, sum :Int, total : Int , data : MutableMap<String, Receipt>){
        val canvas = page.canvas
        val paint = Paint()
        paint.textSize = 16f
         val startX = 80f
         var startY = 100f
         val mainText = "Hadeer Gomal Market"
         val center = (canvas.width - paint.measureText(mainText)) / 2
         val lineSpacing = paint.fontSpacing  // vertical spacing between lines
         val itemX = startX
         val countX = itemX + 150f
         val priceX = countX + 100f
         val totalX = priceX + 100f
         canvas.drawText(mainText, center, startY, paint)
         startY += (lineSpacing * 2)

         canvas.drawText("Total Price: $sum", startX, startY, paint)
         startY += lineSpacing

         canvas.drawText("Total Items: $total", startX, startY, paint)
         startY += (lineSpacing * 2)

         // Table Headers
         canvas.drawText("Item", itemX, startY, paint)
         canvas.drawText("Count", countX, startY, paint)
         canvas.drawText("Price", priceX, startY, paint)
         canvas.drawText("Total", totalX, startY, paint)
         startY += lineSpacing

         for((name, values) in data){
             canvas.drawText(name, itemX, startY, paint)
             canvas.drawText(values.count.toString(), countX, startY, paint)
             canvas.drawText(values.singleCost.toString(), priceX, startY, paint)
             canvas.drawText((values.count * values.singleCost).toString(), totalX, startY, paint)
             startY += lineSpacing
         }
         doc.finishPage(page)

        //Save the PDF file
         file = File(context.getExternalFilesDir(null), "example_${System.currentTimeMillis()}.pdf")
        FileOutputStream(file).use { outputStream ->
            doc.writeTo(outputStream)
        }

        doc.close()
        Toast.makeText(context, "PDF generated successfully!" , Toast.LENGTH_SHORT).show()
    }

    fun getFileUrl(context: Context):Uri?{
        if(!::file.isInitialized || !file.exists()){
            Toast.makeText(context, "There is no file to share" , Toast.LENGTH_SHORT).show()
            return null
        }
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }
}


