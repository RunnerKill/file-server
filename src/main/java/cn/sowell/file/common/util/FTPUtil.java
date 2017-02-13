package cn.sowell.file.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public class FTPUtil {

    private static Logger logger = Logger.getLogger(FTPUtil.class);

    private static int DEAFULT_REMOTE_PORT = 21;

    private static String DEAFULT_REMOTE_CHARSET = "GBK";

    // private static String DEAFULT_LOCAL_CHARSET = "iso-8859-1";

    private FTPClient ftpClient;

    private String host;

    private int port;

    private String username;

    private String password;

    public FTPUtil(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.ftpClient = new FTPClient();
    }

    public boolean login() {
        boolean result = false;
        FTPClientConfig ftpClientConfig = new FTPClientConfig();
        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
        ftpClient.configure(ftpClientConfig);
        // 设置超时时间
        ftpClient.setDataTimeout(7200);
        // 设置默认编码
        ftpClient.setControlEncoding(DEAFULT_REMOTE_CHARSET);
        // 设置默认端口
        ftpClient.setDefaultPort(DEAFULT_REMOTE_PORT);
        try {
            if(port > 0) {
                ftpClient.connect(host, port);
            } else {
                ftpClient.connect(host);
            }
            // FTP服务器连接回答
            int reply = ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.error("connect failed!");
                return result;
            }
            if(!ftpClient.login(username, password)) {
                ftpClient.quit();
                ftpClient.disconnect();
                logger.error("login failed!");
                return result;
            }
            // 设置传输协议
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            logger.info("login success!(username: " + username + ")");
            result = true;
        } catch(IOException e) {
            e.printStackTrace();
            logger.error("login error!(username: " + username + ")" + e, e);
        }
        ftpClient.setBufferSize(1024 * 2);
        ftpClient.setDataTimeout(30 * 1000);
        return result;
    }

    public boolean logout() {
        boolean result = false;
        if(ftpClient != null && ftpClient.isConnected()) {
            try {
                if(ftpClient.logout()) {
                    logger.info("logout success!(username: " + username + ")");
                    result = true;
                }
            } catch(IOException e) {
                e.printStackTrace();
                logger.error("logout error!(username: " + username + ")" + e, e);
            } finally {
                try {
                    ftpClient.disconnect();// 关闭FTP服务器的连接
                } catch(IOException e) {
                    e.printStackTrace();
                    logger.error("disconnect error!" + e, e);
                }
            }
        }
        return result;
    }

    public boolean uploadFile(InputStream inStream, String remoteDir, String fileName) {
        boolean result = false;
        if(!StringUtils.hasText(fileName)) {
            fileName = "no_name_" + UUID.randomUUID().toString();
        }
        try {
            ftpClient.changeWorkingDirectory(remoteDir);// 改变工作路径
            logger.info("start upload...");
            if(ftpClient.storeFile(fileName, inStream)) {
                logger.info("upload success!(filename: " + fileName + ")");
                result = true;
            }
        } catch(IOException e) {
            e.printStackTrace();
            logger.error("upload error!" + e, e);
        } finally {
            if(inStream != null) {
                try {
                    inStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                    logger.error("input stream close error!" + e, e);
                }
            }
        }
        return result;
    }

    public boolean downloadFile(String localDir, String remoteDir, String filename) {
        String strFilePath = localDir + filename;
        BufferedOutputStream outStream = null;
        boolean success = false;
        try {
            ftpClient.changeWorkingDirectory(remoteDir);
            outStream = new BufferedOutputStream(new FileOutputStream(strFilePath));
            logger.info(filename + "开始下载....");
            success = ftpClient.retrieveFile(filename, outStream);
            if(success == true) {
                logger.info(filename + "成功下载到" + strFilePath);
                return success;
            }
        } catch(IOException e) {
            e.printStackTrace();
            logger.error(filename + "下载失败");
        } finally {
            if(null != outStream) {
                try {
                    outStream.flush();
                    outStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(success == false) {
            logger.error(filename + "下载失败!!!");
        }
        return success;
    }

    public boolean uploadDir(String localDir, String remoteDir) {
        File dir = new File(localDir);
        try {
            remoteDir = remoteDir + dir.getName() + "/";
            ftpClient.makeDirectory(remoteDir);
        } catch(IOException e) {
            e.printStackTrace();
            logger.info(remoteDir + "目录创建失败");
        }
        File[] files = dir.listFiles();
        for(int i = 0; i < files.length; i++) {
            File file = files[i];
            if(!files[i].isDirectory()) {
                InputStream in;
                try {
                    in = new FileInputStream(file);
                    uploadFile(in, remoteDir, file.getName());
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                uploadDir(file.getPath(), remoteDir);
            }
        }
        return true;
    }

    public boolean downLoadDir(String localDir, String remoteDir) {
        try {
            String fileName = new File(remoteDir).getName();
            localDir = localDir + fileName + "/";
            new File(localDir).mkdirs();
            FTPFile[] files = ftpClient.listFiles(remoteDir);
            for(int i = 0; i < files.length; i++) {
                FTPFile file = files[i];
                if(!file.isDirectory()) {
                    downloadFile(file.getName(), localDir, remoteDir);
                } else {
                    String deepDir = remoteDir + "/" + file.getName();
                    downLoadDir(localDir, deepDir);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
            logger.info("下载文件夹失败");
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        FTPUtil ftp = new FTPUtil("192.168.1.111", 21, "test", "123456");
        ftp.login();
        // 上传文件夹
        ftp.uploadDir("D:\\test\\2013", "/2013");
        // 下载文件夹
        ftp.downLoadDir("d://tmp//", "/home/data/DataProtemp");
        ftp.logout();
    }
}
