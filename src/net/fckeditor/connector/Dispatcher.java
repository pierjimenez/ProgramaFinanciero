package net.fckeditor.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import net.fckeditor.connector.exception.FolderAlreadyExistsException;
import net.fckeditor.connector.exception.InvalidCurrentFolderException;
import net.fckeditor.connector.exception.InvalidNewFolderNameException;
import net.fckeditor.connector.exception.ReadException;
import net.fckeditor.connector.exception.WriteException;
import net.fckeditor.handlers.Command;
import net.fckeditor.handlers.PropertiesLoader;
import net.fckeditor.handlers.RequestCycleHandler;
import net.fckeditor.handlers.ResourceType;
import net.fckeditor.requestcycle.Context;
import net.fckeditor.requestcycle.ThreadLocalData;
import net.fckeditor.response.GetResponse;
import net.fckeditor.response.UploadResponse;
import net.fckeditor.tool.Utils;
import net.fckeditor.tool.UtilsFile;
import net.fckeditor.tool.UtilsResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dispatcher
{
  private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
  private Connector connector;

  Dispatcher(ServletContext servletContext)
    throws Exception
  {
    String className = PropertiesLoader.getConnectorImpl();
    if (Utils.isEmpty(className))
      logger.error("Empty Connector implementation class name provided");
    else
      try {
        Class clazz = Class.forName(className);
        this.connector = ((Connector)clazz.newInstance());
        logger.info("Connector initialized to {}", className);
      } catch (Throwable e) {
        logger.error("Connector implementation {} could not be instantiated", className);
        throw new RuntimeException("Connector implementation " + className + " could not be instantiated", e);
      }

    this.connector.init(servletContext);
  }

  GetResponse doGet(HttpServletRequest request)
  {
    logger.debug("Entering Dispatcher#doGet");

    Context context = ThreadLocalData.getContext();
    context.logBaseParameters();

    GetResponse getResponse = null;

    if (!(Command.isValidForGet(context.getCommandStr()))) {
      getResponse = GetResponse.getInvalidCommandError();
    } else if (!(ResourceType.isValidType(context.getTypeStr()))) {
      getResponse = GetResponse.getInvalidResourceTypeError();
    } else if (!(UtilsFile.isValidPath(context.getCurrentFolderStr()))) {
      getResponse = GetResponse.getInvalidCurrentFolderError();
    }
    else
    {
      ResourceType type = context.getResourceType();
      Command command = context.getCommand();

      if ((((command.equals(Command.GET_FOLDERS)) || (command.equals(Command.GET_FOLDERS_AND_FILES)))) && (!(RequestCycleHandler.isGetResourcesEnabled(request))))
      {
        getResponse = GetResponse.getGetResourcesDisabledError();
      } else if ((command.equals(Command.CREATE_FOLDER)) && (!(RequestCycleHandler.isCreateFolderEnabled(request)))) {
        getResponse = GetResponse.getCreateFolderDisabledError();
      }
      else
        try
        {
          if (command.equals(Command.CREATE_FOLDER)) {
            String newFolderNameStr = request.getParameter("NewFolderName");

            logger.debug("Parameter NewFolderName: {}", newFolderNameStr);

            String sanitizedNewFolderNameStr = UtilsFile.sanitizeFolderName(newFolderNameStr);

            if (Utils.isEmpty(sanitizedNewFolderNameStr)) {
              getResponse = GetResponse.getInvalidNewFolderNameError();
            }
            else {
              logger.debug("Parameter NewFolderName (sanitized): {}", sanitizedNewFolderNameStr);

              this.connector.createFolder(type, context.getCurrentFolderStr(), sanitizedNewFolderNameStr);

              getResponse = GetResponse.getOK();
            }
          } else if ((command.equals(Command.GET_FOLDERS)) || (command.equals(Command.GET_FOLDERS_AND_FILES)))
          {
            String url = UtilsResponse.getUrl(RequestCycleHandler.getUserFilesPath(request), type, context.getCurrentFolderStr());

            getResponse = getFoldersAndOrFiles(command, type, context.getCurrentFolderStr(), url);
          }
        }
        catch (InvalidCurrentFolderException e) {
          getResponse = GetResponse.getInvalidCurrentFolderError();
        } catch (InvalidNewFolderNameException e) {
          getResponse = GetResponse.getInvalidNewFolderNameError();
        } catch (FolderAlreadyExistsException e) {
          getResponse = GetResponse.getFolderAlreadyExistsError();
        } catch (WriteException e) {
          getResponse = GetResponse.getCreateFolderWriteError();
        } catch (ReadException e) {
          getResponse = GetResponse.getGetResourcesReadError();
        }

    }

    logger.debug("Exiting Dispatcher#doGet");
    return getResponse;
  }

  private GetResponse getFoldersAndOrFiles(Command command, ResourceType type, String currentFolder, String constructedUrl)
    throws InvalidCurrentFolderException, ReadException
  {
    GetResponse getResponse = new GetResponse(command, type, currentFolder, constructedUrl);

    getResponse.setFolders(this.connector.getFolders(type, currentFolder));
    if (command.equals(Command.GET_FOLDERS_AND_FILES))
      getResponse.setFiles(this.connector.getFiles(type, currentFolder));
    return getResponse;
  }

  UploadResponse doPost(HttpServletRequest request)
  {
    logger.debug("Entering Dispatcher#doPost");

    Context context = ThreadLocalData.getContext();
    context.logBaseParameters();

    UploadResponse uploadResponse = null;

    if (!(RequestCycleHandler.isFileUploadEnabled(request))) {
      uploadResponse = UploadResponse.getFileUploadDisabledError();
    }
    else if (!(Command.isValidForPost(context.getCommandStr()))) {
      uploadResponse = UploadResponse.getInvalidCommandError();
    } else if (!(ResourceType.isValidType(context.getTypeStr()))) {
      uploadResponse = UploadResponse.getInvalidResourceTypeError();
    } else if (!(UtilsFile.isValidPath(context.getCurrentFolderStr()))) {
      uploadResponse = UploadResponse.getInvalidCurrentFolderError();
    }
    else
    {
      ResourceType type = context.getDefaultResourceType();
      FileItemFactory factory = new DiskFileItemFactory();
      ServletFileUpload upload = new ServletFileUpload(factory);
      try {
        List items = upload.parseRequest(request);

        FileItem uplFile = (FileItem)items.get(0);

        String fileName = FilenameUtils.getName(uplFile.getName());
        logger.debug("Parameter NewFile: {}", fileName);

        if (type.isDeniedExtension(FilenameUtils.getExtension(fileName))) {
          uploadResponse = UploadResponse.getInvalidFileTypeError();
        }
        else if ((type.equals(ResourceType.IMAGE)) && (PropertiesLoader.isSecureImageUploads()) && (!(UtilsFile.isImage(uplFile.getInputStream()))))
        {
          uploadResponse = UploadResponse.getInvalidFileTypeError();
        } else {
          String sanitizedFileName = UtilsFile.sanitizeFileName(fileName);

          logger.debug("Parameter NewFile (sanitized): {}", sanitizedFileName);

          String newFileName = this.connector.fileUpload(type, context.getCurrentFolderStr(), sanitizedFileName, uplFile.getInputStream());

          String fileUrl = UtilsResponse.fileUrl(RequestCycleHandler.getUserFilesPath(request), type, context.getCurrentFolderStr(), newFileName);

          int i=0;
          InputStream is=null;
          BufferedReader reader=null;
         while(i++<10){
	         try{
	        	 
	  			URL uri=new URL(fileUrl);
	  			 is = uri.openConnection().getInputStream();
	  			 reader = new BufferedReader( new InputStreamReader( is )  );
	  			String line = null;
			    while( ( line = reader.readLine() ) != null )  {
			       line=line;
			    }
			    reader.close();
			    i=10;
	  		}catch (Exception e) {}
	         finally{
	        	 if(reader!=null){reader.close();}
	        	 if(is!=null){is.close();}	        	 
	         }
         }
          
          
          if (sanitizedFileName.equals(newFileName)) {
            uploadResponse = UploadResponse.getOK(fileUrl);
          } else {
            uploadResponse = UploadResponse.getFileRenamedWarning(fileUrl, newFileName);
            logger.debug("Parameter NewFile (renamed): {}", newFileName);
          }

        }

        uplFile.delete();
      } catch (InvalidCurrentFolderException e) {
        uploadResponse = UploadResponse.getInvalidCurrentFolderError();
      } catch (WriteException e) {
        uploadResponse = UploadResponse.getFileUploadWriteError();
      } catch (IOException e) {
        uploadResponse = UploadResponse.getFileUploadWriteError();
      } catch (FileUploadException e) {
        uploadResponse = UploadResponse.getFileUploadWriteError();
      }
    }

    logger.debug("Exiting Dispatcher#doPost");
    return uploadResponse;
  }
}