package com.hoteling.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hoteling.project.domain.entity.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
	
	 @Query(value="SELECT * FROM event u order by created_at desc, event_id desc",
    		countQuery="SELECT count(*) FROM event u "
    		, nativeQuery=true)
    public List<Event> findAllCreatedtSort();
    
    @Transactional
    @Modifying
    @Query(value="update event set title = :title where event_id = :event_id", nativeQuery=true)
    public Integer updateEvent(@Param("title") String title, @Param("event_id") int event_id);
    
    @Transactional
    @Modifying
    @Query(value="DELETE FROM event WHERE event_id = :event_id", nativeQuery=true)
    public Integer deleteEvent(@Param("event_id") int event_id);
    
    
    @Query(value="SELECT * FROM event u WHERE u.event_id = :event_id",
    		countQuery="SELECT count(*) FROM event u WHERE u.event_id = :event_id"
    		, nativeQuery=true)
    public List<Event> findEventByEventId(@Param("event_id") int event_id) ;

	public Event findById(int eventId);
}
