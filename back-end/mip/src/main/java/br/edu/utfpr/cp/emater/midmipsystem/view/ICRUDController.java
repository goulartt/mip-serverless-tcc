package br.edu.utfpr.cp.emater.midmipsystem.view;

import java.util.List;

public interface ICRUDController<T> {
    
    public List<T> readAll();
    public String create();
    public String prepareUpdate (Long anId);
    public String update();
}
