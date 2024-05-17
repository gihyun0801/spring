/* 다음 주소 api 확인하기 */

function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
  

            //-----------------------------------------
             //참고항목 변수 필요없음
             // var extraAddr = ''; // 참고항목 변수
            //------------------------------------------


            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            //---------------------------------------------필ㅇ없음
       /*if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("sample6_extraAddress").value = extraAddr;
            
            } else {
                document.getElementById("sample6_extraAddress").value = '';
            }*/      
         //필요없음


            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById("postcode").value = data.zonecode;
            document.getElementById("address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("detailAddress").focus();
        }
    }).open();
}

const searchAddress = document.querySelector("#searchAddress").addEventListener("click", function(){
    execDaumPostcode();
});





















// **** 회원 가입 유효성 검사 *****


// 필수 입력 항목의 유효성 검사 여부를 체크하기 위한 객체




const checkObj = {

         "memberEmail" : false,
         "memberPw" : false,
         "memberPwConfirm" : false,
         "memberNickname" : false,
         "memberTel" : false,
         "authKey" : false


};

// -----------------------------------------------------------------------


/* 이메일 유효성 검사 */


const memberEmail = document.querySelector("#memberEmail");
const emailMessage = document.querySelector("#emailMessage");

// 이메일이 입력 될 때 마다 유효성 검사 수행
memberEmail.addEventListener("keyup", function(e){
     
    // 이메일 인증 후 이메일이 변경된 경우

    checkObj.authKey = false;
    document.querySelector("#authKeyMessage").innerText = "";

    // 나중에 처리

    // 작성된 이메일 값 얻어오기

    const inputEmail = e.target.value;

    console.log(inputEmail); // 지금 입력하고있는값

    // 3) 입력된 이메일이 없을 경우 

    if(inputEmail.trim().length == 0){
       emailMessage.innerText = "메일을 받을 수 있는 이메일을 입력해주세요";

       // 메시지에 색상을 추가하는 클래스 모두 제거

       emailMessage.classList.remove("confirm", "error");
       // 두 가지 클래스를 지워라

       // 이메일 유효성 검사 여부를 false 로 변경

       checkObj.memberEmail = false;

       // 잘못 입력한 띄어쓰기가 있을 경우 없앰
       memberEmail.value = "";

       return;
      };


      // 4) 입력된 이메일이 있을 경우 정규식 검사

      const regExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

       // 입력 받은 이메일이 정규식과 일치하지 않는 경우

       // (알맞은 이메일 형태가 아닌 경우)

       if(!regExp.test(inputEmail)){
            emailMessage.innerText = "알맞은 이메일 형식으로 작성해주세요.";
            emailMessage.classList.add("error");
            emailMessage.classList.remove("confirm");
            checkObj.memberEmail = false;
            return;
       };

       //5) 유효한 이메일 형식일 경우 중복 검사 수행

       // 비동기(ajax)

       

       fetch("/member/checkEmail?memberEmail=" + inputEmail).then(response => {
                    return response.text();
       }).then(result => {
    

           if(result > 0){
            emailMessage.innerText = "중복된 이메일이 있습니다";
            emailMessage.classList.add("error");
            emailMessage.classList.remove("confirm");
            checkObj.emailMessage = false;
            return;
           }

           // 중복 x

           emailMessage.innerText = "사용 가능한 이메일 입니다";
           emailMessage.classList.add("confirm");
           emailMessage.classList.remove("error");
           checkObj.memberEmail = true;
         
       }).catch(error => {
        // fetch() 수행 중 예외 발생 처리
              console.log(error);
       });

       

       
});

// 이메일 인증


// 인증번호 받기 버튼
const sendAuthKeyBtn = document.querySelector("#sendAuthKeyBtn");

// 인증번호 입력 input
const authKey = document.querySelector("#authKey");

//인증번호 입력 후 확인 버튼
const checkAuthKeyBtn = document.querySelector("#checkAuthKeyBtn");

// 인증번호 관련 메시지 출력 span
const authKeyMessage = document.querySelector("#authKeyMessage");


let authTimer; // 타이머 역할을 할 setInterval 함수

const initMin = 4; // 타이머 초기값 (분)
const initSec = 59; // 타이머 초기값 (초)
const initTime = "05:00"; 

//실제 줄어드는 시간을 저장할 변수

let min = initMin;
let sec = initSec;

// 인증번호 받기 버튼 클릭 시 

