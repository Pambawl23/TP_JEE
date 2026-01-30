package com.polytech.commandes.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;


@AllArgsConstructor
@Getter
public class BoutiqueAPIException extends RuntimeException {
    private final int code;
    private final String msg;

    public ResponseEntity getResponse() {
        return ResponseEntity.status(code).body(msg);
    }
}
