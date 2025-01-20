package com.hoteling.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hoteling.project.domain.entity.Event;
import com.hoteling.project.domain.entity.EventFile;

public interface EventFileRepository extends JpaRepository<EventFile, Integer> {
	
    @Query(value="SELECT * FROM event_file u WHERE u.event_id = :event_id order by event_file_id desc",
    		countQuery="SELECT count(*) FROM event_file u WHERE u.event_id = :event_id"
    		, nativeQuery=true)
    public List<EventFile> findEventFileByEventId(@Param("event_id") int event_id) ;

    @Transactional
    @Modifying
    @Query(value="DELETE FROM event_file WHERE event_id = :event_id", nativeQuery=true)
    Integer deleteEventFileByEventId(@Param("event_id") int eventId);

    @Transactional
    @Modifying
    @Query(value="UPDATE event_file SET event_filename = :event_filename, event_file = :event_file WHERE event_id = :event_id", nativeQuery=true)
    int updateEventFile(@Param("event_filename") String eventFilename, @Param("event_file") String eventFile, @Param("event_id") int eventId);

    @Transactional
    @Modifying
    @Query(value="UPDATE event_file SET event_title_filename = :event_title_filename, event_title_file = :event_title_file WHERE event_id = :event_id", nativeQuery=true)
    int updateEventFileTitle(@Param("event_title_filename") String eventTitleFilename, @Param("event_title_file") String eventTitleFile, @Param("event_id") int eventId);

}
