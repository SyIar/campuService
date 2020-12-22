package cn.edu.fudan.campuservice.controller;

import cn.edu.fudan.campuservice.annotation.ParamCheck;
import cn.edu.fudan.campuservice.entity.Order;
import cn.edu.fudan.campuservice.entity.Response;
import cn.edu.fudan.campuservice.exception.NoSuchOrderException;
import cn.edu.fudan.campuservice.service.OrderService;
import cn.edu.fudan.campuservice.utils.FileUtil;
import cn.edu.fudan.campuservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public Response getOrderById(@PathVariable Integer orderId) {
        try {
            return Response.success(orderService.getOrder(orderId));
        } catch (NoSuchOrderException e) {
            return new Response<>("400", "wrong orderId", e.getMessage());
        }
    }

    @GetMapping("/acceptOrder")
    public Response acceptOrder(@ParamCheck Integer orderId, @ParamCheck Integer accepterId) {
        orderService.acceptOrder(orderId, accepterId);
        return Response.success("accept successfully");
    }

    @GetMapping("/finishOrder")
    public Response finishOrder(@ParamCheck Integer orderId) {
        orderService.finishOrder(orderId);
        return Response.success("finish successfully");
    }

    @GetMapping("/confirmOrder")
    public Response confirmOrder(@ParamCheck Integer orderId) {
        orderService.confirmOrder(orderId);
        return Response.success("confirm successfully");
    }

    @PostMapping("/postOrder")
    public Response insertOrder(@RequestBody Order order) {
        order.setPostTime(new Date());
        order.setStatus(0);
        orderService.saveOrder(order);
        return Response.success("insert successfully");
    }

    @GetMapping("/getOrderByPoster")
    public Response getOrderByPoster(@ParamCheck Integer posterId) {
        return Response.success(orderService.searchOrderByPoster(posterId));
    }

    @GetMapping("/getOrderByAccepter")
    public Response searchOrderByAccepter(@ParamCheck Integer accepterId) {
        return Response.success(orderService.searchOrderByAccepter(accepterId));
    }

    @GetMapping("/searchOrderByStart")
    public Response searchOrderByStart(@ParamCheck Integer start) {
        return Response.success(orderService.searchOrderByAttribute("start", start));
    }

    @GetMapping("/searchOrderByDestination")
    public Response searchOrderByDestination(@ParamCheck Integer destination) {
        return Response.success(orderService.searchOrderByAttribute("destination", destination));
    }

    @GetMapping("/search")
    public Response search(@RequestHeader("Authorization") String token, @ParamCheck Integer start, @ParamCheck Integer destination) {
        return Response.success(orderService.search(JwtUtil.validateToken(token).getUserId(), start, destination));
    }

    @GetMapping("/getRunningOrder")
    public Response getRunningOrder(@RequestHeader("Authorization") String token) {
        return Response.success(orderService.getRunningOrder(JwtUtil.validateToken(token).getUserId()));
    }

    @GetMapping("/getWaitingOrder")
    public Response getWaitingOrder(@RequestHeader("Authorization") String token) {
        return Response.success(orderService.getWaitingOrder(JwtUtil.validateToken(token).getUserId()));
    }

    @PostMapping("/uploadPosterPhoto")
    public Response uploadPosterPhoto(MultipartFile file) {
        if (file.isEmpty()) {
            return new Response<>("400", "upload failed", "file is empty");
        }

        try {
            String fileName = FileUtil.saveFile(file);
            return Response.success(fileName);
        } catch (Exception e) {
            return new Response<>("400", "upload failed", e.getMessage());
        }
    }

    @PostMapping("/uploadAccepterPhoto")
    public Response uploadAccepterPhoto(MultipartFile file, @ParamCheck Integer orderId) {
        if (file.isEmpty()) {
            return new Response<>("400", "upload failed", "file is empty");
        }

        try {
            orderService.updateAccepterPhoto(orderId, file);
        } catch (Exception e) {
            return new Response<>("400", "upload failed", e.getMessage());
        }
        return Response.success("upload successfully");
    }

    @Resource
    private HttpServletResponse response;

    @GetMapping("/getPhoto")
    public void getPhoto(@ParamCheck String filePath) throws IOException {
        String fullPath = FileUtil.getUploadPath() + File.separator + filePath;
        File packetFile = new File(fullPath);
        String fileName = packetFile.getName();
        File file = new File(fullPath);
        if (file.exists()) {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                response.setContentType("text/html;charset=UTF-8");
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                OutputStream os = response.getOutputStream();

                os.write("对应文件不存在".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    @GetMapping("/getPhoto")
//    public Response getPhoto(@ParamCheck String filePath) throws IOException {
//        ByteArrayOutputStream out = null;
//        try {
//            BufferedImage image = ImageIO.read(new FileInputStream(new File(FileUtil.getUploadPath() + File.separator + filePath)));
//            out = new ByteArrayOutputStream();
//
//            if (image != null) {
//                ImageIO.write(image, filePath.substring(filePath.lastIndexOf(".") + 1), out);
//            }
//            return Response.success(out.toByteArray());
//        } catch (IOException e) {
//            return new Response<>("400", "getError", e.getMessage());
//        } finally {
//            if (out != null) {
//                out.flush();
//                out.close();
//            }
//        }
//    }
}
