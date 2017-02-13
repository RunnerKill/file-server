package cn.sowell.file.web.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sowell.file.web.controller.BaseController;

/**
 * Controller层统一异常处理
 * @author Xiaojie.Xu
 */
@ControllerAdvice
public class ControllerExceptionHandler extends BaseController {

    private static final Logger logger = Logger.getLogger(ControllerExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleIOException(Exception ex) {
        ex.printStackTrace();
        logger.error(getStackTrace(ex));
        return error(ex.toString());
    }

    /**
     * 完整的堆栈信息
     * @param e Exception
     * @return Full StackTrace
     */
    private static String getStackTrace(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if(sw != null) {
                try {
                    sw.close();
                } catch(IOException e1) {
                    e1.printStackTrace();
                }
            }
            if(pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }

}
