package me.nickhanson.codeforge.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteResponse{

	@JsonProperty("QuoteResponse")
	private List<QuoteResponseItem> quoteResponse;

	public void setQuoteResponse(List<QuoteResponseItem> quoteResponse){
		this.quoteResponse = quoteResponse;
	}

	public List<QuoteResponseItem> getQuoteResponse(){
		return quoteResponse;
	}

	@Override
 	public String toString(){
		return 
			"QuoteResponse{" + 
			"quoteResponse = '" + quoteResponse + '\'' + 
			"}";
		}
}