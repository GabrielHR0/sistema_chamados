package com.condominio.chamados.audit.service;

import com.condominio.chamados.audit.domain.AuditLogEntry;
import com.condominio.chamados.audit.repository.AuditLogEntryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditService {

    private final AuditLogEntryRepository auditLogEntryRepository;

    public AuditService(AuditLogEntryRepository auditLogEntryRepository) {
        this.auditLogEntryRepository = auditLogEntryRepository;
    }

    @Transactional(readOnly = true)
    public List<AuditLogEntry> listarRecentes(int limite) {
        int safeLimit = Math.max(1, Math.min(limite, 1000));
        return auditLogEntryRepository.findAllByOrderByActionTstampDesc(PageRequest.of(0, safeLimit));
    }
}
