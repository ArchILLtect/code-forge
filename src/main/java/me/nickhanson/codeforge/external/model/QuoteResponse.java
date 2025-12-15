package me.nickhanson.codeforge.external.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class representing a quote response from an external API.
 */
public class QuoteResponse {

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

	/**
	 * Sets the author slug.
	 * @param authorSlug the author slug to set
	 */
	public void setAuthorSlug(String authorSlug){
		this.authorSlug = authorSlug;
	}

	/**
	 * Gets the author slug.
	 * @return the author slug
	 */
	public String getAuthorSlug(){
		return authorSlug;
	}

	/**
	 * Sets the author of the quote.
	 * @param author the author to set
	 */
	public void setAuthor(String author){
		this.author = author;
	}

	/**
	 * Gets the author of the quote.
	 * @return the author
	 */
	public String getAuthor(){
		return author;
	}

	/**
	 * Sets the length of the quote.
	 * @param length the length to set
	 */
	public void setLength(int length){
		this.length = length;
	}

	/**
	 * Gets the length of the quote.
	 * @return the length
	 */
	public int getLength(){
		return length;
	}

	/**
	 * Sets the date the quote was last modified.
	 * @param dateModified the date modified to set
	 */
	public void setDateModified(String dateModified){
		this.dateModified = dateModified;
	}

	/**
	 * Gets the date the quote was last modified.
	 * @return the date modified
	 */
	public String getDateModified(){
		return dateModified;
	}

	/**
	 * Sets the unique identifier of the quote.
	 * @param id the id to set
	 */
	public void setId(String id){
		this.id = id;
	}

	/**
	 * Gets the unique identifier of the quote.
	 * @return the id
	 */
	public String getId(){
		return id;
	}

	/**
	 * Sets the content of the quote.
	 * @param content the content to set
	 */
	public void setContent(String content){
		this.content = content;
	}

	/**
	 * Gets the content of the quote.
	 * @return the content
	 */
	public String getContent(){
		return content;
	}

	/**
	 * Sets the date the quote was added.
	 * @param dateAdded the date added to set
	 */
	public void setDateAdded(String dateAdded){
		this.dateAdded = dateAdded;
	}

	/**
	 * Gets the date the quote was added.
	 * @return the date added
	 */
	public String getDateAdded(){
		return dateAdded;
	}

	/**
	 * Sets the tags associated with the quote.
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags){
		this.tags = tags;
	}

	/**
	 * Gets the tags associated with the quote.
	 * @return the tags
	 */
	public List<String> getTags(){
		return tags;
	}

	/**
	 * Returns a string representation of the QuoteResponse object.
	 * @return string representation of the object
	 */
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