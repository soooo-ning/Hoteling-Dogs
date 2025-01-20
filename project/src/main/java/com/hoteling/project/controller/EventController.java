package com.hoteling.project.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hoteling.project.domain.entity.Event;
import com.hoteling.project.domain.entity.EventFile;
import com.hoteling.project.domain.entity.User;
import com.hoteling.project.service.EventService;
import com.hoteling.project.service.UserService;


@Controller
public class EventController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private UserService userService;
    
 // 공통 메소드: 로그인된 사용자 정보를 모델에 추가
    private void addUserAttributes(Model model, Authentication auth) {
    if (auth != null && auth.isAuthenticated()) {
        String loginedId = auth.getName();
        User loginedUser = userService.getLoginUserByUId(loginedId);
        if (loginedUser != null) {
            model.addAttribute("uId", loginedUser.getUId());
            model.addAttribute("uName", loginedUser.getUName());
        }
    }
}

    @GetMapping("/eventform")
    public String showEventForm(Model model, Authentication auth) {
    	addUserAttributes(model, auth);
        return "event_form"; 
    }

    @GetMapping("/eventlist")
    public String showEventList(Model model, Authentication auth) {
//        model.addAttribute("events", eventService.getAllEvents());
    	addUserAttributes(model, auth);
    	List<Event> events = eventService.getAllEventSort();
    	model.addAttribute("events", events);
    	List<EventFile> eventFiles = new ArrayList<>();
    	try {
    		eventFiles = getEventFileList(events);
    	} catch (Exception e) {
    		
    		e.printStackTrace();
    	}
    	model.addAttribute("eventFiles", eventFiles);
    	model.addAttribute("userType", "2"); // 1:일반사용자 / 2:관리자
        return "event_list"; 
    }
    
    @PostMapping("/eventread")
    public String eventread(@ModelAttribute Event event, Model model, Authentication auth) {
    	addUserAttributes(model, auth);
    	List<Event> events = eventService.getEventByEventId(event);
    	model.addAttribute("events", events);
    	List<EventFile> eventFiles = new ArrayList<>();
    	try {
    		eventFiles = getEventFileList(events);
    	} catch (Exception e) {
    		
    		e.printStackTrace();
    	}
    	model.addAttribute("eventFiles", eventFiles);
    	return "event_read"; 
    }
    
    
    @PostMapping("/submitEvent")
    public String submitEvent(@ModelAttribute Event event, 
    		@RequestParam("image") MultipartFile image, 
    		@RequestParam("title_image") MultipartFile title_image) throws Exception {
    	
    	// 관리자 ID 인지 일반회원 ID 인지 확인 하는 로직 필요
    	
    	List<EventFile> imgFileList = new ArrayList<>(); 
        // 이미지 업로드 처리
        if (!image.isEmpty() && !title_image.isEmpty()) {
        	
            String uploadDir = "src/main/resources/static/images/";
            String fileName = image.getOriginalFilename();
            String fileName2 = title_image.getOriginalFilename();
            try {
//            	System.out.println("fileName : " + fileName );
//            	System.out.println("fileName2 : " + fileName2 );
            	imgFileList = store(image, title_image);
                image.transferTo(new File(fileName));
                title_image.transferTo(new File(fileName2));
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception(e.getMessage());
//                return "redirect:/eventform";
            } catch (Exception e) {
            	e.printStackTrace();
//            	return "redirect:/eventform";
            	throw new Exception(e.getMessage());
            }
            
        }
        
        Event e = new Event();
        e.setTitle(event.getTitle());
        
        // event save
        eventService.saveEvent(e);
        
//        System.out.println("========================r.event_id" + e.getEvent_id());
        
        // event_file save
        for(EventFile ef : imgFileList) {
        	ef.setEvent(e);
        	eventService.saveEventFile(ef);
//        	System.out.println("++++++++++++++++++++++f.getEvent_file_id() : " + ef.getEvent_file_id());
        }
        

        return "redirect:/eventlist";
    }
    
    //첨부파일 경로
    @Value("C:\\20240821_workspace\\ReviewProject\\src\\main\\resources\\static\\images") // application 의 properties 의 변수
    private String uploadPath;
    
    public List<EventFile> store(MultipartFile file, MultipartFile fileTitle) throws Exception {

        // 이미지 파일만 업로드
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
//        	System.out.println("this file is not image type : " + file.getContentType());
//        	System.out.println("image : " + Objects.requireNonNull(file.getContentType()).startsWith("image"));
        	
//        	throw new Exception("image File empty");
        }
        
