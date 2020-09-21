package cn.tedu.fileupload;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class FileuploadController {
    private List<String> contentTyoes = new ArrayList<>();
    private long maxFileSize = 20 * 1024 * 1024;

    {
        contentTyoes.add("image/jpeg");
        contentTyoes.add("image/png");
        contentTyoes.add("image/bmp");
    }
    //  http://localhost:8080/upload
    @PostMapping("/upload")
    public String upload(MultipartFile file) throws Exception{
        //判断上传文件是否为空
        boolean isEmpty = file.isEmpty();
        if (isEmpty) {
            return "空文件上传个鬼";
        }
        //获取上传文件的大小是否超标
        long size = file.getSize();
        System.err.println("size:" + size);
        if (size > maxFileSize) {
            return "文件上传失败,文件大小超出了限制";
        }
        //检查上传的文件类型是否正确,允许:image/jpeg,image/png,image/bmp
        String contentType = file.getContentType();
        System.err.println("contentType:" +contentType);

        if (!contentType.contains(contentType)) {
            return "文件格式不正确你上传个鬼";
        }
        //确定上传的文件保存到哪个文件夹
        //注意:以下文件夹是已经在application.properties中配置为静态资源文件夹的
        String parent = "F:/static-resource";
        //确定上传的文件保存时使用的文件名
        String filename = System.currentTimeMillis() + "-" + System.nanoTime();
        //String filename = UUID.randomUUID().toString();
        //获取上传文件的全名
        String originalFilename = file.getOriginalFilename();
        System.err.println("originalFilename" + originalFilename);
        //确定文件上传保存时使用的扩展名
        String suffix = "";
        int beginIndex = originalFilename.lastIndexOf(".");
        if (beginIndex > 0) {
            suffix = originalFilename.substring(beginIndex);
        }
        //确定文件上传时保存的文件全名
        String child = filename + suffix;
        //确定上传的文件保存到哪里
        File dest = new File(parent,child);
        //执行保存文件,将客户端上传的文件保存到服务器端的dest位置
        file.transferTo(dest);
        //响应给服务器端
        return "OK";
    }
}
