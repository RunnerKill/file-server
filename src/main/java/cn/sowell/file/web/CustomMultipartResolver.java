package cn.sowell.file.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 自定义Spring mvc文件请求处理器
 * @author Xiaojie.Xu
 */
public class CustomMultipartResolver extends CommonsMultipartResolver {

    private HttpServletRequest request;

    @Override
    protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
        FileUpload upload = super.newFileUpload(fileItemFactory);
        // upload.setSizeMax(-1);
        if(request != null) {
            upload.setProgressListener(new CustomProgressListener(request.getQueryString()));
        }
        return upload;
    }

    @Override
    public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
        this.request = request;
        return super.resolveMultipart(request);
    }

    @Override
    public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = determineEncoding(request);
        FileUpload fileUpload = prepareFileUpload(encoding);
        fileUpload.setProgressListener(new CustomProgressListener(request.getQueryString()));
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            return parseFileItems(fileItems, encoding);
        }
        catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        }
        catch (FileUploadException ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }

}
