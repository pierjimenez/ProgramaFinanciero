/**
 * 
 */
package pe.com.bbva.iipf.descarga.applet;

import java.awt.HeadlessException;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

/**
 * @author CMontes
 *
 */
public class DescargaDirectorioApplet extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -863926512233386903L;
	private static final int BUFFER = 2048;
    private static final String BACKSLASH = "/";
    private static final String EXTENSION_EXCEL = ".xls";

	/**
	 * @throws HeadlessException
	 */
	public DescargaDirectorioApplet() throws HeadlessException {
		
	}
	
	public void start(){
		System.out.println("Iniciando proceso de sincronizacion.....");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {			
			e.printStackTrace();
		}
		try{
		//String idProgramaApp = "12011";
		String idProgramaApp = getParameter("idProgramaApp");
		
		JFileChooser filechooser = new JFileChooser();
		filechooser.setDialogTitle("Seleccione la Ruta donde se guardará el Archivo");
		//filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filechooser.setFileFilter(new ExtensionFileFilter("Archivos Excel","xls"));
	    
		filechooser.setAcceptAllFileFilterUsed(false);
	    //    
		int actionDialog = filechooser.showSaveDialog(this);
	    if ( actionDialog == JFileChooser.APPROVE_OPTION) { 
	      //System.out.println("getCurrentDirectory(): " +  filechooser.getCurrentDirectory());
	    	//System.out.println("Carpeta Seleccionada : " +  filechooser.getSelectedFile().getName());
	    	File filesaveable = null;
	    	if(!filechooser.getSelectedFile().getName().endsWith(EXTENSION_EXCEL)){
	    		filesaveable = new File(filechooser.getSelectedFile() + EXTENSION_EXCEL);
	    	}else{
	    		filesaveable = filechooser.getSelectedFile();
	    	}
	    	if(filesaveable.exists()){
	    		actionDialog = JOptionPane.showConfirmDialog(filechooser, "¿Desea Reemplazar el archivo existente?");	    		
	    		while (actionDialog == JOptionPane.NO_OPTION){
	    			actionDialog=filechooser.showSaveDialog(this);
	    			if (actionDialog == JFileChooser.APPROVE_OPTION){
	    				if(!filechooser.getSelectedFile().getName().endsWith(EXTENSION_EXCEL)){
	    		    		filesaveable = new File(filechooser.getSelectedFile() + EXTENSION_EXCEL);
	    		    	}else{
	    		    		filesaveable = filechooser.getSelectedFile();
	    		    	}	    				
	    				if(filesaveable.exists()){	    		
	    					actionDialog = JOptionPane.showConfirmDialog(filechooser, "¿Desea Reemplazar el archivo existente?");
	    				}
	    			}
	    		}
	    		if(actionDialog == JOptionPane.YES_OPTION){
	    			System.out.println("Guardar el Archivo");
	    			writeFile(idProgramaApp, filesaveable);
	    			try {
		    			getAppletContext().showDocument
		    			(new URL("javascript:alert('Se esta procesando su solicitud por favor esperar un momento')"));
	  	          	}catch (MalformedURLException me) {
	  	        	  me.printStackTrace();
	  	          	}
	    		}
	    	}else{	    		
	    		System.out.println("Guardar el Archivo");
	    		writeFile(idProgramaApp, filesaveable);
	    		try {
	    			getAppletContext().showDocument
	    			(new URL("javascript:alert('Se esta procesando su solicitud por favor esperar un momento')"));
  	          	}catch (MalformedURLException me) {
  	        	  me.printStackTrace();
  	          	}
	    	}
	    	/*
	    	try {
	          getAppletContext().showDocument
	            (new URL("javascript:mostrarRutaSeleccionada(\'" + filechooser.getSelectedFile() +"\')"));
	          }catch (MalformedURLException me) {
	        	  me.printStackTrace();
	          }*/
	    	
	    	//writeDirectory(filechooser.getSelectedFile(),direcFuente);
	    	//realizar la confirmacion de descarga
	    }else{
	    	System.out.println("No Hubo Selección ");
	    }
		/*
		try {
			(new DescargaDirectorioApplet()).aperturarConexion("dato1", "dato2");
		} catch (HeadlessException e) {			
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		}*/
	    System.out.println("Finalizado proceso de sincronizacion.....");
		}catch(Exception e){
			e.printStackTrace();
			try {
    			getAppletContext().showDocument
    			(new URL("javascript:alert('Se ha producido un error debe de cerrar el navegador web y volver a intentarlo.')"));
	          	}catch (MalformedURLException me) {
	        	  me.printStackTrace();
	          }
		}
	}
	
	public void writeFile(String idProgramaApp, File filesaveable){		
		try {
			//sourceDirectory = 
			aperturarConexion(idProgramaApp, filesaveable);
			//copyDirectory(sourceDirectory, directory);
		} catch (Exception e) {
			 
			e.printStackTrace();
		}		
	}
	/*
	public void writeDirectory(File directory, String direcFuente){
		if(directory.isDirectory()){
			//File sourceDirectory = null; //new File("C:/logt");
			try {
				//sourceDirectory = 
				aperturarConexion(direcFuente, directory.getPath().replace("\\", "/"));
				//copyDirectory(sourceDirectory, directory);
			} catch (Exception e) {
				 
				e.printStackTrace();
			}
		}
	}*/
	
	public void copyDirectory(File sourceLocation , File targetLocation) throws IOException {		        
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
	
	public void aperturarConexion(String idProgramaApp, File filesaveable) throws Exception{
		
		URL servletURL = new URL( getCodeBase().toString()+ "GeneradorReporteP05Excel" );
		//URL servletURL = new URL( "http://localhost:9080/ProgramaFinanciero/GeneradorReporteP05Excel" );
				
		URLConnection urlConnection = servletURL.openConnection();
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(true);
		urlConnection.setUseCaches(false);
		

		ObjectOutputStream objOut = new ObjectOutputStream (urlConnection.getOutputStream());
		objOut.writeObject(idProgramaApp); // enviando el id de programa a bajar		
		objOut.flush();
		objOut.close();
		
		ObjectInputStream objIn = new ObjectInputStream(urlConnection.getInputStream());		
		/*String code = (String)objIn.readObject();
		objIn.close();
		System.out.println("codigo: " + code);*/
		byte[] arregloExcel = (byte[])objIn.readObject();
		//byte[] arregloZip = (byte[])objIn.readObject();
		//ZipInputStream zinst = (ZipInputStream) objIn.readObject(); // leyendo el Object Servlet
		//ZipInputStream zinst = new ZipInputStream(ins);
		//ZipInputStream zinst = new ZipInputStream(new ByteArrayInputStream(arregloZip));
		//unzip(zinst, new File(targetDirectory));
		
		FileOutputStream fos = new FileOutputStream(filesaveable);
		fos.write(arregloExcel);
		fos.flush();
		fos.close();
		
		byte[] arregloZip = (byte[])objIn.readObject();
		//ZipInputStream zinst = (ZipInputStream) objIn.readObject(); // leyendo el Object Servlet
		//ZipInputStream zinst = new ZipInputStream(ins);
		System.out.println(arregloZip);
		ZipInputStream zinst = new ZipInputStream(new ByteArrayInputStream(arregloZip));
		unzip(zinst, filesaveable.getParentFile());
		
		objIn.close();
		
		try {
			System.out.println("cmd /c \""+filesaveable.getParentFile()+"\\archivostmp\\patchFile.vbs\" " + filesaveable.getAbsolutePath()+"");
			Runtime.getRuntime().exec("cmd /c \""+filesaveable.getParentFile()+"\\archivostmp\\patchFile.vbs\" " + filesaveable.getAbsolutePath()+"");
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	public void destroy(){
		System.out.println("metodo destroy");
	}
	
	public static File unzip(ZipInputStream  in, File outFolder){    
        try{
             BufferedOutputStream out = null;
             /*ZipInputStream  in = new ZipInputStream(
                                           new BufferedInputStream(
                                                new FileInputStream(inFile)));*/
             //creando la carpeta
             File file = new File(outFolder.getAbsolutePath() +File.separator+ "archivostmp");
     			if(!file.exists()){
     				file.mkdir();
     		 }
             
             ZipEntry entry;
             while((entry = in.getNextEntry()) != null){
            	 
            	/*
          	   if(entry.getName().contains(File.separator) || entry.getName().contains(BACKSLASH)){
          		   crearDirectorios(outFolder.getPath(), entry.getName());
          	   }*/
          	   int count;
                 byte data[] = new byte[BUFFER];
                 String newname = "";
                 newname = cleanName(entry.getName());
                 System.out.println("descompimiendo archivo: " + newname);
                  out = new BufferedOutputStream(
                            new FileOutputStream(file.getPath() + File.separator + newname),BUFFER);
                  while ((count = in.read(data,0,BUFFER)) != -1){
                       out.write(data,0,count);
                  }
                  cleanUp(out);
             }
             cleanUp(in);
             return outFolder;
        }catch(Exception e){
             e.printStackTrace();
             return null;
        }
   }
	public static String cleanName(String name){
		String newname = name;
		newname = newname.replace(':', '_');
		newname = newname.replace('*', '_');
		newname = newname.replace('?', '_');
		newname = newname.replace('"', '_');
		newname = newname.replace('<', '_');
		newname = newname.replace('>', '_');
		newname = newname.replace('|', '_');
		if(newname.contains(File.separator)){
			newname = newname.substring(newname.lastIndexOf(File.separator)+1);
		}
		if(newname.contains(BACKSLASH)){
			newname = newname.substring(newname.lastIndexOf(BACKSLASH)+1);
   	    }
		return newname;
	}
	/*
	public static void main(String args[]){
		try{
		String strdemo = "asdfasfasfasdfdd";
		System.out.println(cleanName(strdemo));
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
	public static void crearDirectorios(String strBase, String strNameFile){
    	if(strNameFile.contains(File.separator)){
    		int val = strNameFile.indexOf(File.separator);
    		String strdirecto = strNameFile.substring(0,val);
    		File file = new File(strBase +File.separator+ strdirecto);
    		if(!file.exists()){
    			file.mkdir();
    		}
    		crearDirectorios(file.getAbsolutePath(), strNameFile.substring(val+1));
    	}else if(strNameFile.contains(BACKSLASH)){
    		int val = strNameFile.indexOf(BACKSLASH);
    		String strdirecto = strNameFile.substring(0,val);
    		File file = new File(strBase +BACKSLASH+ strdirecto);
    		if(!file.exists()){
    			file.mkdir();
    		}
    		crearDirectorios(file.getAbsolutePath(), strNameFile.substring(val+1));
    	}
    }
	
	private static void cleanUp(InputStream in) throws Exception{
		in.close();
	}
   
	private static void cleanUp(OutputStream out) throws Exception{
        out.flush();
        out.close();
	}
	/*
	public static void main(String []args){
		try {
			//(new DescargaDirectorioApplet()).aperturarConexion("dato1", "dato2");
			(new DescargaDirectorioApplet()).start();
		} catch (HeadlessException e) {			
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}*/
}

class ExtensionFileFilter extends FileFilter {
	  String description;

	  String extensions[];

	  public ExtensionFileFilter(String description, String extension) {
	    this(description, new String[] { extension });
	  }

	  public ExtensionFileFilter(String description, String extensions[]) {
	    if (description == null) {
	      this.description = extensions[0];
	    } else {
	      this.description = description;
	    }
	    this.extensions = (String[]) extensions.clone();
	    toLower(this.extensions);
	  }

	  private void toLower(String array[]) {
	    for (int i = 0, n = array.length; i < n; i++) {
	      array[i] = array[i].toLowerCase();
	    }
	  }

	  public String getDescription() {
	    return description;
	  }

	  public boolean accept(File file) {
	    if (file.isDirectory()) {
	      return true;
	    } else {
	      String path = file.getAbsolutePath().toLowerCase();
	      for (int i = 0, n = extensions.length; i < n; i++) {
	        String extension = extensions[i];
	        if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
	          return true;
	        }
	      }
	    }
	    return false;
	  }
	}
