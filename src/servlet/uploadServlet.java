package servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 刘芋池
 * @Description
 * @create 2020/12/23 20:00
 */
@WebServlet("/registerServlet")
public class uploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*//以输入流的形式获取multipart请求的请求体内容
        ServletInputStream is = request.getInputStream();
        PrintWriter out = response.getWriter();
        int len=-1;
        byte[] buffer=new byte[1024];
        while((len=is.read(buffer))!=-1){
            String str=new String(buffer,0,len);
            out.print(str);
        }
        is.close();*/
        //1.判断请求是否为multipart类
        Integer i=1;
        if(!ServletFileUpload.isMultipartContent(request)){
            throw new RuntimeException("当前请求不支持文件上传!");
        }
        try{
            //创建一个FileItem工厂
            DiskFileItemFactory factory=new DiskFileItemFactory();
            //设置临时文件的边界值，大于该值，上传文件会先保存在临时文件，否则，上传文件将直接写入内存，单位：字节
            factory.setSizeThreshold(1024*1024*1);
            //设置临时文件
            String tempPath =getServletContext().getRealPath("/temp");
            File temFile= new File(tempPath);
            factory.setRepository(temFile);
            //创建文件上传核心组件
            ServletFileUpload upload=new ServletFileUpload(factory);
            //设置每一个item的头部字符编码，其可以解决文件名的中文乱码问题
            upload.setHeaderEncoding("utf-8");
            //设置单个上传文件的最大边界值
            upload.setFileSizeMax(1024*1024*2);
            //设置一次上传，所有文件的总和最大值为5MB（对一次上传多个文件）
            upload.setSizeMax(1024*1024*5);
            //解析请求，获取所有的item
            List<FileItem> items = upload.parseRequest(request);
            //遍历items
            for (FileItem item : items) {
                if(item.isFormField()){   //若item为普通表单
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("utf-8");
                    System.out.println(fieldName+"="+fieldValue);
                }else{
                    String fileName = item.getName();
                    request.setAttribute("filename",fileName);

                    //防止同名覆盖
                    fileName=System.currentTimeMillis()+fileName;
                    //获取输入流，其中有上传文件的内容
                    InputStream is = item.getInputStream();
                    //获取保存在服务器的路径
                    String path = getServletContext().getRealPath("/images");
                    //获取当前时间
                    Calendar now = Calendar.getInstance();
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH)+1;
                    int day = now.get(Calendar.DAY_OF_MONTH);


                   /* Date date =new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String now=sdf.format(date);
*/
                    path=path+"/"+year+"/"+month+"/"+day;
                    //若该目录不存在，则创建这个目录
                    File dirFile=new File(path);
                    if(!dirFile.exists()){
                        dirFile.mkdirs();
                    }
                    //创建目标文件，用于保存上传的文件
                    File descFile=new File(path,fileName);
                    if(!descFile.exists()){
                        descFile.getParentFile().mkdirs();
                        descFile.createNewFile();
                    }
                    //创建文件输出流
                    OutputStream os=new FileOutputStream(descFile);
                    //将输入流中的数据写到输出流中去
                    int len=-1;
                    byte[] buffer=new byte[1024];
                    while((len=is.read(buffer))!=-1){
                        os.write(buffer,0,len);
                    }
                    //关闭流
                    os.close();
                    is.close();
                    //删除临时文件
                    item.delete();
                }
            }

        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
