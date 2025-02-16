package file.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Configuration {

    @Bean
    public MinioClient minioClient(S3Properties s3Properties) throws Exception {
        MinioClient client = MinioClient.builder()
            .endpoint(s3Properties.getEndpoint())
            .credentials(s3Properties.getAccessKey(), s3Properties.getSecretKey())
            .region(s3Properties.getRegion())
            .build();
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(s3Properties.getBucket()).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(s3Properties.getBucket()).build());
        }
        return client;
    }
}