//        // 이미지 파일만 업로드
//        if (!Objects.requireNonNull(fileTitle.getContentType()).startsWith("title_image") ) {
//        	System.out.println("this file is not image type : " + fileTitle.getContentType());
//        	System.out.println("title_image : " + Objects.requireNonNull(fileTitle.getContentType()).startsWith("title_image"));
//        	
//        	throw new Exception("title image File empty");
//        }

        List<EventFile> list = new ArrayList<>();
        String orginalName = "", fileName = "", orginalTitleName = "", fileTitleName="", saveName = "", saveTitleName = "";
        boolean isFile = false, isTitleFile = false;
        
        if(null != file.getOriginalFilename() && !"".equals(file.getOriginalFilename())) {
        	isFile = true;
        }
        
        if(null != fileTitle.getOriginalFilename() && !"".equals(fileTitle.getOriginalFilename())) {
        	isTitleFile = true;
        }
        
        // 실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로 => 바뀐 듯 ..
        // 내용
        if(isFile) {
        	orginalName = file.getOriginalFilename();
        	assert orginalName != null;
        	fileName = orginalName.substring(orginalName.lastIndexOf("\\") + 1);
        }
        
        // 타이틀 이미지
        if(isTitleFile) {
        	orginalTitleName = fileTitle.getOriginalFilename();
        	assert orginalTitleName != null;
        	fileTitleName = orginalTitleName.substring(orginalTitleName.lastIndexOf("\\") + 1);
        }

//        System.out.println("fileName: "+fileName + " / fileTitleName : " + fileTitleName);

        // 날짜 폴더 생성
        String folderPath = makeFolder();
//        System.out.println("Original folderPath : " + folderPath);
        
        long unixTime = Instant.now().getEpochSecond();
        String unixTimeString = String.valueOf(unixTime);
        
