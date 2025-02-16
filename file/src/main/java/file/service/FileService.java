package file.service;

import file.exception.ErrorCode;
import file.exception.InternalException;
import file.utils.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Api s3Api;

    public String uploadFile(MultipartFile file) throws IOException {
        String userId = SecurityContextHolder.getUserId();
        String path = s3Api.upload(
            userId + "/" + UUID.randomUUID() + "/" + file.getOriginalFilename(),
                file.getInputStream()
        );
        log.info("Document saved successfully by user with id: {}", userId);
        return path;
    }

    public byte[] downloadFile(String path) throws IOException {
        log.info("Downloading file: {}", path);
        return s3Api.download(path);
    }

    public List<String> findPreviews(String userId) {
        log.info("Find previews files by user with id: {}", userId);
        return s3Api.findObjectsPreviews(userId);
    }

    public String getUrl(String path) {
        log.info("Find URL file: {}", path);
        return s3Api.findPresignedObject(path);
    }

    public void delete(String path) {
        String userId = SecurityContextHolder.getUserId();
        if (!path.contains(userId)) {
            throw new InternalException(HttpStatus.FORBIDDEN, ErrorCode.DOCUMENT_NOT_YOURS);
        }
        log.info("Deleting file: {}", path);
        s3Api.delete(path);
    }
}
