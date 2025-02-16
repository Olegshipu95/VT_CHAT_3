package file.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import file.BaseControllerTest;
import file.utils.MappingUtils;
import org.junit.jupiter.api.Test;

import org.assertj.core.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FileControllerTest extends BaseControllerTest {

    @Test
    void uploadShouldReturnCreated() throws Exception {
        uploadDocument();
    }

    @Test
    void downloadShouldExecuteOk() throws Exception {
        FileController.UploadFileResponse uploadFileResponse = uploadDocument();
        MvcResult mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.get("/files")
                .param("path", uploadFileResponse.getPath())
        ).andExpect(status().isOk()).andReturn();

        Assertions.assertThat(mvcResult.getResponse().getContentType()).isEqualTo(MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        FileController.UploadFileResponse uploadFileResponse = uploadDocument();
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/files")
                .param("path", uploadFileResponse.getPath())
        ).andExpect(status().isNoContent());
    }

    @Test
    void findPreviewsShouldReturnOk() throws Exception {
        FileController.UploadFileResponse uploadFileResponse = uploadDocument();
        mockMvc.perform(
            MockMvcRequestBuilders.get("/files/previews")
                .param("userId", uploadFileResponse.getPath())
        ).andExpectAll(
            status().isOk(),
            jsonPath("$").isNotEmpty()
        );
    }

    @Test
    void findDocumentUrlShouldReturnOk() throws Exception {
        FileController.UploadFileResponse uploadFileResponse = uploadDocument();
        mockMvc.perform(
            MockMvcRequestBuilders.get("/files/urls")
                .param("path", uploadFileResponse.getPath())
        ).andExpectAll(
            status().isOk(),
            jsonPath("$").isNotEmpty()
        );
    }

    private FileController.UploadFileResponse uploadDocument() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "picture.png",
            MediaType.IMAGE_PNG_VALUE,
            getClass().getResourceAsStream("/picture.png")
        );

        String responseContent = mockMvc.perform(
            MockMvcRequestBuilders.multipart("/files").file(file)
        ).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        return MappingUtils.toObject(responseContent, new TypeReference<FileController.UploadFileResponse>() {});
    }
}
