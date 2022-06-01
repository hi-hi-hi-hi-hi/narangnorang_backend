$(document).ready(function(){
	$("#pwCheck").on("click", function(){
		var pw = $("#password").val();
		var pw2 = $("#password2").val();
		var privilege= $("#privilege").val();
		if(pw != pw2){
			alert("비밀번호가 틀립니다.");
			location.href="/narangnorang/mypage";
		}else if(privilege == 0){
			location.href="/narangnorang/admin";
		}else{
			location.href="/narangnorang/mypage/edit";
		}
	});
	
	$("#password").on("keyup", function(event){
		if(event.keyCode == 13){
			$("#pwCheck").trigger("click");
		}
	});
});

