package file.service;

import file.config.S3Properties;
import file.exception.S3Exception;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Api {

    private static final int EXPIRY = 1;
    private static final long PART_SIZE = -1L;

    private final MinioClient minioClient;

    private final S3Properties s3Properties;

    public String upload(String path, InputStream inputStream) {
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(s3Properties.getBucket())
                .object(path)
                .stream(inputStream, inputStream.available(), PART_SIZE)
                .build();
            minioClient.putObject(putObjectArgs);
        } catch (Exception ex) {
            throw new S3Exception("Can't upload object", ex);
        }
        log.debug("Successfully put object to path - {}", path);
        return path;
    }

    public byte[] download(String path) {
        try (InputStream inputStream = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(s3Properties.getBucket())
                .object(path)
                .build())) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            inputStream.transferTo(outputStream);
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new S3Exception("Can't download object", ex);
        }
    }

    public List<String> findObjectsPreviews(String prefix) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(s3Properties.getBucket())
                    .recursive(true)
                    .prefix(prefix)
                    .build()
            );
            return StreamSupport.stream(results.spliterator(), false)
                .map(result -> {
                    try {
                        return result.get().objectName();
                    } catch (Exception ex) {
                        throw new RuntimeException("Error reading object name", ex);
                    }
                })
                .toList();
        } catch (Exception ex) {
            throw new S3Exception("Can't find object's list", ex);
        }
    }

    public String findPresignedObject(String path) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(s3Properties.getBucket())
                    .object(path)
                    .method(Method.GET)
                    .expiry(EXPIRY, TimeUnit.HOURS)
                    .build()
            );
        } catch (Exception ex) {
            throw new S3Exception("Can't find object's url", ex);
        }
    }

    public void delete(String path) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(s3Properties.getBucket())
                    .object(path)
                    .build()
            );
        } catch (Exception ex) {
            throw new S3Exception("Can't delete object", ex);
        }
        log.debug("Successfully deleted object by path - {}", path);
    }
}
