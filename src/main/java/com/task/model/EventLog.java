package com.task.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "EventLog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLog {
 
	@javax.persistence.Id
	private String Id;
	private String event;
	
	public EventLog(String event) {
		this.event = event;
	}
	
}
