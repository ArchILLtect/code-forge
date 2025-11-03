package me.nickhanson.codeforge.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteResponse{

	@JsonProperty("QuoteResponse")
	private List<QuoteResponseItem> quoteResponse;

	public List<QuoteResponseItem> getQuoteResponse(){
		return quoteResponse;
	}
}