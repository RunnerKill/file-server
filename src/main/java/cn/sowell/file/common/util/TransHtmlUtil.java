package cn.sowell.file.common.util;

import java.io.File;
import java.util.List;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class TransHtmlUtil {

    private static TransHtmlUtil transHtmlUtil = null;

    private TransHtmlUtil() {
    }

    public static TransHtmlUtil getInstance() {
        if(transHtmlUtil == null) {
            transHtmlUtil = new TransHtmlUtil();
        }
        return transHtmlUtil;
    }

    private int WORD_HTML = 8;

    private ActiveXComponent app = null;

    private static final String HTML_TYPE = "html";

    /**
     * WORD转HTML
     * @param filepath WORD文件全路径
     * @param htmlfile 转换后HTML存放路径
     */
    public void transToHtml(String filepath, String htmlpath) {
        createdHtmlDir(htmlpath);
        ComThread.InitSTA();
        // 启动word
        app = new ActiveXComponent("Word.Application");
        try {
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();
            Dispatch doc =
                Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[]{filepath, new Variant(false), new Variant(true)},
                    new int[1]).toDispatch();
            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[]{htmlpath, new Variant(WORD_HTML)}, new int[1]);
            Variant f = new Variant(false);
            Dispatch.call(doc, "Close", f);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            app.invoke("Quit", new Variant[]{});
            ComThread.Release();
        }
    }

    /**
     * 把Excel转成html
     * @param xlsfile
     * @param htmlfile
     */
    public void excelToHtml(String xlsfile, String htmlfile) {
        createdHtmlDir(htmlfile);
        ActiveXComponent app = new ActiveXComponent("Excel.Application"); // 启动Excel
        try {
            app.setProperty("Visible", new Variant(false));
            Dispatch excels = app.getProperty("Workbooks").toDispatch();
            Dispatch excel =
                Dispatch.invoke(excels, "Open", Dispatch.Method, new Object[]{xlsfile, new Variant(false), new Variant(true)},
                    new int[1]).toDispatch();
            Dispatch.invoke(excel, "SaveAs", Dispatch.Method, new Object[]{htmlfile, new Variant(44)}, new int[1]);
            Variant f = new Variant(false);
            Dispatch.call(excel, "Close", f);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            app.invoke("Quit", new Variant[]{});
        }

    }

    /**
     * 两WORD合并转HTML
     * @param filepath WORD文件全路径
     * @param htmlfile 转换后HTML存放路径
     */
    public void transToHtml(List<String> filepath, String htmlpath) {
        createdHtmlDir(htmlpath);
        // 启动word
        app = new ActiveXComponent("Word.Application");
        try {
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();
            Dispatch doc =
                Dispatch.invoke(docs, "Open", Dispatch.Method,
                    new Object[]{filepath.get(0), new Variant(false), new Variant(true)}, new int[1]).toDispatch();
            for(int i = 1; i < filepath.size(); i++) {

                Dispatch.invoke(app.getProperty("Selection").toDispatch(), "insertFile", Dispatch.Method, new Object[]{
                    (String)filepath.get(i), "", new Variant(false), new Variant(false), new Variant(false)}, new int[1]);
            }
            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[]{htmlpath, new Variant(WORD_HTML)}, new int[1]);
            Variant f = new Variant(false);
            Dispatch.call(doc, "Close", f);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            app.invoke("Quit", new Variant[]{});
        }
    }

    /**
     * 生成html文件名称
     */
    public static String getHtmlName(String filename) {
        return filename + "." + HTML_TYPE;
    }

    /**
     * 生成html文件夹
     */
    private void createdHtmlDir(String htmlpath) {
        if(htmlpath != null) {
            String dir = htmlpath.substring(0, htmlpath.lastIndexOf("/"));
            File htmldir = new File(dir);
            if(!htmldir.exists()) {
                htmldir.mkdirs();
            }
        }
    }
}
