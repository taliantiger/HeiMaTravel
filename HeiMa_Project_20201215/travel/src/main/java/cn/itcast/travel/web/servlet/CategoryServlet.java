package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {

    private CategoryService service = new CategoryServiceImp()   ;

    /**
     * 查询所有
     */
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         // 1.调用service 查询所有
        List<Category> cs = service.findAll();

//        // 2.序列号json返回
//        ObjectMapper mapper = new ObjectMapper() ;
//
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(),cs);

        // 可以直接调用父类BaseServlet 中的方法
        writeValue(cs,response);
    }

}
