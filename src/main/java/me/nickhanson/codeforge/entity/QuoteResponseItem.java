package me.nickhanson.codeforge.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteResponseItem{

	@JsonProperty("authorSlug")
	private String authorSlug;

	@JsonProperty("author")
	private String author;

	@JsonProperty("length")
	private int length;

	@JsonProperty("dateModified")
	private String dateModified;

	@JsonProperty("_id")
	private String id;

	@JsonProperty("content")
	private String content;

	@JsonProperty("dateAdded")
	private String dateAdded;

	@JsonProperty("tags")
	private List<String> tags;

	public void setAuthorSlug(String authorSlug){
		this.authorSlug = authorSlug;
	}

	public String getAuthorSlug(){
		return authorSlug;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setLength(int length){
		this.length = length;
	}

	public int getLength(){
		return length;
	}

	public void setDateModified(String dateModified){
		this.dateModified = dateModified;
	}

	public String getDateModified(){
		return dateModified;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public void setDateAdded(String dateAdded){
		this.dateAdded = dateAdded;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public void setTags(List<String> tags){
		this.tags = tags;
	}

	public List<String> getTags(){
		return tags;
	}

	@Override
 	public String toString(){
		return 
			"QuoteResponseItem{" + 
			"authorSlug = '" + authorSlug + '\'' + 
			",author = '" + author + '\'' + 
			",length = '" + length + '\'' + 
			",dateModified = '" + dateModified + '\'' + 
			",_id = '" + id + '\'' + 
			",content = '" + content + '\'' + 
			",dateAdded = '" + dateAdded + '\'' + 
			",tags = '" + tags + '\'' + 
			"}";
		}
}