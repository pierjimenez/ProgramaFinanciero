package pe.com.bbva.iipf.util;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import pe.com.stefanini.core.util.ConexionJDBC;
 
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
public class MergePDF {
	static Logger logger = Logger.getLogger(MergePDF.class.getName());

	
    public static void main(String[] args) {
        try {
            List<InputStream> pdfs = new ArrayList<InputStream>();
            pdfs.add(new FileInputStream(
                    "D:\\mnt\\compartido\\profin\\export\\PF21227.pdf"));
            pdfs.add(new FileInputStream(
                    "D:\\mnt\\compartido\\profin\\export\\PF21060.pdf"));
            OutputStream output = new FileOutputStream(
                    "D:\\mnt\\compartido\\profin\\export\\merge.pdf");
            MergePDF.concatPDFs(pdfs, output, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void concatPDFs(List<InputStream> streamOfPDFFiles,
            OutputStream outputStream, boolean paginate) {
 
        Document document = new Document();
        try {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();
 
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
 
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
 
            document.open();
            PdfContentByte cb = writer.getDirectContent();
 
            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();
            PdfReader pdfReader=null;
            while (iteratorPDFReader.hasNext()) {
                 pdfReader = iteratorPDFReader.next();
               
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
 
                    Rectangle rectangle = pdfReader.getPageSizeWithRotation(1);
                    document.setPageSize(rectangle);
                    document.newPage();
 
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader,
                            pageOfCurrentReaderPDF);
                    switch (rectangle.getRotation()) {
                    case 0:
                        cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                        break;
                    case 90:
                        cb.addTemplate(page, 0, -1f, 1f, 0, 0, pdfReader
                                .getPageSizeWithRotation(1).getHeight());
                        break;
                    case 180:
                        cb.addTemplate(page, -1f, 0, 0, -1f, 0, 0);
                        break;
                    case 270:
                        cb.addTemplate(page, 0, 1.0F, -1.0F, 0, pdfReader
                                .getPageSizeWithRotation(1).getWidth(), 0);
                        break;
                    default:
                        break;
                    }
                    if (paginate) {
                        cb.beginText();
                        cb.getPdfDocument().getPageSize();
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close(); 
            if (streamOfPDFFiles != null){
	            for (int i = 0; i < streamOfPDFFiles.size(); i++)
	            {
	            	streamOfPDFFiles.get(i).close();
	            }  
            }

        } catch (Exception e) {
        	logger.error("Error MergePDF: "+ e.getMessage());
            e.printStackTrace();            
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null){
                    outputStream.close();
                    }
                if (streamOfPDFFiles != null){
    	            for (int i = 0; i < streamOfPDFFiles.size(); i++)
    	            {
    	            	streamOfPDFFiles.get(i).close();
    	            }  
                }

            } catch (IOException ioe) {
            	logger.error("Error MergePDF IO: "+ ioe.getMessage());
                ioe.printStackTrace();
                
            }
        }
    }
}
