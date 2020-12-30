package servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 刘芋池
 * @Description
 * @create 2020/12/24 18:44
 */
@WebServlet("/downloadServlet")
public class downloadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String filename="照片.jpg";
        //打散:以当前字符编码进行打散
        byte[] bytes = filename.getBytes("UTF-8");
        //组装：按照目标字符编码进行组装
        filename=new String(bytes,"ISO8859-1");
        response.setHeader("content-disposition","attachment;filename="+filename);
        InputStream is = getServletContext().getResourceAsStream("/images/照片.jpg");
        ServletOutputStream os = response.getOutputStream();
        int len=-1;
        byte[] buffer=new byte[1024];
        while((len=is.read(buffer))!=-1){
            os.write(buffer,0,len);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
