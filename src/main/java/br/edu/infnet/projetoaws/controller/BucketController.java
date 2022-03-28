package br.edu.infnet.projetoaws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.edu.infnet.projetoaws.model.service.AmazonClient;


@RestController
@RequestMapping("/storage/")
public class BucketController {
    
    @Autowired 
    private AmazonClient amazonClient;
    
    @PostMapping("/upload")
    public String uploadFile(@RequestPart(value="file") MultipartFile file) {
        return amazonClient.uploadFile(file);
    }
    
    @DeleteMapping("/delete")
    public String deleteFile(@RequestPart(value="file") String file) {
        return amazonClient.deleteFile(file);
    }
    
    @GetMapping("/list")
    public String listFiles() {
        return amazonClient.listFiles();
    }
    
    @GetMapping("/download")
    public String downloadFile(@RequestPart(value="file") String file) {
        return amazonClient.downloadFile(file);
    }
}
