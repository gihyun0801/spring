
// 새로고침
const reloadBtn = document.querySelector("#reloadBtn");

reloadBtn.addEventListener("click", function(){

    getCompleteCount();
    getTotalCount();
})

//전체 Todo 개수 조회 및 출력하는 함수 정의----------------------------------------------

const totalCount = document.querySelector("#totalCount");
const completeCount = document.querySelector("#completeCount");


function getTotalCount(){

   // 비동기로 서버에서 전체 Todo 개수를 조회하는
   // fetch() Api 코드 작성
   // fetch : 가지고오다 라는 뜻
  
   fetch("/ajax/totalCount").then(response => {
  console.log("response : " + response);
    //response.text() : 응답 데이터를 문자열/숫자 형태로 변환한
                    // 결과를 가지는 Promise 객체 반환
    return response.text();

   }).then(result => {
 
       console.log("result : " , result);
      
       totalCount.innerText = result;
   });
}



getTotalCount();


// completeCount 값 비동기 통신으로 얻어와서 화면 출력

// 첫 번째 then 의 response : 응답 결과 , 요청 주소 응답 데이터 등이 담겨있음
// response.text() 응답데이터를 text형태로 변화

function getCompleteCount(){

fetch("/ajax/getCompleteCount").then(response => {
   console.log("response : " + response);
    return response.text(); 

}).then(result => {
 
     console.log("result : " + result);
     completeCount.innerText = result;
     
})

}

getCompleteCount();


//비동기로 할 일 추가하기---------------------------------------



const todoTitle = document.querySelector("#todoTitle");
const todoContent = document.querySelector("#todoContent");
const addBtn = document.querySelector("#addBtn");

// 할 일 상세 조회 관련 요소
const popupLayer = document.querySelector("#popupLayer");
const popupTodoNo = document.querySelector("#popupTodoNo");
const popupTodoTitle = document.querySelector("#popupTodoTitle");
const popupComplete = document.querySelector("#popupComplete");
const popupRegDate = document.querySelector("#popupRegDate");
const popupTodoContent = document.querySelector("#popupTodoContent");
const popupClose = document.querySelector("#popupClose");

// 상세 조회 버튼

const deleteBtn = document.querySelector("#deleteBtn");
const updateView = document.querySelector("#updateView");
const changeComplete = document.querySelector("#changeComplete");


//수정 버튼 클릭시 레이어

const updateLayer = document.querySelector("#updateLayer");
const updateTitle = document.querySelector("#updateTitle");
const updateContent = document.querySelector("#updateContent");
const updateBtn = document.querySelector("#updateBtn");




addBtn.addEventListener("click", function(){

    // 비동기로 할 일 추가하기 fetch() 코드 작성

    // 요청 주소 : "/ajax/add"
    // 데이터 전달 방식(method) : post

    const param = {
        "todoTitle" : todoTitle.value,
        "todoContent" : todoContent.value
    };
 
    
    
// 기능추가 method 는 post 방식
// 요청 데이터의 형식을 JSON 으로 지정
// 보낼때 param을 json 객체로 변환
    fetch("/ajax/addTodo", {method : "POST", 
    headers :  {"Content-Type" : "application/json"} , 
    body : JSON.stringify(param) } ).then(response => {
             
        return response.text();

    }).then(temp => {  // 첫번째 then 에서 반환된 값중 변환된 text를 temp에 저장
              
          if(temp > 0){
                    // alert("추가 성공");

                    // 추가 성공한 제목, 내용 지우기00000
                    todoTitle.value = "";
                    todoContent.value = "";
 
                    
                    getTotalCount();


                    // 할 일 목록 다시 조회

                    selectTodoList();

          }else{
                    alert("추가 실패");
          }

    });




});

//할 일 목록 조회 관련 요소
const tbody = document.querySelector("#tbody");

// 비동기로 할 일 목록을 조회하는 함수

const selectTodo = (url) => {
//매개변수 url = "/ajax/detail?todNo= 10" 형태의 문자열
//응답 데이터의 객체를 .text() 로 받으면 sTRING 이 되지만 바로 response.json() 을 사용하면 알아서 json 으로 바뀐다.
     fetch(url).then(response => {
        return response.text();
     }).then(result => {
          const todo = JSON.parse(result);

          console.log(todo);



          //0-9-09-9-9-9

          popupTodoNo.innerText = todo.todoNo;
          popupTodoTitle.innerText = todo.todoTitle;
          popupComplete.innerText = todo.complete;
          popupRegDate.innerText = todo.regDate;
          popupTodoContent.innerText = todo.todoContent;

          //popup layer 보이게 하기

          popupLayer.classList.remove("popup-hidden");

          
     });


};

