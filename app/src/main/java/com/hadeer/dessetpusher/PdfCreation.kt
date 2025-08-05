package com.hadeer.dessetpusher

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
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

     fun createPdf(context : Context, sum :Int, total : Int , data : MutableMap<String, Receipt>){
        val canvas = page.canvas
        val paint = Paint()
        paint.textSize = 16f
         val startX = 80f
         var startY = 100f
         val lineSpacing = paint.fontSpacing  // vertical spacing between lines
         canvas.drawText("Hadeer Gomal Market", startX, startY, paint)
         startY += (lineSpacing * 2)

         canvas.drawText("Total Price: $sum", startX, startY, paint)
         startY += lineSpacing

         canvas.drawText("Total Items: $total", startX, startY, paint)
         startY += lineSpacing

         canvas.drawText(
             "Item       Count       Price     Total",
             startX, startY, paint
         )
         startY += lineSpacing

         for((name, values) in data){
             canvas.drawText(
                 "$name       ${values.count}       ${values.singleCost}     ${values.count * values.singleCost}",
                 startX, startY, paint
             )
             startY += lineSpacing
         }

         doc.finishPage(page)

        //Save the PDF file
        val file = File(context.getExternalFilesDir(null), "example_${System.currentTimeMillis()}.pdf")
        FileOutputStream(file).use { outputStream ->
            doc.writeTo(outputStream)
        }

        doc.close()
        Toast.makeText(context, "PDF generated successfully!" , Toast.LENGTH_SHORT).show()
    }
}


