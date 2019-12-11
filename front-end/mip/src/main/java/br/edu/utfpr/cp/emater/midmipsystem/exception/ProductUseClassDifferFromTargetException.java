package br.edu.utfpr.cp.emater.midmipsystem.exception;

public class ProductUseClassDifferFromTargetException extends Exception {

    public ProductUseClassDifferFromTargetException() {
        super ("A classe de uso do produto e do alvo deve ser a mesma!");
    }
    
}
