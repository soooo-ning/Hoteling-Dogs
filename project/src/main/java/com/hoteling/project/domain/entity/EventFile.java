package com.hoteling.project.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "event_file")
public class EventFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int event_file_id;

    private String event_title_filename;
    
    @Column(columnDefinition = "MEDIUMBLOB")
    private String event_title_file;
    
    private String event_filename;
    
    @Column(columnDefinition = "MEDIUMBLOB")
    private String event_file;

    @OneToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

	public int getEvent_file_id() {
		return event_file_id;
	}

	public void setEvent_file_id(int event_file_id) {
		this.event_file_id = event_file_id;
	}

	public String getEvent_title_filename() {
		return event_title_filename;
	}

	public void setEvent_title_filename(String event_title_filename) {
		this.event_title_filename = event_title_filename;
	}

	public String getEvent_title_file() {
		return event_title_file;
	}

	public void setEvent_title_file(String event_title_file) {
		this.event_title_file = event_title_file;
	}

	public String getEvent_filename() {
		return event_filename;
	}

	public void setEvent_filename(String event_filename) {
		this.event_filename = event_filename;
	}

	public String getEvent_file() {
		return event_file;
	}

	public void setEvent_file(String event_file) {
		this.event_file = event_file;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
    
}
