package pe.com.bbva.iipf.pf.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class DownloadServlet
 */
public class DownloadServlet extends HttpServlet {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = 1L;
       
	   //Initialize global variables
    String fileName="";
    String ruta="";
    
   // private static Logger log = Logger.getLogger(DownloadServlet.class.getName());
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    //Process the HTTP Get request
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        doPost(request,response);
    }
    
    //Process the HTTP Post request
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        log.debug("Entered DownloadServlet");
        OutputStream outStream = response.getOutputStream();
        ruta=fileName=request.getParameter("ruta");
        fileName=request.getParameter("fileName");
        logger.info("ruta:"+ ruta);
        logger.info("Filename:"+ fileName);
        if (ruta == null) {
        	return;
        }
        ruta = java.net.URLDecoder.decode(ruta, "UTF-8");	
                
        
       logger.info("Filename3:"+fileName);
       
        String filePath =ruta ;
        File f=new File(filePath, fileName);
        String fileType = fileName.substring(fileName.indexOf(".")+1,fileName.length());
//        log.debug("Filetype:"+fileType+";"+f.length());

        if (fileType.trim().equalsIgnoreCase("txt")) {
            response.setContentType( "text/plain" );
        } else if (fileType.trim().equalsIgnoreCase("doc")) {
            response.setContentType( "application/msword" );
        } else if (fileType.trim().equalsIgnoreCase("xls")) {
            response.setContentType( "application/vnd.ms-excel" );
        } else if (fileType.trim().equalsIgnoreCase("pdf")) {
            response.setContentType( "application/pdf" );
//            log.debug("content type set to pdf");
        } else {
            response.setContentType( "application/octet-stream" );
        }
        
        response.setContentLength((int)f.length());
         response.setHeader("Content-Disposition"," attachment; filename=\"" + fileName + "\"  ");

         response.setHeader("Cache-Control", "max-age=60"); 
        //response.setHeader("Cache-Control", "no-cache");
        byte[] buf = new byte[8192];
        FileInputStream inStream = new FileInputStream(f);
        int sizeRead = 0;
        while ((sizeRead = inStream.read(buf, 0, buf.length)) > 0) {
//            log.debug("size:"+sizeRead);
            outStream.write(buf, 0, sizeRead);
        }
        inStream.close();
        outStream.close();
        
    }
    
    //Get Servlet information
    public String getServletInfo() {
        return "DownloadServlet Information";
    }
	
}
