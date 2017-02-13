package cn.sowell.file;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;

import cn.sowell.file.modules.model.Excel;
import cn.sowell.file.modules.model.File;
import cn.sowell.file.modules.model.Image;
import cn.sowell.file.modules.model.Word;
import cn.sowell.file.modules.service.FileService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:src/main/resources/spring-config.xml"})
public class FileServiceTest {
    
    private static final Gson gson = new Gson();

    @Autowired
    private FileService fs;
    
    @Before
    public void testInsert() throws Exception {
        File f = new File();
        f.setId("xxj");
        f.setName("file");
        f.setExt("jpg");
        f.setFtype("image");
        f.setPath("ad/sasad.jpg");
        f.setProject("wsp");
        f.setModule("test");
        f.setSize("12KB");
        f.setUploadTime(System.currentTimeMillis());
        Image image = new Image(f);
        image.setWidth(123);
        image.setHeight(22);
        image.setOrientation("adasdas");
        fs.insert(image);
    }
    
    @Before
    public void testInsertMultiply() throws Exception {
        List<File> files = new ArrayList<File>();
        for(int i = 0; i < 3; i ++) {
            File f = new File();
            f.setId("x-" + i);
            f.setName("file");
            f.setPath("ad/sasad");
            f.setProject("wsp");
            f.setModule("test");
            f.setSize("12KB");
            f.setUploadTime(System.currentTimeMillis());
            files.add(f);
        }
        files.get(0).setExt("jpg");
        Image image = new Image(files.get(0));
        image.setWidth(123);
        image.setHeight(22);
        image.setOrientation("adasdas");
        files.get(1).setExt("doc");
        Word word = new Word(files.get(1));
        word.setPreviewPath("sdasd/asd/sa/d.html");
        word.setVersion("2003");
        files.get(2).setExt("xls");
        Excel excel = new Excel(files.get(2));
        excel.setPreviewPath("sa/d/as/d/f/html");
        excel.setVersion("2007");
        files.clear();
        files.add(image);
        files.add(word);
        files.add(excel);
        fs.insert(files);
    }
    
    @After
    public void testDelete() throws Exception {
        int num = fs.delete("xxj", null);
        System.out.println(num);
    }
    
    @After
    public void testDeleteMultiply() throws Exception {
        String[] ids = new String[]{"x-0","x-1","x-2"};
        int num = fs.delete(ids, null);
        System.out.println(num);
    }
    
    @Test
    public void testFindById() throws Exception {
        File f = fs.findById("xxj");
        System.out.println(gson.toJson(f));
    }
    
    @Test
    public void testListByIds() throws Exception {
        String[] ids = new String[]{"x-0","x-1","x-2"};
        List<File> files = fs.listByIds(ids);
        System.out.println(files);
        System.out.println(gson.toJson(files));
    }
    
}
