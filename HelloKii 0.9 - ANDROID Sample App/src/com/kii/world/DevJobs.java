package com.kii.world;

/**
 * Represents an item in a ToDo list
 */
public class DevJobs {

	/**
	 * Item text
	 */
	@com.google.gson.annotations.SerializedName("text")
	private String mText;

	/**
	 * Item Id
	 */
	@com.google.gson.annotations.SerializedName("id")
	private String mId;

	/**
	 * Indicates if the item is completed
	 */
	@com.google.gson.annotations.SerializedName("complete")
	private boolean mComplete;
	
	@com.google.gson.annotations.SerializedName("contact")
	private String mContact;
	
	@com.google.gson.annotations.SerializedName("jobtitle")
	private String mJobTitle;

	/**
	 * ToDoItem constructor
	 */
	public DevJobs() {

	}

	@Override
	public String toString() {
		return getText();
	}

	/**
	 * Initializes a new ToDoItem
	 * 
	 * @param text
	 *            The item text
	 * @param id
	 *            The item id
	 */
	public DevJobs(String text, String id, String contact, String jobTitle) {
		this.setText(text);
		this.setId(id);
		this.setContact(contact);
		this.setJobTitle(jobTitle);
	}

	/**
	 * Returns the item text
	 */
	public String getText() {
		return mText;
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setText(String text) {
		mText = text;
	}

	/**
	 * Returns the item id
	 */
	public String getId() {
		return mId;
	}

	
	/**
	 * Returns the item contact
	 */
	public String getContact() {
		return mContact;
	}
	
	/**
	 * Returns the item contact
	 */
	public String getJobTitle() {
		return mJobTitle;
	}

	/**
	 * Sets the item id
	 * 
	 * @param id
	 *            id to set
	 */
	public final void setId(String id) {
		mId = id;
	}
	
	/**
	 * Sets the item id
	 * 
	 * @param id
	 *            id to set
	 */
	public final void setContact(String contact) {
		mContact = contact;
	}

	/**
	 * Indicates if the item is marked as completed
	 */
	public boolean isComplete() {
		return mComplete;
	}

	/**
	 * Marks the item as completed or incompleted
	 */
	public void setComplete(boolean complete) {
		mComplete = complete;
	}
	
	public void setJobTitle(String jobTitle)
	{
		mJobTitle = jobTitle;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof DevJobs && ((DevJobs) o).mId == mId;
	}
}
