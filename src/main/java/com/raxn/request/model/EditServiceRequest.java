package com.raxn.request.model;

public class EditServiceRequest {
	private String id;
	private String fafaicon;
	private String name;
	private String status;
	private String url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFafaicon() {
		return fafaicon;
	}

	public void setFafaicon(String fafaicon) {
		this.fafaicon = fafaicon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
