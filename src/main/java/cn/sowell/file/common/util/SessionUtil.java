package cn.sowell.file.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import cn.sowell.file.modules.model.File;

/**
 * web连接缓存至session中，两个list分别存储add和edit时的数据 先测试static 用web测试
 * @author Xiaojie.Xu
 */
@SuppressWarnings("unchecked")
public class SessionUtil {

    private static final String SESSION_KEY_LIST = "session_key_list";

    private static SessionUtil sessionUtil = new SessionUtil();

    private SessionUtil() {
    }

    public static SessionUtil getInstance() {
        return sessionUtil;
    }

    public File get(HttpSession session, String id) {
        List<File> list = get(session);
        for(File f: list) {
            if(f.getId().equals(id))
                return f;
        }
        return null;
    }

    public List<File> get(HttpSession session) {
        Object obj = session.getAttribute(SESSION_KEY_LIST);
        if(obj == null)
            session.setAttribute(SESSION_KEY_LIST, new ArrayList<File>());
        return (List<File>)session.getAttribute(SESSION_KEY_LIST);
    }

    public boolean put(HttpSession session, File file) {
        return get(session).add(file);
    }

    public boolean put(HttpSession session, List<File> files) {
        if(files == null || files.size() < 1)
            return false;
        return get(session).addAll(files);
    }

    public File remove(HttpSession session, String id) {
        List<File> list = get(session);
        Iterator<File> it = list.iterator();
        while(it.hasNext()) {
            File file = it.next();
            if(file.getId().equals(id)) {
                it.remove();
                return file;
            }
        }
        return null;
    }

    public void clear(HttpSession session) {
        session.removeAttribute(SESSION_KEY_LIST);
    }

}
