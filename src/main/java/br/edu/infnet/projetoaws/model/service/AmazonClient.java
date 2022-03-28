package br.edu.infnet.projetoaws.model.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class AmazonClient {
    
    private AmazonS3 s3client;
    
    @Value("${amazon.s3.bucket-name}")
    private String bucketName;
    @Value("${amazon.s3.access-key}")
    private String accessKey;
    @Value("${amazon.s3.secret-key}")
    private String secretKey;
    
    @PostConstruct
    private void initAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        
        s3client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.SA_EAST_1)
            .build();
    }
    
    private File convertMultipartToFile(MultipartFile file) throws IOException {
    
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    
    public String uploadFile(MultipartFile multipartFile) {
        
        try {
            File file = convertMultipartToFile(multipartFile);
            String fileName = multipartFile.getOriginalFilename();
            s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
            return s3client.getUrl(bucketName, fileName).toString();
        }
        catch (IOException e) {
            return e.getMessage();
        }
    }
    
    public String deleteFile(String file) {
        
        s3client.deleteObject(bucketName, file);
        return "Arquivo excluido";
    }
    
    public String listFiles() {
        String files = "";
        
        ListObjectsV2Result result = s3client.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            files += os.getKey() + "\n";
        }
        return files;
    }
    
    public String downloadFile(String fileName) {
        
        S3Object s3Object = s3client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File(fileName));
            return "Arquivo baixado para o diretório do projeto";
        } catch (IOException | AmazonServiceException ex) {
            return ex.getMessage();
        }
    }
}
