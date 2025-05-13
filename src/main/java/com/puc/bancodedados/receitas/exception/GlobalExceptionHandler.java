package com.puc.bancodedados.receitas.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Pattern FK_VIOLATION_PATTERN = Pattern.compile(
      "violates foreign key constraint \"([^\"]+)\" on table \"([^\"]+)\"",
      Pattern.CASE_INSENSITIVE);
  private static final Pattern DETAIL_KEY_PATTERN = Pattern.compile(
      "Key \\(([^)]+)\\)=\\(([^)]+)\\) is still referenced from table \"([^\"]+)\"",
      Pattern.CASE_INSENSITIVE);

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> handleDataIntegrityViolation(
      DataIntegrityViolationException ex, WebRequest request) {

    String userMessage = "Operação não permitida devido a dados dependentes.";
    String detailedMessage = ex.getMostSpecificCause().getMessage();

    Matcher fkMatcher = FK_VIOLATION_PATTERN.matcher(detailedMessage);
    Matcher detailMatcher = DETAIL_KEY_PATTERN.matcher(detailedMessage);

    if (detailedMessage.contains("violates foreign key constraint")) {
      userMessage = "Não é possível excluir ou atualizar este item pois ele está sendo referenciado por outros registros.";
      if (detailMatcher.find()) {
        String referencedTable = detailMatcher.group(3);
        userMessage = String.format(
            "Não é possível excluir ou atualizar este item, pois a chave (%s)=(%s) ainda é referenciada pela tabela \"%s\".",
            detailMatcher.group(1),
            detailMatcher.group(2),
            referencedTable);
      } else if (fkMatcher.find()) {
        String constraintName = fkMatcher.group(1);
        String targetTable = fkMatcher.group(2);
        userMessage = String.format(
            "Operação viola a restrição de chave estrangeira \"%s\" na tabela \"%s\". Verifique os dados relacionados.",
            constraintName,
            targetTable);
      }
    } else if (detailedMessage.contains("violates unique constraint")) {
      userMessage = "Já existe um item com os mesmos identificadores únicos.";
    }

    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", System.currentTimeMillis());
    body.put("status", HttpStatus.CONFLICT.value());
    body.put("error", "Conflict");
    body.put("message", userMessage);
    body.put("path", request.getDescription(false).replace("uri=", ""));

    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
  }
}