package com.condominio.chamados.shared.upload;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UploadStorageServiceTest {

    @Test
    void deveArmazenarECarregarArquivo(@TempDir Path tempDir) {
        UploadStorageService service = new UploadStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile(
                "arquivo",
                "manual.pdf",
                "application/pdf",
                "conteudo".getBytes(StandardCharsets.UTF_8)
        );

        UploadStorageService.StoredUpload stored = service.store(file);

        assertThat(stored.storedName()).endsWith(".pdf");
        assertThat(stored.originalName()).isEqualTo("manual.pdf");
        assertThat(service.loadAsResource(stored.storedName()).exists()).isTrue();
    }

    @Test
    void deveBloquearUploadVazio(@TempDir Path tempDir) {
        UploadStorageService service = new UploadStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile("arquivo", new byte[0]);

        assertThatThrownBy(() -> service.store(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("obrigatorio");
    }
}
