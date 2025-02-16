package file.controller;

import file.service.FileService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "Cкачивание файла")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Файл скачан"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> download(
        @Parameter(description = "Путь до файла") @RequestParam String path
    ) throws IOException {
        String fileName = path.split("/")[path.split("/").length - 1];
        return asFileResponseEntity(fileService.downloadFile(path), fileName);
    }

    @Operation(summary = "Загрузка файла")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Файл загружен"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UploadFileResponse upload(
        @Parameter(description = "Файл к загрузке") @RequestParam MultipartFile file
    ) throws IOException {
        String path = fileService.uploadFile(file);
        return new UploadFileResponse(path);
    }

    @Operation(summary = "Удаление файла")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Файл удален"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
        @Parameter(description = "Путь до файла") @RequestParam String path
    ) {
        fileService.delete(path);
    }

    @Operation(summary = "Поиск всех файлов пользователя")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Файл получены"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @GetMapping("/previews")
    @ResponseStatus(HttpStatus.OK)
    public List<String> findPreviews(
        @Parameter(description = "Id пользователя") @RequestParam String userId
    ) {
        return fileService.findPreviews(userId);
    }

    @Hidden
    @Operation(summary = "Получение Url файла")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Файл загружен"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @GetMapping("/urls")
    @ResponseStatus(HttpStatus.OK)
    public FindFileUrlResponse getUrl(
        @Parameter(description = "Путь до файла") @RequestParam String path
    ) {
        String url = fileService.getUrl(path);
        return new FindFileUrlResponse(url);
    }


    private ResponseEntity<byte[]> asFileResponseEntity(byte[] file, String filename) {
        ContentDisposition contentDisposition = ContentDisposition.inline().filename(filename).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(file);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class FindFileUrlResponse {
        @Schema(description = "Ссылка файла")
        private String url;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UploadFileResponse {
        @Schema(description = "Путь к файлу")
        private String path;
    }

    private static final String APPLICATION_DOC_TYPE = "application/msword";
    private static final String APPLICATION_DOCX_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    static final List<String> AVAILABLE_DOCUMENT_TYPES = List.of(
        MediaType.APPLICATION_PDF_VALUE,
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        APPLICATION_DOCX_TYPE,
        APPLICATION_DOC_TYPE
    );
}
