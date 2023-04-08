// th:inline : 자바스크립트를 써서 타임리프 속성을 쓸수도 있다는 뜻
async function boardLikeHandler(status,bId){
    let url=`/board/like/${status}/${bId}/handler.do`
    // alert(url);
    const resp=await fetch(url);
    if(resp.status===200){
        const json=await  resp.json();
        if(json.handler>0){
            alert(json.status+" "+json.handlerType+" 성공");
        }else {
            alert(json.status+" "+json.handlerType+" 실패");
        }
    }else{ // 실패
        alert("실패 status:"+resp.status);
    }
    // likes 에 bId 가 없어서 받아와야 한다. => 대괄호 두개 => 타임리프를 불러오는것!
}
