package file.controller;

import file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketFileController {

    private final FileService fileService;

    @MessageMapping("/upload")
    @SendTo("/topic/uploadResponse")
    public String upload(UploadFileRequest request) throws IOException {
        SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(UUID.randomUUID().toString(), "usernam", List.of(new SimpleGrantedAuthority(("USEr")))));
        byte[] file = Base64.getDecoder().decode(request.encodedImage);
        MultipartFile multiPartFile = new MockMultipartFile(UUID.randomUUID().toString(), file);
        fileService.uploadFile(multiPartFile);
        return "{\"path\": \"" + fileService.uploadFile(multiPartFile) + "\"}";
    }

    public static class UploadFileRequest {
        String encodedImage;
    }
}