sendAuthKeyBtn.addEventListener("click", function(e){
    // 인증번호 입력할 input
  
     
    checkObj.authKey = false;
    authKeyMessage.innerText = "";

    if(!checkObj.memberEmail){
        alert("유효한 이메일 작성 후 클릭해 주세요");
        return;
    }

    //클릭 시 타이머 숫자 초기화
    
    min = initMin;
    sec = initSec;

    // 이전 동작중인 인터벌 클리어
    clearInterval(authTimer);
//******************************************* */
// 비동기로 서버에서 메일보내기

fetch("/email/signup", {method : "POST", headers : {"Content-Type" : "application/json"}, body : memberEmail.value    }  ).then(response => {
            return response.text();
}).then(result => {
                if(result == 1){
                    console.log("인증 번호 발송 성공");
                }else{
                    console.log("인증 번호 발송 실패");
                };
});



//메일은 비동기로 서버에서 보내라고 하고
//화면에서는 타이머 측정하기

authKeyMessage.innerText = initTime;
authKeyMessage.classList.remove("confirm", "error");

alert("인증번호가 발송되었습니다");

// setInterval(함수, 지연시간)
/// - 지연시간 만큼 시간이 지날 때 마다 함수 실행

// clearInterval()
//매개변수로 전달받은 interval 멈춤

authTimer = setInterval(function(){

    authKeyMessage.innerText = `${addZero(min)}:${addZero(sec)}`;

    // 0분 0초 인 경우 ("00:00" 출력 후)

    if(min == 0 && sec == 0){
            checkObj.authKey = false; //인증 못했음
            clearInterval(authTimer);
            authKeyMessage.classList.add("error");
            authKeyMessage.classList.remove("confirm");
            return;
    };


    // 초가 0 인 경우

    if(sec == 0){
        sec = 60;
        min--;
    };

    sec--; // 1초 감소

}, 1000);

});

// 전달받은 숫자가 10 미만인 경우(한자리) 앞에 0 붙혀서 반환

function addZero(number){
   
    if(number < 10){
            return "0" + number;    
        }else{
            return number;
        }

};

// --------------------------------------------------------------------------------------------------

//    인증하기 버튼 클릭 시 
//    입력된 인증번호를 비동기로 서버에 전달
// -> 입력된 인증번호와 발급된 인증번호가 같은지 비교
//    같으면 1, 아니면 0 반환
//    단, 타이머가 00:00초가 아닐 경우 수행



checkAuthKeyBtn.addEventListener("click", function(){

      if(min === 0 && sec === 0){
                alert("인증번호 입력 제한시간을 초과하였습니다");
                return;
      }
      
      if(authKey.value.length < 6){
           alert("인증번호를 정확히 입력해 주세요");
           return;
      }

       
      //입력받은 이메일, 인증번호로 객체 생성

      const obj = {
            
         "email" : memberEmail.value,
         "authKey" : authKey.value



      };

      fetch("/email/checkAuthKey", {method : "POST", headers : {"Content-Type" : "application/json"}, body : JSON.stringify(obj)  })
      .then(response => {
         return response.text();
      })
      .then(result => {
   
              if(result == 0){
                alert("인증실패");
                checkObj.authKey = false;
                return;
              }
                clearInterval(authTimer);
                authKeyMessage.innerText = "인증 되었습니다";
                authKeyMessage.classList.remove("error");
                authKeyMessage.classList.add("confirm");

                checkObj.authKey = true;
              
         
      });

     
});

//------------------------------------------------------------------------------------------------------------

/* 비밀번호 / 비밀번호 확인 유효성 검사 */ 

// 1) 비밀번호 관련 요소 얻어오기

//비밀번호
const memberPw = document.querySelector("#memberPw");

const memberPwConfirm = document.querySelector("#memberPwConfirm");

const pwMessage = document.querySelector("#pwMessage");

// 5)비밀번호, 비밀번호 확인이 같은지 검사하는 함수

const checkPw = () => {

        //같을 경우

        if(memberPw.value == memberPwConfirm.value){
            pwMessage.innerText = "비밀번호가 일치합니다";
            pwMessage.classList.add("confirm");
            pwMessage.classList.remove("error");
            checkObj.memberPwConfirm = true;
           return;   
        }
  
        
        pwMessage.innerText = "비밀번호가 일치하지 않습니다";
        pwMessage.classList.add("error");
        pwMessage.classList.remove("confirm");
        checkObj.memberPwConfirm = false;
};




