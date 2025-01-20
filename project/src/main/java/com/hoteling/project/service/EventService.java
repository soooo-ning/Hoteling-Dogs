package com.hoteling.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoteling.project.domain.entity.Event;
import com.hoteling.project.domain.entity.EventFile;
import com.hoteling.project.repository.EventFileRepository;
import com.hoteling.project.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EventFileRepository eventFileRepository;

    public Event getEventById(int eventId) {
        return eventRepository.findById(eventId);
    }
    
    public List<Event> getAllEvents() {
    	return eventRepository.findAll();
    }
    
    public List<Event> getAllEventSort() {
        return eventRepository.findAllCreatedtSort();
    }
    
    public List<Event> getEventByEventId(Event event) {
    	return eventRepository.findEventByEventId(event.getEvent_id());
    }

    public List<EventFile> getFindEventFileByEventId(Event event) throws Exception {
        return (List<EventFile>) eventFileRepository.findEventFileByEventId(event.getEvent_id()); // 전체 회원 리뷰리스트
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveEvent(Event event) {
		System.out.println("event : getTitle : " + event.getTitle() + " / event.getContent : " + event.getContent());
		System.out.println("event : created_at : " + event.getCreated_at() );
		
		eventRepository.save(event);
		System.out.println("event getEvent_id : " + event.getEvent_id());
    }
    

    @Transactional(rollbackFor = Exception.class)
    public void saveEventFile(EventFile eventFile) throws Exception {
    	System.out.println("1111111111111111111111111111111111111111 event : getEvent_filename : " + eventFile.getEvent_filename());
    	System.out.println("eventFile : getEvent_title_filename : " + eventFile.getEvent_title_filename() );
    	System.out.println("eventFile : eventId : " + eventFile.getEvent().getEvent_id() );
    	
    	eventFileRepository.save(eventFile);
    	System.out.println("getEvent_file_id : " + eventFile.getEvent_file_id());
    	
    }
    
    public Integer updateEvent(Event event) throws Exception {
    	System.out.println("1111111111111111111111111111111111111111 event : getEvent_id : " + event.getEvent_id());
    	System.out.println("event : getTitle : " + event.getTitle() );
    	return eventRepository.updateEvent(event.getTitle(), event.getEvent_id()); // 전체 회원 리뷰리스트
    }
    
    public Integer deleteEventFileByEventId(Event event) throws Exception {
    	return eventFileRepository.deleteEventFileByEventId(event.getEvent_id()); // 이벤트 파일 삭제
    }

    public Integer deleteEvent(Event event) throws Exception {
    	return eventRepository.deleteEvent(event.getEvent_id()); // 이벤트 삭제
    }

    public int updateEventFile(EventFile eventFile) throws Exception {
    	System.out.println("1111111111111111111111111111111111111111updateEventFile event : getEvent_id : " + eventFile.getEvent().getEvent_id());
    	System.out.println("1111111111111111111111111111111111111111 event : getEvent_filename : " + eventFile.getEvent_filename());
    	System.out.println("eventFile : eventId : " + eventFile.getEvent().getEvent_id() );
    	
    	return eventFileRepository.updateEventFile(eventFile.getEvent_filename(), eventFile.getEvent_file(), eventFile.getEvent().getEvent_id()); // 전체 회원 리뷰리스트
    }
    
    public int updateEventFileTitle(EventFile eventFile) throws Exception {
    	System.out.println("1111111111111111111111111111111111111111updateEventFile event : getEvent_id : " + eventFile.getEvent().getEvent_id());
    	System.out.println("eventFile : getEvent_title_filename : " + eventFile.getEvent_title_filename() );
    	System.out.println("eventFile : eventId : " + eventFile.getEvent().getEvent_id() );
    	
    	return eventFileRepository.updateEventFileTitle(eventFile.getEvent_title_filename(), eventFile.getEvent_title_file(), eventFile.getEvent().getEvent_id()); // 전체 회원 리뷰리스트
    }

    
}
