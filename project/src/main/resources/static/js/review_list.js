$(function(){
	$("#replyBtn").click(function(e){
		e.preventDefault();
		console.log("폼 제출 시");
		
		let my = $(this);
		let name = my.attr("name");
		let review_id = name.split("_")[0];
		console.log(review_id);
		console.log($("#" + review_id + "_review_reply"));
		
		var formData = new FormData($("#" + review_id + "_review_reply")[0]);
		console.log(formData);
		
		
		$.ajax({
		            url: '/submitReviewReply', 
		            processData: false,
		            contentType: false,
		            data: formData,
		            type: 'POST',
		            success: function(result) {
/*alert 문구수정 */	alert("답변이 등록되었습니다.");
					 window.location.href = '/reviewListAdmin';
		            },
		            error: function(xhr, status, error) {
		                console.error("업로드 실패: ", error);
		                alert("업로드 중 오류가 발생했습니다.");
		            }
        });
    });    
	
	$("#saveBtn").click(function(e){
		e.preventDefault();
		console.log("폼 제출 시");
		
		let my = $(this);
		let name = my.attr("name");
		let review_id = name.split("_")[0];
		console.log(review_id);
		console.log($("#" + review_id + "_review_reply"));
		
		var formData = new FormData($("#" + review_id + "_review_reply")[0]);
		console.log(formData);
		
		
		$.ajax({
		            url: '/submitReviewReply', 
		            processData: false,
		            contentType: false,
		            data: formData,
		            type: 'POST',
		            success: function(result) {
/*alert 문구수정 */   alert("업데이트가 완료되었습니다.");
				    window.location.href = '/reviewListAdmin';
		            },
		            error: function(xhr, status, error) {
		                console.error("업로드 실패: ", error);
		                alert("업로드 중 오류가 발생했습니다.");
		            }
        });
    });    
	
	$("#deleteBtn").click(function(e){
		e.preventDefault();
		console.log("폼 제출 시");
		
		let my = $(this);
		let name = my.attr("name");
		let review_id = name.split("_")[0];
		console.log(review_id);
		console.log($("#" + review_id + "_review_reply"));
		
		var formData = new FormData($("#" + review_id + "_review_reply")[0]);
		console.log(formData);
		
		
		$.ajax({
		            url: '/delReviewReplyReviewId', 
		            processData: false,
		            contentType: false,
		            data: formData,
		            type: 'POST',
		            success: function(result) {
/*alert 문구수정 */   alert("삭제되었습니다.");
					window.location.href = '/reviewListAdmin';
		            },
		            error: function(xhr, status, error) {
		                console.error("업로드 실패: ", error);
		                alert("업로드 중 오류가 발생했습니다.");
		            }
        });
    });    
	
	
	$("#cancelBtn").click(function(e){
		e.preventDefault();
		window.location.href = '/reviewListAdmin';
	});
	
	function btnHide(){
		$('input[name$="reply_ids"]').each(function(idx, obj){
			console.log("idx : " + idx);
			console.log(obj);
			if($(obj).attr("id")){
				$("#" + $(obj).val() + "_review_reply").find("#editBtn").attr("style", "display:inline;");
				$("#" + $(obj).val() + "_review_reply").find("#editBtn").removeAttr("disabled");
				$("#" + $(obj).val() + "_review_reply").find("#replyBtn").attr("style", "display:none;")
				$("#" + $(obj).val() + "_review_reply").find("#reply_content").attr("readonly", true);
			}
		});
	}
	
	$( document ).ready(function() {
	    btnHide();
	});
	
});