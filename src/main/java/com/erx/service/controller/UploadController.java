package com.erx.service.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/upload")
public class UploadController {

    private Queue<byte[]> queue = new LinkedBlockingDeque<>();

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity postData(@RequestParam String segmentNumber, @RequestParam MultipartFile binaryData)
            throws IOException {
        queue.add(binaryData.getBytes());
        return ResponseEntity.ok().build();
    }


    @RequestMapping(name = "/get", method = RequestMethod.GET)
    public StreamingResponseBody getNext() {
        return out -> {
            byte[] file = queue.remove();
                out.write(file);
        };
    }

}
