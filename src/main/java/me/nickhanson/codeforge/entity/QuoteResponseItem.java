package me.nickhanson.codeforge.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteResponseItem {

	@JsonProperty("q")
	private String q;

	@JsonProperty("a")
	private String a;

	@JsonProperty("h")
	private String h;

	public String getQ(){
		return q;
	}

	public String getA(){
		return a;
	}

	public String getH(){
		return h;
	}
}