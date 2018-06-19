package br.ce.wcaquino.exceptions;

public class FilmeSemEstoqueException extends RuntimeException{
    public FilmeSemEstoqueException(String message) {
        super(message);
    }
}
