package br.edu.utfpr.cp.emater.midmipsystem.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResponseDTO implements Serializable {
	
	public String headers;
	public Integer statusCode;
	public String body;
}
