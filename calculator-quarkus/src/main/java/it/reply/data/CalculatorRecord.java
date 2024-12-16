package it.reply.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CalculatorRecord {
    private Long id;
    private double numerator;
    private double denominator;
    private char operator;
    private String result;
    private String error;
    private LocalDateTime timestamp;
}

