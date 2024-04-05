// 목록으로 버튼 동작

const goToList = document.querySelector("#goToList");

goToList.addEventListener("click", function(){
    // history.back();
    location.href="/";
});


// 완료 여부 변경 버튼 동작

const completeBtn = document.querySelector(".complete-btn");

completeBtn.addEventListener("click", function(e){
    
      const todoNo = e.target.dataset.todoNo;
     
    //   console.log(todoNo);

    // Y - N 변경

    let complete = e.target.innerText; //기존 완료 여부 값 얻어오기 //현재 N

    complete =(complete === 'Y') ? 'N' : 'Y';

    // 완료 여부 수정 요청하기

    location.href = 
    `/todo/changeComplete?todoNo=${todoNo}&complete=${complete}`;
    ;
      

});

//------------------------------------------------------------------------------

// 수정 버튼 클릭 시

const updateBtn = document.querySelector("#updateBtn");

updateBtn.addEventListener("click", function(e){

const todoNo = e.target.dataset.todoNo;

location.href = `/todo/update?todoNo=${todoNo}`;



})

//--------------------------------------------------------------------

//삭제 버튼

const deleteBtn = document.querySelector("#deleteBtn");

deleteBtn.addEventListener("click", function(e){

if(confirm("정말 삭제 하시겠습니까 ?")){
    const todoNo = e.target.dataset.todoNo;

    location.href = `/todo/delete?todoNo=${todoNo}`;
}else{
    
}




})

