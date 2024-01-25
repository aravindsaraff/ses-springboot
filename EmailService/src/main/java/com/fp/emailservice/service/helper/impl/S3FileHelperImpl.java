package com.fp.emailservice.service.helper.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fp.emailservice.config.fpAWSBean;
import com.fp.emailservice.exception.FPGenericException;
import com.fp.emailservice.exception.InvalidRequestException;
import com.fp.emailservice.service.helper.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Optional;

/**
 * Encapsulates S3 specific file operations. Provides ability to read a S3 file stored in a bucket and thus
 * provides the implementation of the strategy File Helper.
 */
@Component("s3Helper")
public class S3FileHelperImpl implements FileHelper {
    private static final Logger logger = LoggerFactory.getLogger(S3FileHelperImpl.class);
    // Any attachment bucket value required for S3
    @Value("${aws_fp_files_bucket}")
    private String fpFilesBucket;
    // AWS Connection info bean
    @Autowired
    private fpAWSBean awsBean;
    // Amazon SDK S3 client to handle s3 request
    private AmazonS3 awsS3Client;

    /**
     * Dowload an Amazon S3 file. When object i downloaded, you get all the objects' metadata and a stream to read
     * the contents.
     * @param fileName Name of the file in String
     * @return {@link java.io.InputStream} of S3 file object content
     */
    @Override
    public InputStream readFile(String fileName) throws FPGenericException {
        Optional<String> optionalFileName = Optional.of(fileName);
        if(!optionalFileName.isPresent()) {
            throw new FPGenericException("Invalid FileName for AWS file read");

        }
        Optional<AmazonS3> s3Optional = Optional.of(awsS3Client);
        if(!s3Optional.isPresent()) {
            throw new RuntimeException("AWS Client Failed");
        }
        S3Object file = awsS3Client.getObject(new GetObjectRequest(fpFilesBucket, fileName));
        return file.getObjectContent();
    }

    public String getfpFilesBucket() {
        return fpFilesBucket;
    }

    @PostConstruct
    public void initialize() {
        awsS3Client = new AmazonS3Client(awsBean);
        logger.info("Inited AmazonS3 client successfully");
    }
}
