import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class TestFtl {

    public static void main(String[] args) {
        Configuration conf = new Configuration(Configuration.getVersion());
        try {
            conf.setDirectoryForTemplateLoading(new File("E:\\work\\dongyimai\\demo_project\\demo_freemarker\\src\\main\\resources"));
            conf.setDefaultEncoding("utf-8");
            Template template = conf.getTemplate("test.ftl");
            Map<String,Object> map = new HashMap<>();
            map.put("name","学员");
            map.put("message","欢迎来到王者荣耀");

            List goodsList=new ArrayList();
            Map goods1=new HashMap();
            goods1.put("name", "苹果");
            goods1.put("price", 5.8);
            Map goods2=new HashMap();
            goods2.put("name", "香蕉");
            goods2.put("price", 2.5);
            Map goods3=new HashMap();
            goods3.put("name", "橘子");
            goods3.put("price", 3.2);
            goodsList.add(goods1);
            goodsList.add(goods2);
            goodsList.add(goods3);
            map.put("goodsList", goodsList);

            map.put("today", new Date());

            map.put("money",133333330);
            map.put("aaa","vvv");



            FileWriter out = new FileWriter(new File("E:\\item\\demo.html"));
            template.process(map,out);
            System.out.println("生成成功");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