memberPw.addEventListener("input", function(e){
 
   const inputPw = e.target.value;

    if(inputPw.trim().length == 0){
        pwMessage.innerText = "영어,숫자,특수문자(!,@,#,-,_) 6~20글자 사이로 입력해주세요.";
        pwMessage.classList.remove("confirm", "error");
        checkObj.memberPw = false;
        memberPw.value = ""; // 처음에 띄어쓰기 입력 못하게 하기
        return;
    } 

    const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;
    



    if(!regExp.test(inputPw)){
  
            pwMessage.innerText = "올바른 비밀번호 형식을 입력하세요";
            pwMessage.classList.add("error");
            pwMessage.classList.remove("confirm");
            checkObj.memberPw = false;


           

            return;

    }
    // 유효한 경우

    pwMessage.innerText = "유효한 비밀번호 형식입니다";
    pwMessage.classList.add("confirm");
    pwMessage.classList.remove("error");
    checkObj.memberPw = true;

  //비밀번호 입력 시 확인란의 값과 비교하는 코드 추가
    
   if(memberPwConfirm.value.length > 0){
    checkPw();
   }
   
    

      

});


memberPwConfirm.addEventListener("input", function(){
     

   if(checkObj.memberPw){


           checkPw(); //비교함수
           return;



   }

   //memberPw 가 유효하지 않는경우

    // memberPwConfirm 검사 x

    checkObj.memberPwConfirm = false;

   
     
});





//닉네임 정규식 검사


//닉네임 input
const memberNickname = document.querySelector("#memberNickname");

//닉네임 message
const nickMessage = document.querySelector("#nickMessage");

memberNickname.addEventListener("input", function(e){

    const regExp = /^[가-힣\w\d]{2,10}$/;

    const inputNickName = e.target.value;

    console.log(inputNickName);
 
    if(inputNickName.trim().length === 0){
        memberNickname.value = "";
        nickMessage.innerText = "한글,영어,숫자로만 2~10글자";
        nickMessage.classList.remove("confirm", "error");
        return;
    }


    if(!regExp.test(inputNickName)){
        nickMessage.innerText = "올바른 입력을 하세요";
        nickMessage.classList.add("error");
        nickMessage.classList.remove("confirm");
        checkObj.memberNickname = false;
        return;
    }

    nickMessage.innerText = "올바른 형식입니다";
    nickMessage.classList.add("confirm");
    nickMessage.classList.remove("error");
    checkObj.memberNickname = true;

    fetch("/email/checkNickName?memberNickname=" + inputNickName).then(response => {
        
            return response.text();
                

    }).then(result => {
              
        if(result == 1){
                nickMessage.innerText = "중복된 닉네임이 있습니다";
                nickMessage.classList.add("error");
                nickMessage.classList.remove("confirm");
                checkObj.memberNickname = false;
                return;
        }



    });
     
});



//----------------------------------------

//전화번호 정규식 검사


const memberTel = document.querySelector("#memberTel");

const telMessage = document.querySelector("#telMessage");

memberTel.addEventListener("input", function(e){

    const regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;

    
    const inputTel = e.target.value;

    if(inputTel.trim().length == 0){
 
        telMessage.innerText = "전화번호를 입력해주세요.(- 제외)";
        telMessage.classList.remove("confirm", "error");
        memberTel.value = "";
         checkObj.memberTel = false;
        return;

    };

    if(!regExp.test(inputTel)){
        telMessage.innerText = "올바른 전화번호를 입력하세요";
        telMessage.classList.remove("confirm");
        telMessage.classList.add("error");
        checkObj.memberTel = false;
        return;
      

    };
  

    telMessage.innerText = "올바른 전화번호 형식입니다";
        telMessage.classList.remove("error");
        telMessage.classList.add("confirm");
        checkObj.memberTel = true;
        console.log(checkObj);
});

// 회원 가입 버튼 클릭 시 전체 유효성 검사 여부 확인

const signUpForm = document.querySelector("#signUpForm");

signUpForm.addEventListener("submit", function(e){
  
     //checkObj 가 하나라도 false 가 있으면 안됌 

     for(let key in checkObj){ // for in 문 = key 값을 순서대로 꺼냄
            if(!checkObj[key]){
                    //false 인 경우 // 유효하지 않은 경우
                    
                    let str; //출력할 메시지를 저장할 변수

                    switch(key){
                        case "memberEmail" : str = "이메일이 유효하지 않음"; break;
                        case "authKey" : str = "이메일이 인증되지 않음"; break;
                        case  "memberPw" : str = "비밀번호가 유효하지 않음"; break;
                        case "memberPwConfirm" : str = "비밀번호가 일치하지 않음" ; break;
                        case  "memberNickname" : str = "닉네임이 유효하지 않음" ; break;
                        case "memberTel" : str = "전화번호가 유효하지 않음"; break;
                    }

                    alert(str);

                    document.getElementById(key).focus();
                  e.preventDefault();
                  return;
            }
     }

});












































































































