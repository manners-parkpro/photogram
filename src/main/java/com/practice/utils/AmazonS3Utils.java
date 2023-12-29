package com.practice.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.practice.configure.component.S3Properties;
import org.apache.commons.lang3.StringUtils;

public class AmazonS3Utils {

    public static AmazonS3 amazonS3(String profiles, S3Properties s3Properties) {
        AmazonS3 amazonS3;

        if (StringUtils.isBlank(profiles) || "local".equals(profiles)) {
            AWSCredentials awsCredentials = new BasicAWSCredentials(s3Properties.getAccessKey(), s3Properties.getSecretKey());
            amazonS3 = AmazonS3ClientBuilder.standard()
                    .withRegion(s3Properties.getRegion())
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();
        } else {
            amazonS3 = AmazonS3ClientBuilder.standard()
                    .withRegion(s3Properties.getRegion())
                    .withCredentials(new InstanceProfileCredentialsProvider(true))
                    .build();
        }

        return amazonS3;
    }
}
