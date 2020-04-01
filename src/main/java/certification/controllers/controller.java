package certification.controllers;

import certification.core.Source;
import certification.core.query.PathQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
public class controller {
    @Resource
    private Source source = new Source();

    @RequestMapping("/")
    public String getHome(Model model){
        return "index";
    }

    /**
     *上传文件 ,用@RequestParam注解来指定表单上的file为MultipartFile
     */
    @RequestMapping(value = "/keyUpload", method = RequestMethod.POST)
    @ResponseBody
    public String handleFormUpload(@RequestParam MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            // store the bytes somewhere
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);
            String path = PathQuery.democaPath+PathQuery.privatePath+fileName;
            File fileWriteTo = new File(path);
            if(!fileWriteTo.exists()) {
                fileWriteTo.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(fileWriteTo);
            out.write(bytes);
            out.close();
            Source.pkPathList.add(path);
            Source.pkListLastSize++;
            return "redirect:requestDispose.html";
        }

        return "redirect:uploadFailure.html";
    }


    @RequestMapping(value = "/gencer",method = RequestMethod.GET)
    @ResponseBody
    public String genCerHandle(HttpServletRequest request){
        String info =  request.getQueryString();
        System.out.println(info);
        return "redirect:upload.jsp";
    }
}
