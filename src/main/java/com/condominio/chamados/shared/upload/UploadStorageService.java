package com.condominio.chamados.shared.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

@Service
public class UploadStorageService {

    private final Path uploadsRoot;

    public UploadStorageService(@Value("${app.uploads.dir:uploads}") String uploadsDir) {
        this.uploadsRoot = Path.of(uploadsDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadsRoot);
        } catch (IOException ex) {
            throw new IllegalStateException("Nao foi possivel criar diretorio de uploads", ex);
        }
    }

    public StoredUpload store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de upload e obrigatorio");
        }
        String originalName = sanitizeOriginalName(file.getOriginalFilename());
        String extension = extractExtension(originalName);
        String storedName = UUID.randomUUID() + extension;
        Path target = uploadsRoot.resolve(storedName).normalize();
        if (!target.startsWith(uploadsRoot)) {
            throw new IllegalArgumentException("Caminho de upload invalido");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao armazenar arquivo de upload", ex);
        }
        return new StoredUpload(storedName, originalName, file.getContentType(), file.getSize());
    }

    public Resource loadAsResource(String storedName) {
        Path filePath = uploadsRoot.resolve(storedName).normalize();
        if (!filePath.startsWith(uploadsRoot)) {
            throw new IllegalArgumentException("Arquivo de upload invalido");
        }
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalArgumentException("Arquivo de upload nao encontrado");
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Arquivo de upload invalido", ex);
        }
    }

    public void delete(String storedName) {
        if (storedName == null || storedName.isBlank()) {
            throw new IllegalArgumentException("Nome de arquivo armazenado invalido");
        }
        try {
            Path filePath = uploadsRoot.resolve(storedName).normalize();
            if (!filePath.startsWith(uploadsRoot)) {
                throw new IllegalArgumentException("Arquivo de upload invalido");
            }
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao limpar arquivo de upload", ex);
        }
    }

    private String sanitizeOriginalName(String originalFilename) {
        String clean = StringUtils.cleanPath(originalFilename == null ? "arquivo" : originalFilename);
        if (clean.isBlank()) {
            return "arquivo";
        }
        Path filenameOnly = Path.of(clean).getFileName();
        if (filenameOnly == null) {
            return "arquivo";
        }
        String value = filenameOnly.toString();
        if (value.contains("..")) {
            throw new IllegalArgumentException("Nome de arquivo invalido");
        }
        return value;
    }

    private String extractExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx <= 0 || idx == filename.length() - 1) {
            return "";
        }
        return filename.substring(idx).toLowerCase(Locale.ROOT);
    }

    public record StoredUpload(
            String storedName,
            String originalName,
            String contentType,
            long size
    ) {}
}