//		System.out.println("Unix Time : " + unixTimeString);

        // UUID
        String uuid = "jin_test";

        // 저장할 파일 이름 중간에 "_"를 이용해서 구현
        // 내용
        if(isFile) {
        	saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
        }
        
        // 타이틀 이미지
        if(isTitleFile) {
        	saveTitleName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileTitleName;
        }

        System.out.println("saveName : " + saveName + " / saveTitleName : " + saveTitleName);
        Path savePath = Paths.get(saveName);// 내용
        Path saveTitlePath = Paths.get(saveTitleName); // 타이틀

        try {
        	if(isFile) {
        		file.transferTo(savePath); // 실제 내용 이미지 저장
        	}
        	if(isTitleFile) {
        		fileTitle.transferTo(saveTitlePath); // 실제 타이틀 이미지 저장
        	}
        	
        	EventFile ef = new EventFile();
        	if(isFile) {
        		ef.setEvent_filename(orginalName); // 내용
        		ef.setEvent_file(Base64.getEncoder().encodeToString(file.getBytes()));// 내용
        	}
        	
        	if(isTitleFile) {
        		ef.setEvent_title_filename(orginalTitleName);// 타이틀이미지
        		ef.setEvent_title_file(Base64.getEncoder().encodeToString(fileTitle.getBytes()));// 타이틀이미지
        	}
        	
        	if(isFile || isTitleFile) {
        		list.add(ef);
        	}

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;

	}
    
    /*날짜 폴더 생성*/
    private String makeFolder() {
        // 현재 시간을 사용하여 폴더명을 생성합니다. 
        // 하지만 매번 새 폴더를 만들지 않으려면 기존 폴더가 존재하는지 확인합니다.
        String folderPath = "images"; // 혹은 고정된 폴더 이름을 사용할 수 있습니다.

        // 폴더 생성 경로
        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            boolean mkdirs = uploadPathFolder.mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("Failed to create directory: " + uploadPathFolder.getAbsolutePath());
            }
        }

        return folderPath;
    }

    
    public List<EventFile> getEventFileList(List<Event> eventList) throws Exception{
    	
    	List<EventFile> rtnList = new ArrayList<>();
    	FileOutputStream fos = null;
    	FileOutputStream fosTitle = null;
    	try {
    		for(Event e : eventList) {
    			List<EventFile> rfList = eventService.getFindEventFileByEventId(e);
//    			System.out.println("===============rfList.getEventFileList : " + rfList.size());
    			if(null != rfList && 0 < rfList.size()) {
    				for(EventFile ef : rfList) {
    					byte[] data = Base64.getDecoder().decode(ef.getEvent_file());
    					byte[] dataTitle = Base64.getDecoder().decode(ef.getEvent_title_file());
//    					System.out.println("getEventFileList ================ef.getEvent_file_id : " + ef.getEvent_file_id());
//    					System.out.println("getEventFileList ================ef.getEvent_filename : " + ef.getEvent_filename());
//    					System.out.println("getEventFileList ================ef.getEvent_title_filename : " + ef.getEvent_title_filename());
    					// 임시 파일 저장
    					String folderPath = makeFolder();
//    			        System.out.println("folderPath : " + folderPath);

    			        // UUID
    			        String uuid = "jin_test";

    			        // 저장할 파일 이름 중간에 "_"를 이용해서 구현
    			        String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + ef.getEvent_filename();
    			        String saveNameTitle = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + ef.getEvent_title_filename();
	    				fos = new FileOutputStream(saveName);
	    				fos.write(data);
	    				fos.close();
	    				fosTitle = new FileOutputStream(saveNameTitle);
	    				fosTitle.write(dataTitle);
	    				fosTitle.close();
    					rtnList.add(ef);
    				}
    			}
    		}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("파일 데이터에 문제가 발생했습니다");
		} finally {
			if(null != fos) {
				fos.close();
			}
			if(null != fosTitle) {
				fosTitle.close();
			}
		}
    	return rtnList;
    }
    
    @PostMapping("/updateEvent")
    public String updateEvent(@ModelAttribute Event event, 
    		@RequestParam("imgChg") String imageChg, 
    		@RequestParam("imgChgTitle") String imgChgTitle, 
    		@RequestParam("image") MultipartFile image, 
    		@RequestParam("title_image") MultipartFile title_image) throws Exception {
    	
    	// 관리자 ID 인지 일반회원 ID 인지 확인 하는 로직 필요
    	
    	List<EventFile> imgFileList = new ArrayList<>(); 
        // 이미지 업로드 처리
        if (!image.isEmpty() || !title_image.isEmpty()) {
        	
            String uploadDir = "src/main/resources/static/images/";
            String fileName = image.getOriginalFilename();
            String fileName2 = title_image.getOriginalFilename();
            try {
            	System.out.println("fileName : " + fileName );
            	System.out.println("fileName2 : " + fileName2 );
            	imgFileList = store(image, title_image);
            	if(null != fileName && !"".equals(fileName)) {
            		image.transferTo(new File(fileName));
            	}
            	if(null != fileName2 && !"".equals(fileName2)) {
            		title_image.transferTo(new File(fileName2));
            	}
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception(e.getMessage());
//                return "redirect:/eventform";
            } catch (Exception e) {
            	e.printStackTrace();
//            	return "redirect:/eventform";
            	throw new Exception(e.getMessage());
            }
            
        }
        
        Event e = new Event();
        e.setEvent_id(event.getEvent_id());
        e.setTitle(event.getTitle());
        
        // events save
        eventService.updateEvent(e);
        
        System.out.println("========================e.event_id" + e.getEvent_id());
        System.out.println("========================imageChg " + imageChg);
        System.out.println("========================imgChgTitle " + imgChgTitle);
        
        if(imageChg.indexOf(",") > -1) {
        	imageChg = imageChg.split(",")[1];
        }
        
        if(imgChgTitle.indexOf(",") > -1) {
        	imgChgTitle = imgChgTitle.split(",")[1];
        }
        
        System.out.println("111========================imageChg " + imageChg);
        System.out.println("111========================imgChgTitle " + imgChgTitle);
        System.out.println((!"".equals(imageChg) && Integer.parseInt(imageChg) > 0) && (!"".equals(imgChgTitle) && Integer.parseInt(imgChgTitle) > 0));
        System.out.println((!"".equals(imageChg) && Integer.parseInt(imageChg) > 0) );
        System.out.println( (!"".equals(imgChgTitle) && Integer.parseInt(imgChgTitle) > 0));
        System.out.println( "imgFileList.size : "+ imgFileList.size() );
        
        if((!"".equals(imageChg) && Integer.parseInt(imageChg) > 0) && (!"".equals(imgChgTitle) && Integer.parseInt(imgChgTitle) > 0)) {
        	// event_file save
        	for(EventFile ef : imgFileList) {
        		ef.setEvent(e);
        		
        		int files = eventService.deleteEventFileByEventId(e);
        		System.out.println("files del : " + files);
        		
        		eventService.saveEventFile(ef);
        		System.out.println("++++++++++++++++++++++all e.getEvent_file_id() : " + ef.getEvent_file_id());
        	}
        } else {
        	if((!"".equals(imageChg) && Integer.parseInt(imageChg) > 0)) {
        		for(EventFile ef : imgFileList) {
            		ef.setEvent(e);
            		
            		eventService.updateEventFile(ef);
            		System.out.println("++++++++++++++++++++++imageChg e.getEvent_file_id() : " + ef.getEvent_file_id());
            	}
        	} else if((!"".equals(imgChgTitle) && Integer.parseInt(imgChgTitle) > 0)) {
        		for(EventFile ef : imgFileList) {
            		ef.setEvent(e);
            		
            		eventService.updateEventFileTitle(ef);
            		System.out.println("++++++++++++++++++++++imgChgTitle e.getEvent_file_id() : " + ef.getEvent_file_id());
            	}
        	}
        }
        
        System.out.println("end===============================================");
        
        

        return "redirect:/eventlist";
    }
    

    @PostMapping("/deleteEvent")
    public String deleteEvent(@ModelAttribute Event event) throws Exception {
    	
    	Event e = new Event();
    	e.setEvent_id(event.getEvent_id());
    	
//    	System.out.println("========================e.getEvent_id : " + e.getEvent_id());

    	int file = eventService.deleteEventFileByEventId(e);

		// events delete
//		System.out.println("(file + reply) : " + (file ));
		
		eventService.deleteEvent(e);
    	
    	return "redirect:/eventlist";
    }
    

}