popupClose.addEventListener("click", function(){
    popupLayer.classList.add("popup-hidden");
})

const selectTodoList = () => {

      
    fetch("/ajax/selectList").then(response => {
 
         return response.text();
         
    }).then(result => {
        console.log(result);
        console.log(typeof result);
        //객체가 아닌 문자열 형태


        // String 인 지금 result를 JSON 으로 변환
        // JSON.parse(JSON 데이터) : String -> object
        // -String 형태의 JSON 데이터를 JS Object 타입으로 변환

        // JSON.Stringify(JS Object) : object -> String
        // - JS Object 타입을 String 형태의 JSON 데이터로 변환

        const todoList = JSON.parse(result);

        console.log(todoList);

        //-----------------------------------------------------------

        // #tbody

        // #tbody 에 tr/td 요소를 생성해서 내용 추가 

        tbody.innerHTML = "";

        for(let todo of todoList){ //향상된 for문 배열을 돌때 

            // tr 태그 생성

            const tr = document.createElement("tr");

            const arr = ['todoNo', 'todoTitle', 'complete', 'regDate'];

            for(let key of arr){
                const td = document.createElement("td");
                
//제목인 경우

                   if(key === 'todoTitle'){
                        const a = document.createElement("a");
                        a.innerText = todo[key];
                        a.href = "/ajax/detail?todoNo=" + todo.todoNo;
                        td.append(a);
                        tr.append(td);

                        // a 태그 클릭 시 기본 이벤트(페이지 이동) 막기
                   a.addEventListener("click", function(e){
                        e.preventDefault();

                        // 할 일 상세 조회 비동기 요청
                        // e.target.href : 클릭된 a태그의 href 속성 값
                        selectTodo(e.target.href);
                   });

                   continue;
                   };

                  td.innerText = todo[key];
                  tr.append(td); 

            }

//tbody 에 한 행씩 추가

tbody.append(tr);

        }


    });


};

selectTodoList();


// -------------------------------------------------------------------------------

// 삭제 버튼 클릭 시
deleteBtn.addEventListener("click", () => {
          // 취소 클릭 시 아무것도 안함

          if(!confirm("정말 삭제하시겠습니까 ?")){
                 return;
          }else{
                //삭제할 할 일 번호 얻어오기

                const todoNo = popupTodoNo.innerText; //todono 얻어오기

                fetch("/ajax/delete", {method : "DELETE", headers : {"Content-Type" : "application/json"}  , body:todoNo
             }).then(response => {
                      return response.text();
                }).then(result => {

                        if(result > 0){
                                alert("삭제 성공");
                                popupLayer.classList.add("popup-hidden");
                                getCompleteCount();
                                getTotalCount();
                                selectTodoList();
                                
                        }else{
                                    alert("삭제 실패");
                        };

                });
          }
});

//수정


updateView.addEventListener("click", function(){

   const todoNo = popupTodoNo.innerText;

    popupLayer.classList.add("popup-hidden");

    updateLayer.classList.remove("popup-hidden");
    updateTitle.classList.remove("popup-hidden");


    updateTitle.value = popupTodoTitle.innerHTML;
    updateContent.value = popupTodoContent.innerHTML;

    

    

    

});

updateBtn.addEventListener("click", function(){

//  console.log(updateTitle.value);
//  console.log(updateContent.value);

const ReupdateTitle = updateTitle.value;
const ReupdateContent = updateContent.value;
const todoNo = popupTodoNo.innerText;


console.log(todoNo);

const updateModal = {
    "todoNo" : todoNo,
   "todoTitle" : ReupdateTitle,
   "todoContent" : ReupdateContent

};

console.log(updateModal);


 fetch("/ajax/updateTodo", {method : "post", headers : 
 {"Content-Type" : "application/json"}, 
 body : JSON.stringify(updateModal)}).then(response => {
           return response.text();
 }).then(result => {
        
     if(result > 0){
        alert("수정완료");
        updateLayer.classList.add("popup-hidden");
        getCompleteCount();
        getTotalCount();
        selectTodoList();
        

     }else{
        alert("수정실패");
     };

 });

})

changeComplete.addEventListener("click", function(){

        
        


        if(popupComplete.innerText === 'Y'){
            popupComplete.innerText = 'N';
        }else{
            popupComplete.innerText = 'Y';
        };

        
        


     



});


